package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.managers.RanksManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 單一隊伍成員的操作選單（升職、踢除、轉讓隊長）。
 *
 * 每個動作都是轉發成 BentoBox 官方指令，不自己重寫權限判斷邏輯，
 * 這裡的按鈕顯示與否只是「UI 層面先擋一次明顯不合理的操作」，
 * 真正的權限把關仍然是 BentoBox 官方指令自己做的。
 *
 * 目前只提供「升職」，沒有「降職」按鈕——反編譯 BentoBox 3.22.0 只找到
 * IslandTeamPromoteCommand 這一個指令類別，沒有對應的 Demote 指令類別，
 * 代表官方目前沒有內建的降職指令（要降職通常是踢除後重新邀請）。
 * 這點沒有把握到方法內部實際行為（只確認到指令類別存在與否，
 * 不代表 promote 指令本身有沒有支援反向操作），如果之後實測發現
 * 官方其實有支援降職，再回來補上這個按鈕。
 */
public class TeamMemberActionForm {

    private final BentoBoxBedrockCompanion plugin;
    private final UUID targetUuid;
    private final String targetName;
    private final int targetRank;
    private final int viewerRank;
    private final boolean viewerIsOwner;

    public TeamMemberActionForm(
            BentoBoxBedrockCompanion plugin,
            UUID targetUuid,
            String targetName,
            int targetRank,
            int viewerRank,
            boolean viewerIsOwner
    ) {
        this.plugin = plugin;
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.targetRank = targetRank;
        this.viewerRank = viewerRank;
        this.viewerIsOwner = viewerIsOwner;
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

        var builder = SimpleForm.builder()
                .title("👤 " + targetName)
                .content("目前職級：§7" + TeamMenuForm.rankLabel(player, targetRank));

        List<Runnable> actions = new ArrayList<>();

        boolean canManage = viewerRank > targetRank;

        if (canManage && targetRank < RanksManager.SUB_OWNER_RANK) {
            builder.button("⬆ 升為副隊長");
            actions.add(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team promote " + targetName),
                            () -> player.sendMessage("§c查不到目前玩法，無法執行操作。")
                    ));
        }

        if (canManage) {
            builder.button("👢 踢出隊伍");
            actions.add(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team kick " + targetName),
                            () -> player.sendMessage("§c查不到目前玩法，無法執行操作。")
                    ));
        }

        if (viewerIsOwner) {
            builder.button("👑 轉讓隊長給他");
            actions.add(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team setowner " + targetName),
                            () -> player.sendMessage("§c查不到目前玩法，無法執行操作。")
                    ));
        }

        builder.button("⬅ 返回隊伍管理");
        int backButtonId = actions.size();

        if (actions.isEmpty()) {
            builder.content("目前職級：§7" + TeamMenuForm.rankLabel(player, targetRank) + "\n\n§7你目前的職級無法對這位成員執行任何操作。");
        }

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                new TeamMenuForm(plugin).open(player);
                return;
            }

            actions.get(clickedId).run();
            new TeamMenuForm(plugin).open(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
