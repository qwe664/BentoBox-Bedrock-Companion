package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.database.objects.Island;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀請玩家選單，列出目前在線、且不在自己隊伍裡的玩家。
 *
 * 只能邀請「目前在線」的玩家——BentoBox 的邀請指令本身也要求目標玩家在線
 * （邀請是即時互動，離線玩家收不到邀請提示），所以這裡直接用
 * Bukkit.getOnlinePlayers() 篩選，不需要額外查詢離線玩家資料庫。
 */
public class TeamInvitePickerForm {

    private final BentoBoxBedrockCompanion plugin;

    public TeamInvitePickerForm(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {

        FloodgateApi api = FloodgateApi.getInstance();

        if (api == null) {
            player.sendMessage("§cFloodgate API 尚未初始化。");
            return;
        }

        if (!api.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage("§e目前只有基岩版玩家可以使用 Bedrock UI。");
            return;
        }

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        if (island == null) {
            player.sendMessage("§c你目前沒有島嶼，無法邀請玩家。");
            return;
        }

        List<Player> candidates = new ArrayList<>();

        for (Player online : Bukkit.getOnlinePlayers()) {

            if (online.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }

            if (island.getMemberSet().contains(online.getUniqueId())) {
                continue;
            }

            candidates.add(online);
        }

        var builder = SimpleForm.builder()
                .title("➕ 邀請玩家");

        if (candidates.isEmpty()) {

            builder.content("目前沒有可邀請的在線玩家。")
                    .button("⬅ 返回隊伍管理");

            builder.validResultHandler(response -> new TeamMenuForm(plugin).open(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content("選擇要邀請加入隊伍的玩家：");

        for (Player candidate : candidates) {
            builder.button("👤 " + candidate.getName());
        }

        builder.button("⬅ 返回隊伍管理");
        int backButtonId = candidates.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                new TeamMenuForm(plugin).open(player);
                return;
            }

            String targetName = candidates.get(clickedId).getName();

            plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team invite " + targetName),
                            () -> player.sendMessage("§c查不到目前玩法，無法送出邀請。")
                    );

            new TeamMenuForm(plugin).open(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
