package dev.qwe664.bbc.util;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * 選單物品的建立與判斷工具。
 *
 * 用 PersistentDataContainer（PDC）在物品上打一個唯一標記，
 * 比用物品名稱、Lore 判斷更可靠——玩家改名、加附魔、換材質都不影響判斷結果，
 * 也不會跟伺服器上其他外觀相似的物品誤判混淆。
 *
 * 不依賴任何第三方物品插件（例如 ItemJoin），
 * 純用 Bukkit 原生 API 實作，不受第三方插件版本適配進度影響。
 */
public final class MenuItem {

    private static final Material MATERIAL = Material.COMPASS;
    private static final String DISPLAY_NAME = "\u00a76\u2726 \u4e3b\u9078\u55ae";

    private MenuItem() {
    }

    private static NamespacedKey key(BentoBoxBedrockCompanion plugin) {
        return new NamespacedKey(plugin, "menu_item");
    }

    /**
     * 建立一份新的選單物品。
     */
    public static ItemStack create(BentoBoxBedrockCompanion plugin) {

        ItemStack item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(DISPLAY_NAME);
        meta.getPersistentDataContainer().set(
                key(plugin),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }

    /**
     * 判斷一個物品是否為選單物品。
     */
    public static boolean isMenuItem(BentoBoxBedrockCompanion plugin, ItemStack item) {

        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(key(plugin), PersistentDataType.BYTE);
    }
}
