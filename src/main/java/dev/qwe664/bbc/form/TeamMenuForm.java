package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 隊伍管理主選單。
 *
 * 讀取隊伍成員清單這件事直接呼叫 BentoBox 的 Island API（安全，純讀取），
 * 但「邀請、踢除、升職、轉讓隊長、離隊」這些會改變資料的動作，
 * 一律轉發成 BentoBox 官方指令（跟「傳送到島嶼」按鈕同樣的模式），
 * 不自己重寫這些邏輯——官方指令內建的權限檢查、事件觸發、確認提示
 * 都已經寫好且經過驗證，重寫一遍容易漏掉細節、產生跟官方行為不一致的 bug。
 *
 * 職級數值（反編譯 BentoBox 3.22.0 的 RanksManager 常數池確認，非猜測）：
 * ADMIN_RANK=10000、MOD_RANK=5000、OWNER_RANK=1000、SUB_OWNER_RANK=900、
 * MEMBER_RANK=500、TRUSTED_RANK=400、COOP_RANK=200、VISITOR_RANK=0、BANNED_RANK=-1。
 *
 * 這裡只顯示「真正的隊伍成員」（MEMBER_RANK 以上），不含 trusted/coop 這種
 * 外部協作關係——那是 BentoBox 另一個獨立功能（trust/coop），跟隊伍管理是分開的概念，
 * 現階段沒有一併做成表單。
 */
public class TeamMenuForm extends BaseForm {

