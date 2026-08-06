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

    private static final String[] RANK_NAMES = {
            "訪客",
            "合作成員",
            "信任成員",
            "成員",
            "副島主",
            "島主"
    };

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public IslandInfoForm(BentoBoxBedrockCompanion plugin) {
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
            player.sendMessage("§c你目前沒有島嶼！");
            return;
        }

        String content = buildContent(island);

        var builder = SimpleForm.builder()
                .title("📊 島嶼資訊")
                .content(content)
                .button("⬅ 返回島嶼選單");

        builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

        api.sendForm(player.getUniqueId(), builder);
    }

    private String buildContent(Island island) {

        StringBuilder sb = new StringBuilder();

        String islandName = island.getName();
        sb.append("島嶼名稱：")
                .append(islandName == null || islandName.isBlank() ? "（未命名）" : islandName)
                .append("\n\n");

        UUID ownerUuid = island.getOwner();
        String ownerName = ownerUuid == null
                ? "（無島主）"
                : plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);
        sb.append("島主：").append(ownerName).append("\n\n");

        sb.append("保護範圍：").append(island.getProtectionRange()).append(" 格\n\n");
        sb.append("島嶼範圍：").append(island.getRange()).append(" 格\n\n");

        long createdMillis = island.getCreatedDate();
        if (createdMillis > 0) {
            sb.append("建立時間：").append(DATE_FORMAT.format(new Date(createdMillis))).append("\n\n");
        }

        Map<UUID, Integer> members = island.getMembers();
        int memberCount = members == null ? 0 : (int) members.values().stream()
                .filter(rank -> rank >= RanksManager.COOP_RANK)
                .count();

        sb.append("成員人數：").append(memberCount).append("\n");

        if (members != null) {
            for (Map.Entry<UUID, Integer> entry : members.entrySet()) {

                int rank = entry.getValue();

                // 訪客排名不算實際成員，跳過不顯示。
                if (rank < RanksManager.COOP_RANK) {
                    continue;
                }

                String name = plugin.getBentoBoxService().getPlayersManager().getName(entry.getKey());
                sb.append("  - ").append(name).append("（").append(rankName(rank)).append("）\n");
            }
        }

        return sb.toString();
    }

    private String rankName(int rank) {
        for (int i = 0; i < RANK_VALUES.length; i++) {
            if (RANK_VALUES[i] == rank) {
                return RANK_NAMES[i];
            }
        }
        String closest = RANK_NAMES[0];
        for (int i = 0; i < RANK_VALUES.length; i++) {
            if (rank >= RANK_VALUES[i]) {
                closest = RANK_NAMES[i];
            }
        }
        return closest;
    }
}
