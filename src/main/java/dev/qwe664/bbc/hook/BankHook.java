package dev.qwe664.bbc.hook;

import world.bentobox.bank.Bank;
import world.bentobox.bank.BankManager;
import world.bentobox.bank.data.Money;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

/**
 * Bank 是軟依賴（softdepend），伺服器不一定有裝，所有呼叫前都要先檢查
 * isAvailable()，避免在沒裝的伺服器上崩潰。
 *
 * 跟 WarpsHook、ChallengesHook 同樣的教訓：Bukkit 層級的外掛名稱是
 * "BentoBox-Bank"（見其 plugin.yml），跟 BentoBox Addon 系統裡的名稱
 * "Bank"（見其 addon.yml）不一樣，一律只用 getAddonByName("Bank") 判斷，
 * 不檢查 Bukkit.getPluginManager().getPlugin()。
 *
 * Bank 附加模組其實自己內建了一套 PlaceholderAPI 整合（PhManager），
 * 但那是掛在 Bank 自己的識別碼底下（各玩法各自的排行榜變數），
 * 這裡另外直接呼叫 BankManager 讀取，是為了把餘額整合進 BBC 自己統一的
 * %bbc_*% 命名空間，不需要玩家額外記 Bank 的變數名稱。
 */
public class BankHook {

    public boolean isAvailable() {
        return getBankAddon() != null;
    }

    /**
     * 取得 Bank 的 Addon 實例（world.bentobox.bank.Bank）。
     * 找不到就回傳 null，呼叫端要自己配合 isAvailable() 做判斷。
     */
    public Bank getBankAddon() {
        return BentoBox.getInstance()
                .getAddonsManager()
                .getAddonByName("Bank")
                .filter(addon -> addon instanceof Bank)
                .map(addon -> (Bank) addon)
                .orElse(null);
    }

    /**
     * 取得指定島嶼的銀行餘額。Bank 未安裝、島嶼為 null、或該島嶼查無帳戶資料
     * （例如剛建立的新島嶼還沒有任何存款紀錄）時回傳 0.0，不回傳 null，
     * 讓呼叫端（尤其是 PlaceholderAPI 表達式）不用額外判空。
     */
    public double getIslandBalance(Island island) {

        Bank addon = getBankAddon();

        if (addon == null || island == null) {
            return 0.0;
        }

        BankManager bankManager = addon.getBankManager();
        Money balance = bankManager.getBalance(island);

        return balance == null ? 0.0 : balance.getValue();
    }
}
