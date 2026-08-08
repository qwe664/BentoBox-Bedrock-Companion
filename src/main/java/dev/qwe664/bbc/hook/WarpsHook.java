package dev.qwe664.bbc.hook;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.warps.Warp;
import world.bentobox.warps.managers.WarpSignsManager;

/**
 * Warps 是軟依賴（softdepend），伺服器不一定有裝，
 * 所有呼叫前都要先檢查 isAvailable()，避免在沒裝的伺服器上崩潰。
 */
public class WarpsHook {

    /**
     * 判斷 Warps 附加模組是否已安裝且啟用。
     *
     * 注意：Bukkit 層級的外掛名稱是 "BentoBox-Warps"（見其 plugin.yml），
     * 跟 BentoBox Addon 系統裡的名稱 "Warps"（見其 addon.yml）不一樣，
     * 所以這裡不檢查 Bukkit.getPluginManager().getPlugin()，
     * 只靠 getWarpAddon() 去查 BentoBox 的 Addon 清單就夠了。
     */
    public boolean isAvailable() {
        return getWarpAddon() != null;
    }

    /**
     * 取得 Warps 的 Addon 實例（world.bentobox.warps.Warp）。
     * 找不到就回傳 null，呼叫端要自己配合 isAvailable() 做判斷。
     */
    public Warp getWarpAddon() {
        return BentoBox.getInstance()
                .getAddonsManager()
                .getAddonByName("Warps")
                .filter(addon -> addon instanceof Warp)
                .map(addon -> (Warp) addon)
                .orElse(null);
    }

    /**
     * 取得 WarpSignsManager，實際的 Warp 資料都透過它讀取/操作。
     * Warps 沒安裝或未啟用時回傳 null。
     */
    public WarpSignsManager getWarpSignsManager() {
        Warp addon = getWarpAddon();
        return addon == null ? null : addon.getWarpSignsManager();
    }
}
