package dev.qwe664.bbc.hook;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.challenges.ChallengesAddon;
import world.bentobox.challenges.managers.ChallengesManager;

/**
 * Challenges 是軟依賴（softdepend），伺服器不一定有裝，
 * 所有呼叫前都要先檢查 isAvailable()，避免在沒裝的伺服器上崩潰。
 *
 * 跟 WarpsHook 同樣的教訓：Bukkit 層級的外掛名稱是 "BentoBox-Challenges"
 * （見其 plugin.yml），跟 BentoBox Addon 系統裡的名稱 "Challenges"
 * （見其 addon.yml）不一樣，所以一律只用 getAddonByName("Challenges") 判斷，
 * 不檢查 Bukkit.getPluginManager().getPlugin()。
 */
public class ChallengesHook {

    public boolean isAvailable() {
        return getChallengesAddon() != null;
    }

    /**
     * 取得 Challenges 的 Addon 實例（world.bentobox.challenges.ChallengesAddon）。
     * 找不到就回傳 null，呼叫端要自己配合 isAvailable() 做判斷。
     */
    public ChallengesAddon getChallengesAddon() {
        return BentoBox.getInstance()
                .getAddonsManager()
                .getAddonByName("Challenges")
                .filter(addon -> addon instanceof ChallengesAddon)
                .map(addon -> (ChallengesAddon) addon)
                .orElse(null);
    }

    /**
     * 取得 ChallengesManager，實際的挑戰資料都透過它讀取/操作。
     * Challenges 沒安裝或未啟用時回傳 null。
     */
    public ChallengesManager getChallengesManager() {
        ChallengesAddon addon = getChallengesAddon();
        return addon == null ? null : addon.getChallengesManager();
    }
}
