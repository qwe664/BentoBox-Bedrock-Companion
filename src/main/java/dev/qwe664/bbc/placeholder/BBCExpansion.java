package dev.qwe664.bbc.placeholder;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.form.TeamMenuForm;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.database.objects.Island;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BentoBox Bedrock Companion 的 PlaceholderAPI 變數擴充。
 *
 * 所有變數都在 %bbc_*% 這個命名空間底下，例如 %bbc_island_name%。
 * PlaceholderAPI 是軟依賴（softdepend），這個類別只有在
 * BentoBoxBedrockCompanion.onEnable() 確認偵測到 PlaceholderAPI 已啟用時
 * 才會被 new 出來並呼叫 register()，未安裝 PlaceholderAPI 的伺服器
 * 完全不會用到這個類別，不影響其他功能。
 *
 * 提供的變數：
 *   %bbc_luckperms_group%  玩家目前的 LuckPerms 主要權限組名稱
 *   %bbc_player_name%      玩家名稱
 *   %bbc_server_time%      伺服器目前時間（HH:mm:ss）
 *   %bbc_gamemode%         玩家目前所在的玩法名稱，不在任何玩法世界（大廳/主城）時顯示「大廳」
 *   %bbc_island_name%      玩家目前所在世界裡，自己島嶼的自訂名稱
 *   %bbc_island_rank%      玩家在自己島嶼裡的職級（隊長/副隊長/成員...）
 *   %bbc_island_money%     玩家目前所在世界裡，自己島嶼的銀行餘額（需要 Bank 附加模組，未安裝固定回傳 0）
 *
 * 「島嶼」相關的四個變數都是以玩家「目前所在世界」對應的玩法為準
 * （呼叫 IslandsManager.getIsland(player.getWorld(), uuid)），
 * 玩家站在不屬於任何玩法的中立世界（大廳）時，這些變數一律回傳「無」。
 *
 * island_rank 的顯示文字直接複用 TeamMenuForm.rankLabel()，該方法
 * 改成呼叫 BentoBox 官方的 RanksManager + User.getTranslation()
 * 從 BentoBox 自己的語言檔（ranks: 區塊）動態查出翻譯，不再自己
 * 寫死一份中文對照表；查不到才 fallback 回寫死的中文。
 */
public class BBCExpansion extends PlaceholderExpansion {

    private final BentoBoxBedrockCompanion plugin;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public BBCExpansion(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "bbc";
    }

    @Override
    public String getAuthor() {
        return "qwe664";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * 伺服器重載 PlaceholderAPI（/papi reload）時不用重新註冊一次，
     * 這個擴充會持續留著。
     */
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if (player == null) {
            return "";
        }

        return switch (params.toLowerCase()) {

            case "luckperms_group" -> {
                String group = plugin.getLuckPermsService().getPrimaryGroup(player);
                yield group == null ? "無" : group;
            }

            case "player_name" -> player.getName();

            case "server_time" -> timeFormat.format(new Date());

            case "gamemode" -> plugin.getBentoBoxService()
                    .getCurrentGameModeName(player)
                    .orElse(plugin.getConfigService().getMessage("lobby-name", "大廳"));

            case "island_name" -> {
                Island island = getIsland(player);
                if (island == null) {
                    yield "無";
                }
                String name = island.getName();
                yield (name == null || name.isEmpty()) ? "無" : name;
            }

            case "island_rank" -> {
                Island island = getIsland(player);
                yield island == null
                        ? "無"
                        : TeamMenuForm.rankLabel(plugin, player, island.getRank(player.getUniqueId()));
            }

            case "island_money" -> {
                Island island = getIsland(player);
                double balance = plugin.getBankHook().getIslandBalance(island);
                yield String.format("%.2f", balance);
            }

            default -> null;
        };
    }

    private Island getIsland(Player player) {
        return plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());
    }
}
