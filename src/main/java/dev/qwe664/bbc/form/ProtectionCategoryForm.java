package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.menu.ProtectionCategory;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;

/**
 * 顯示單一分類底下所有 Flag.Type.PROTECTION 旗標的表單。
 *
 * 每個旗標對應一個下拉選單，讓玩家選擇「訪客 / 合作成員 / 信任成員 / 成員 / 副島主 / 島主」
 * 這幾個排名中，哪個排名（含）以上的成員可以使用該功能。
 *
 * 這一批旗標跟 SettingsMenuForm 用的 Flag.Type.SETTING 不一樣：
 * 讀寫是用 island.getFlag(flag) / island.setFlag(flag, int rank)，而不是布林值。
 */
public class ProtectionCategoryForm {

    // 排名由低到高排列，索引順序即為下拉選單選項順序。
    private static final int[] RANK_VALUES = {
            RanksManager.VISITOR_RANK,
            RanksManager.COOP_RANK,
            RanksManager.TRUSTED_RANK,
            RanksManager.MEMBER_RANK,
            RanksManager.SUB_OWNER_RANK,
            RanksManager.OWNER_RANK
    };

    private static final String[] RANK_KEYS = {
            "rank-visitor",
            "rank-coop",
            "rank-trusted",
            "rank-member",
            "rank-sub-owner",
            "rank-owner"
    };

    private static final String[] RANK_FALLBACKS = {
            "訪客",
            "合作成員",
            "信任成員",
            "成員",
            "副島主",
            "島主"
    };

    private final BentoBoxBedrockCompanion plugin;
    private final ProtectionCategory category;

    public ProtectionCategoryForm(BentoBoxBedrockCompanion plugin, ProtectionCategory category) {
        this.plugin = plugin;
        this.category = category;
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

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        if (island == null) {
            player.sendMessage(plugin.getLocaleService().get(player, "protection_category.no-island", "§c你目前沒有島嶼！"));
            return;
        }

        var locale = plugin.getLocaleService();

        String[] rankNames = new String[RANK_KEYS.length];
        for (int i = 0; i < RANK_KEYS.length; i++) {
            rankNames[i] = locale.get(player, "protection_category." + RANK_KEYS[i], RANK_FALLBACKS[i]);
        }

        Flag[] flags = category.flags();
        String[] labels = new String[flags.length];
        String[] descriptions = new String[flags.length];
        for (int i = 0; i < flags.length; i++) {
            String flagId = flags[i].getID();
            labels[i] = locale.get(player, "protection_flags." + flagId + ".label", category.labels()[i]);
            descriptions[i] = locale.get(player, "protection_flags." + flagId + ".description", category.descriptions()[i]);
        }

        String categoryTitle = locale.get(player, "protection_categories." + category.id(), category.title());

        CustomForm.Builder builder = CustomForm.builder()
                .title(categoryTitle)
                .label(locale.get(player, "protection_category.label-hint",
                        "排名由低到高：訪客 → 合作成員 → 信任成員 → 成員 → 副島主 → 島主。\n"
                                + "每個項目選擇「該功能最低要什麼身份才能使用」，選越低代表越多人能用。"));

        // 因為最上面多加了一個 label 元件，後面的 dropdown 在整張表單裡的
        // 元件索引都會往後移一位，所以要用這個位移量來對應 asDropdown() 的索引。
        int fieldOffset = 1;

        for (int i = 0; i < flags.length; i++) {

            int currentRank = island.getFlag(flags[i]);
            String currentRankName = rankName(rankNames, currentRank);

            // 標籤同時放名稱、簡短說明、目前排名，讓玩家不用猜這個旗標在做什麼。
            String labelWithCurrent = labels[i] + "\n" + descriptions[i]
                    + locale.get(player, "protection_category.current-suffix", "\n（目前：{rank}）")
                            .replace("{rank}", currentRankName);

            builder.dropdown(labelWithCurrent, rankNames);
        }

        builder.validResultHandler(response -> {

            for (int i = 0; i < flags.length; i++) {
                int selectedIndex = response.asDropdown(i + fieldOffset);
                island.setFlag(flags[i], RANK_VALUES[selectedIndex]);
            }

            player.sendMessage(locale.get(player, "protection_category.update-success", "§a「{category}」保護設定已成功更新！")
                    .replace("{category}", categoryTitle));
            plugin.getFormManager().openProtectionMenu(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    private String rankName(String[] rankNames, int rank) {
        for (int i = 0; i < RANK_VALUES.length; i++) {
            if (RANK_VALUES[i] == rank) {
                return rankNames[i];
            }
        }
        // 找不到完全對應的排名時（例如自訂排名），取最接近且不超過的排名顯示。
        String closest = rankNames[0];
        for (int i = 0; i < RANK_VALUES.length; i++) {
            if (rank >= RANK_VALUES[i]) {
                closest = rankNames[i];
            }
        }
        return closest;
    }
}
