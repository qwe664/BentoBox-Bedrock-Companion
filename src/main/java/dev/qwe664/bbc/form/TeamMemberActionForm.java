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

    /**
     * 一個表單按鈕對應的動作：實際要執行的邏輯，以及執行完之後
     * 要不要自動跳回隊伍管理選單。
     */
    private record MemberAction(Runnable action, boolean autoReturnToMenu) {
    }

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
            player.sendMessage(plugin.getLocaleService().get(player, "common.floodgate-not-ready", "§cFloodgate API 尚未初始化。"));
            return;
        }

        if (!api.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage(plugin.getLocaleService().get(player, "common.bedrock-only", "§e目前只有基岩版玩家可以使用 Bedrock UI。"));
            return;
        }

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title("👤 " + targetName)
                .content(locale.get(player, "team_member_action.current-rank", "目前職級：§7") + TeamMenuForm.rankLabel(plugin, player, targetRank));

        List<MemberAction> actions = new ArrayList<>();

        boolean canManage = viewerRank > targetRank;

        if (canManage && targetRank < RanksManager.SUB_OWNER_RANK) {
            builder.button(locale.get(player, "team_member_action.promote", "⬆ 升為副隊長"));
            // 升職指令沒有二次確認機制，執行完直接返回隊伍選單沒問題。
            actions.add(new MemberAction(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team promote " + targetName),
                            () -> player.sendMessage(locale.get(player, "common.no-gamemode", "§c查不到目前玩法，無法執行操作。"))
                    ), true));
        }

        if (canManage) {
            builder.button(locale.get(player, "team_member_action.kick", "👢 踢出隊伍"));
            // 反編譯確認 IslandTeamKickCommand extends ConfirmableCommand，
            // 唯獨踢除這個動作需要玩家在 BentoBox 自己跳出的原生二次確認畫面
            // 再答一次（新版 BentoBox 是原生 Minecraft 對話框，不是我們的表單）。
            // 這裡執行完指令後刻意不自動跳回隊伍選單，讓那個原生確認畫面自己
            // 跑完，不然兩套介面系統同時搶畫面，玩家會覺得轉場卡卡的，
            // 而且此時實際上人都還沒被踢掉（還在等確認），提早跳回選單看到
            // 的成員清單也是還沒更新的舊資料。
            actions.add(new MemberAction(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> {
                                plugin.getCommandService().execute(player, label + " team kick " + targetName);
                                player.sendMessage(locale.get(player, "team_member_action.kick-confirm-hint",
                                        "§e請在跳出的確認畫面完成確認，才會真的踢除。"));
                            },
                            () -> player.sendMessage(locale.get(player, "common.no-gamemode", "§c查不到目前玩法，無法執行操作。"))
                    ), false));
        }

        if (viewerIsOwner) {
            builder.button(locale.get(player, "team_member_action.transfer-owner", "👑 轉讓隊長給他"));
            // 反編譯確認 IslandTeamSetownerCommand 沒有繼承 ConfirmableCommand，
            // 不需要二次確認，執行完直接返回隊伍選單沒問題。
            actions.add(new MemberAction(() -> plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player, label + " team setowner " + targetName),
                            () -> player.sendMessage(locale.get(player, "common.no-gamemode", "§c查不到目前玩法，無法執行操作。"))
                    ), true));
        }

        builder.button(locale.get(player, "team_member_action.back-to-team-menu", "⬅ 返回隊伍管理"));
        int backButtonId = actions.size();

        if (actions.isEmpty()) {
            builder.content(locale.get(player, "team_member_action.current-rank", "目前職級：§7") + TeamMenuForm.rankLabel(plugin, player, targetRank)
                    + "\n\n" + locale.get(player, "team_member_action.no-actions", "§7你目前的職級無法對這位成員執行任何操作。"));
        }

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                new TeamMenuForm(plugin).open(player);
                return;
            }

            MemberAction action = actions.get(clickedId);
            action.action().run();

            if (action.autoReturnToMenu()) {
                new TeamMenuForm(plugin).open(player);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
