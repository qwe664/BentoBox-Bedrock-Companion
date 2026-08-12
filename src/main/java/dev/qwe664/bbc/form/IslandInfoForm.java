package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 顯示島嶼資訊（島主、成員、大小、建立時間）的表單。
 *
 * 純資訊顯示，不需要互動元件，用 SimpleForm 就夠了，
 * 內容用一段多行文字組成。
 *
 * 成員排名文字直接複用 TeamMenuForm.rankLabel()（同一套 BentoBox
 * 官方語言檔翻譯 + config.yml fallback 的邏輯），不再自己另外維護
 * 一份 RANK_NAMES 對照表——避免兩個地方各自一份稱呼，改一邊忘了改
 * 另一邊而顯示不一致。
 */
public class IslandInfoForm extends BaseForm {

    private static final int[] RANK_VALUES = {
            RanksManager.VISITOR_RANK,
            RanksManager.COOP_RANK,
            RanksManager.TRUSTED_RANK,
            RanksManager.MEMBER_RANK,
            RanksManager.SUB_OWNER_RANK,
            RanksManager.OWNER_RANK
    };

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public IslandInfoForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
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

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        if (island == null) {
            player.sendMessage(plugin.getLocaleService().get(player, "island_info.no-island", "§c你目前沒有島嶼！"));
            return;
        }

        String content = buildContent(player, island);

        var builder = SimpleForm.builder()
                .title(plugin.getLocaleService().get(player, "island_info.title", "📊 島嶼資訊"))
                .content(content)
                .button(plugin.getLocaleService().get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

        builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

        api.sendForm(player.getUniqueId(), builder);
    }

    private String buildContent(Player player, Island island) {

        var locale = plugin.getLocaleService();
        StringBuilder sb = new StringBuilder();

        String islandName = island.getName();
        sb.append(locale.get(player, "island_info.name-label", "島嶼名稱："))
                .append(islandName == null || islandName.isBlank()
                        ? locale.get(player, "island_info.unnamed", "（未命名）")
                        : islandName)
                .append("\n\n");

        UUID ownerUuid = island.getOwner();
        String ownerName = ownerUuid == null
                ? locale.get(player, "island_info.no-owner", "（無島主）")
                : plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);
        sb.append(locale.get(player, "island_info.owner-label", "島主：")).append(ownerName).append("\n\n");

        String blocksSuffix = locale.get(player, "island_info.blocks-suffix", " 格");
        sb.append(locale.get(player, "island_info.protection-range-label", "保護範圍："))
                .append(island.getProtectionRange()).append(blocksSuffix).append("\n\n");
        sb.append(locale.get(player, "island_info.range-label", "島嶼範圍："))
                .append(island.getRange()).append(blocksSuffix).append("\n\n");

        long createdMillis = island.getCreatedDate();
        if (createdMillis > 0) {
            sb.append(locale.get(player, "island_info.created-label", "建立時間："))
                    .append(DATE_FORMAT.format(new Date(createdMillis))).append("\n\n");
        }

        Map<UUID, Integer> members = island.getMembers();
        int memberCount = members == null ? 0 : (int) members.values().stream()
                .filter(rank -> rank >= RanksManager.COOP_RANK)
                .count();

        sb.append(locale.get(player, "island_info.member-count-label", "成員人數："))
                .append(memberCount).append("\n");

        if (members != null) {
            for (Map.Entry<UUID, Integer> entry : members.entrySet()) {

                int rank = entry.getValue();

                // 訪客排名不算實際成員，跳過不顯示。
                if (rank < RanksManager.COOP_RANK) {
                    continue;
                }

                String name = plugin.getBentoBoxService().getPlayersManager().getName(entry.getKey());
                sb.append("  - ").append(name).append("（").append(closestRankLabel(player, rank)).append("）\n");
            }
        }

        return sb.toString();
    }

    /**
     * 找出離 rank 最近（不超過）的官方職級常數，交給 TeamMenuForm.rankLabel()
     * 統一翻譯——這裡的 rank 值不一定剛好等於六個官方常數之一
     * （BentoBox 允許自訂中間值），所以要找最接近的一個。
     */
    private String closestRankLabel(Player viewer, int rank) {
        int closest = RANK_VALUES[0];
        for (int value : RANK_VALUES) {
            if (rank >= value) {
                closest = value;
            }
        }
        return TeamMenuForm.rankLabel(plugin, viewer, closest);
    }
}
