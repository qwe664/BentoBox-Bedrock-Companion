package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.menu.ProtectionCategory;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import dev.qwe664.bbc.service.SettingsAccess;
import world.bentobox.bentobox.api.events.flags.FlagProtectionChangeEvent;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;
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

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> open(player));
            return;
        }
        if (!player.isOnline()) return;
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

        if (!SettingsAccess.canOpen(player, island)) {
            SettingsAccess.deny(plugin, player);
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

        String labelHint = locale.get(player, "protection_category.label-hint",
                "排名由低到高：訪客 → 合作成員 → 信任成員 → 成員 → 副島主 → 島主。\n"
                        + "每個項目選擇「該功能最低要什麼身份才能使用」，選越低代表越多人能用。");
        CustomForm.Builder builder = CustomForm.builder()
                .title(categoryTitle)
                .label(labelHint);

        // 因為最上面多加了一個 label 元件，後面的 dropdown 在整張表單裡的
        // 元件索引都會往後移一位，所以要用這個位移量來對應 asDropdown() 的索引。
        int fieldOffset = 1;
        boolean[] editable = new boolean[flags.length];
        int[] original = new int[flags.length];
        boolean hasEditable = false;
        StringBuilder readOnlyContent = new StringBuilder(labelHint).append("\n\n");
        String readOnlyText = locale.get(player, "common.read-only", "唯讀");

        for (int i = 0; i < flags.length; i++) {

            int currentRank = island.getFlag(flags[i]);
            original[i] = currentRank;
            int selected = -1;
            for (int r = 0; r < RANK_VALUES.length; r++) {
                if (RANK_VALUES[r] == currentRank) selected = r;
            }
            editable[i] = selected >= 0 && SettingsAccess.canEdit(player, island, flags[i]);
            hasEditable |= editable[i];
            String currentRankName = rankName(rankNames, currentRank);

            // 標籤同時放名稱、簡短說明、目前排名，讓玩家不用猜這個旗標在做什麼。
            String labelWithCurrent = labels[i] + "\n" + descriptions[i]
                    + locale.get(player, "protection_category.current-suffix", "\n（目前：{rank}）")
                            .replace("{rank}", currentRankName);

            if (editable[i]) builder.dropdown(labelWithCurrent, selected, rankNames);
            else builder.label(labelWithCurrent + " [" + readOnlyText + "]");
            readOnlyContent.append(labelWithCurrent).append("\n\n");
        }

        if (!hasEditable) {
            var readOnlyForm = SimpleForm.builder()
                    .title(categoryTitle)
                    .content(readOnlyContent.toString())
                    .button(locale.get(player, "common.done", "✅ 完成"));
            api.sendForm(player.getUniqueId(), readOnlyForm);
            return;
        }

        builder.validResultHandler(response -> Bukkit.getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) return;
            Island current = plugin.getBentoBoxService().getIslandsManager()
                    .getIslandById(island.getUniqueId()).orElse(null);
            if (current == null || !SettingsAccess.canOpen(player, current)) {
                SettingsAccess.deny(plugin, player);
                return;
            }
            int[] desired = original.clone();
            for (int i = 0; i < flags.length; i++) {
                if (!editable[i]) continue;
                desired[i] = RANK_VALUES[response.asDropdown(i + fieldOffset)];
                if (desired[i] == original[i]) continue;
                if (!SettingsAccess.canEdit(player, current, flags[i])
                        || current.getFlag(flags[i]) != original[i]) {
                    SettingsAccess.deny(plugin, player);
                    return;
                }
            }
            // Validate every change before applying any of them.
            for (int i = 0; i < flags.length; i++) {
                if (desired[i] == original[i]) continue;
                current.setFlag(flags[i], desired[i]);

            }
            for (int i = 0; i < flags.length; i++) {
                if (desired[i] == original[i]) continue;
                Bukkit.getPluginManager().callEvent(new FlagProtectionChangeEvent(current, player.getUniqueId(), flags[i], current.getFlag(flags[i])));
                for (Flag child : flags[i].getSubflags()) {
                    Bukkit.getPluginManager().callEvent(new FlagProtectionChangeEvent(current, player.getUniqueId(), child, current.getFlag(child)));
                }
            }
            player.sendMessage(locale.get(player, "settings_menu.update-success", "§a島嶼設定已成功更新！"));
        }));

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
