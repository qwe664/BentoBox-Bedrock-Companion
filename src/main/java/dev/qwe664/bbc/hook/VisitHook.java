package dev.qwe664.bbc.hook;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.visit.VisitAddon;
import world.bentobox.visit.managers.VisitAddonManager;

/**
 * Visit 是軟依賴（softdepend），伺服器不一定有裝，
 * 所有呼叫前都要先檢查 isAvailable()，避免在沒裝的伺服器上崩潰。
 *
 * 跟 Warps、Challenges、Bank 一樣的命名坑：Bukkit 層級的外掛名稱是
 * "BentoBox-Visit"（見其 plugin.yml），跟 BentoBox Addon 系統裡的名稱
 * "Visit"（見其 addon.yml）不一樣，所以一律用 getAddonByName("Visit")
 * 判斷，不檢查 Bukkit.getPluginManager().getPlugin()。
 * 反編譯實際伺服器 Visit-1.7.0.jar 確認過，非猜測。
 */
public class VisitHook {

    /**
     * 判斷 Visit 附加模組是否已安裝且啟用。
     */
    public boolean isAvailable() {
        return getVisitAddon() != null;
    }

    /**
     * 取得 Visit 的 Addon 實例（world.bentobox.visit.VisitAddon）。
     * 找不到就回傳 null，呼叫端要自己配合 isAvailable() 做判斷。
     */
    public VisitAddon getVisitAddon() {
        return BentoBox.getInstance()
                .getAddonsManager()
                .getAddonByName("Visit")
                .filter(addon -> addon instanceof VisitAddon)
                .map(addon -> (VisitAddon) addon)
                .orElse(null);
    }

    /**
     * 取得 VisitAddonManager，讀取「能不能拜訪這座島」用得到
     * （preprocessTeleportation、canVisitOffline 等唯讀查詢方法）。
     * Visit 沒安裝或未啟用時回傳 null。
     */
    public VisitAddonManager getVisitAddonManager() {
        VisitAddon addon = getVisitAddon();
        return addon == null ? null : addon.getAddonManager();
    }
}