    public TeamMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
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
            player.sendMessage("§c你目前沒有島嶼，無法管理隊伍。");
            return;
        }

        int viewerRank = island.getRank(player.getUniqueId());

        List<UUID> members = new ArrayList<>(island.getMemberSet());
        members.sort(Comparator.<UUID>comparingInt(uuid -> island.getRank(uuid)).reversed());

        var builder = SimpleForm.builder()
                .title("👥 隊伍管理");

        StringBuilder content = new StringBuilder("島嶼成員（共 ").append(members.size()).append(" 人）：\n");
        content.append("點擊成員可查看可執行的操作。");
        builder.content(content.toString());

        for (UUID memberUuid : members) {

            String name = plugin.getBentoBoxService().getPlayersManager().getName(memberUuid);
            int rank = island.getRank(memberUuid);

            String icon = switch (rank) {
                case RanksManager.OWNER_RANK -> "👑";
                case RanksManager.SUB_OWNER_RANK -> "⭐";
                default -> "👤";
            };

            String rankLabel = rankLabel(player, rank);
            String selfTag = memberUuid.equals(player.getUniqueId()) ? "（你）" : "";

            builder.button(icon + " " + name + selfTag + "\n§7" + rankLabel);
        }

        int inviteButtonId = -1;
        int leaveButtonId = -1;

        boolean canInvite = viewerRank >= RanksManager.SUB_OWNER_RANK;
        boolean isOwner = island.getOwner().equals(player.getUniqueId());

        if (canInvite) {
            builder.button("➕ 邀請玩家");
            inviteButtonId = members.size();
        }

        if (!isOwner) {
            builder.button("🚪 離開隊伍");
            leaveButtonId = members.size() + (canInvite ? 1 : 0);
        }

        builder.button("⬅ 返回島嶼選單");
        int backButtonId = members.size() + (canInvite ? 1 : 0) + (!isOwner ? 1 : 0);

        int finalInviteButtonId = inviteButtonId;
        int finalLeaveButtonId = leaveButtonId;

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openIslandMenu(player);
                return;
            }

            if (clickedId == finalInviteButtonId) {
                new TeamInvitePickerForm(plugin).open(player);
                return;
            }

            if (clickedId == finalLeaveButtonId) {
                plugin.getBentoBoxService().getPlayerCommandLabel(player)
                        .ifPresentOrElse(
                                label -> plugin.getCommandService().execute(player, label + " team leave"),
                                () -> player.sendMessage("§c查不到目前玩法，無法離開隊伍。")
                        );
                return;
            }

            if (clickedId < members.size()) {

                UUID targetUuid = members.get(clickedId);

                if (targetUuid.equals(player.getUniqueId())) {
                    player.sendMessage("§7這是你自己。");
                    open(player);
                    return;
                }

                int targetRank = island.getRank(targetUuid);
                String targetName = plugin.getBentoBoxService().getPlayersManager().getName(targetUuid);

                new TeamMemberActionForm(plugin, targetUuid, targetName, targetRank, viewerRank, isOwner).open(player);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    /**
     * 職級數值轉成顯示文字。
     *
     * 不自己維護一份中文對照表，改成直接呼叫 BentoBox 官方翻譯系統：
     * RanksManager.getRank(int) 回傳這個職級對應的語言檔參照鍵
     * （例如 OWNER_RANK -> "ranks.owner"），再交給
     * User.getTranslation(World, reference) 依照該玩家「目前所在世界」，
     * 從 BentoBox 語言檔查出實際顯示文字。
     *
     * 特地帶入 World（而不是不帶 World 的 getTranslation(reference)），
     * 是因為伺服器同時跑 AOneBlock、ChunkBlock 兩種玩法：反編譯確認
     * User.translate() 的查詢邏輯是先試 "<addon名稱小寫>.ranks.owner"
     * 這種玩法專屬覆寫，查不到才退回共用的 "ranks.owner"；不帶 World
     * 的版本沒有世界可判斷玩家在哪個玩法裡，永遠只查得到共用版本，
     * 之後如果想讓兩個玩法各自客製化職級稱呼（例如 ChunkBlock 想叫
     * 「區長」而不是「隊長」）就會沒作用。
     *
     * 好處：伺服器管理員之後想改稱呼，或伺服器本身是多語言環境，
     * 都不用改 BBC 的程式碼重新編譯，直接改 BentoBox 語言檔就會生效；
     * 也不會跟 BentoBox 本身、其他也在讀這份語言檔的外掛
     * （如原生 /ob team、/ch team 介面）顯示不一致。
     *
     * 反編譯 BentoBox 3.22.0（GitHub develop 分支原始碼確認，非猜測）
     * RanksManager.java 第 24-41 行：ADMIN_RANK_REF="ranks.admin"、
     * MOD_RANK_REF="ranks.mod"、OWNER_RANK_REF="ranks.owner"、
     * SUB_OWNER_RANK_REF="ranks.sub-owner"、MEMBER_RANK_REF="ranks.member"、
     * TRUSTED_RANK_REF="ranks.trusted"、COOP_RANK_REF="ranks.coop"、
     * VISITOR_RANK_REF="ranks.visitor"、BANNED_RANK_REF="ranks.banned"；
     * User.java 第 479 行 getTranslation(World, String, String...)、
     * 第 612 行 translate() 內部查詢順序確認過，非猜測。
     *
     * 保留舊的寫死中文對照表當 fallback：萬一語言檔缺這個 key
     * （reference 查不到回傳空字串，或翻譯結果剛好是空的），
     * 才不會直接顯示原始的 "ranks.xxx" 字串給玩家看。
     */
    public static String rankLabel(Player viewer, int rank) {
        String reference = RanksManager.getInstance().getRank(rank);
        if (!reference.isEmpty()) {
            String translated = User.getInstance(viewer).getTranslation(viewer.getWorld(), reference);
            if (translated != null && !translated.isEmpty() && !translated.equals(reference)) {
                return translated;
            }
        }
        return switch (rank) {
            case RanksManager.OWNER_RANK -> "隊長";
            case RanksManager.SUB_OWNER_RANK -> "副隊長";
            case RanksManager.MEMBER_RANK -> "成員";
            case RanksManager.TRUSTED_RANK -> "受信任";
            case RanksManager.COOP_RANK -> "合作";
            default -> "成員";
        };
    }
}
