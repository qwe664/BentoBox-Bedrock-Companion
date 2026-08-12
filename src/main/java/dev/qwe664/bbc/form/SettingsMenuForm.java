package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;

/**
 * 島嶼設定表單。
 *
 * 使用 BentoBox 的 {@link Flag} 物件讀寫島嶼保護旗標，
 * 而不是用不存在的字串旗標名稱（原本的 "PVP" / "MONSTER_SPAWN" / "FIRE_SPREAD"
 * 字串寫法在 BentoBox 3.20.0 API 裡完全不存在，無法編譯）。
 */
public class SettingsMenuForm extends BaseForm {

    // 表單上顯示的旗標與對應的中文標籤，索引順序即為 toggle 在表單上出現的順序。
    private static final Flag[] TOGGLE_FLAGS = {
            Flags.PVP_OVERWORLD,
            Flags.PVP_NETHER,
            Flags.PVP_END,
            Flags.MONSTER_NATURAL_SPAWN,
            Flags.MONSTER_SPAWNERS_SPAWN,
            Flags.ANIMAL_NATURAL_SPAWN,
            Flags.ANIMAL_SPAWNERS_SPAWN,
            Flags.FIRE_SPREAD,
            Flags.FIRE_BURNING,
            Flags.FIRE_IGNITE,
            Flags.LEAF_DECAY,
            Flags.TNT_DAMAGE,
            Flags.BLOCK_EXPLODE_DAMAGE
    };

    private static final String[] TOGGLE_KEYS = {
            "toggle-pvp-overworld",
            "toggle-pvp-nether",
            "toggle-pvp-end",
            "toggle-monster-natural-spawn",
            "toggle-monster-spawners-spawn",
            "toggle-animal-natural-spawn",
            "toggle-animal-spawners-spawn",
            "toggle-fire-spread",
            "toggle-fire-burning",
            "toggle-fire-ignite",
            "toggle-leaf-decay",
            "toggle-tnt-damage",
            "toggle-block-explode-damage"
    };

    private static final String[] TOGGLE_FALLBACKS = {
            "允許 PvP（主世界）",
            "允許 PvP（地獄）",
            "允許 PvP（終界）",
            "允許怪物自然生成",
            "允許刷怪磚生成怪物",
            "允許動物自然生成",
            "允許刷怪磚生成動物",
            "允許火勢蔓延",
            "允許火焰燃燒方塊",
            "允許非玩家方式點火",
            "允許樹葉自然凋零",
            "允許 TNT 破壞方塊/傷害",
            "允許床/重生錨爆炸傷害方塊"
    };

    public SettingsMenuForm(BentoBoxBedrockCompanion plugin) {
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
            player.sendMessage(plugin.getLocaleService().get(player, "settings_menu.no-island", "§c你目前沒有島嶼！"));
            return;
        }

        var locale = plugin.getLocaleService();

        CustomForm.Builder builder = CustomForm.builder()
                .title(locale.get(player, "settings_menu.title", "島嶼設定管理"));

        for (int i = 0; i < TOGGLE_FLAGS.length; i++) {
            builder.toggle(
                    locale.get(player, "settings_menu." + TOGGLE_KEYS[i], TOGGLE_FALLBACKS[i]),
                    island.isAllowed(TOGGLE_FLAGS[i])
            );
        }

        builder.validResultHandler(response -> {

            for (int i = 0; i < TOGGLE_FLAGS.length; i++) {
                boolean value = response.asToggle(i);
                island.setSettingsFlag(TOGGLE_FLAGS[i], value);
            }

            player.sendMessage(locale.get(player, "settings_menu.update-success", "§a島嶼設定已成功更新！"));
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
