package dev.qwe664.bbc.hook;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

/**
 * Vault 是軟依賴（softdepend），伺服器不一定有裝，也不一定有掛任何一個
 * 實際的經濟外掛（EssentialsX Economy 等）在底下——Vault 本身只是接口，
 * 沒有掛任何經濟外掛時 getServicesManager().getRegistration(Economy.class)
 * 會回傳 null，所有呼叫前都要先檢查 isAvailable()。
 *
 * 反編譯實際伺服器 Vault.jar 確認 Economy 介面（net.milkbowl.vault.economy.
 * Economy）方法簽章，這是 Vault 標準公開 API，取得方式是透過 Bukkit
 * ServicesManager 拿 RegisteredServiceProvider（Vault 官方文件記載的
 * 標準取用方式，非猜測）：
 *
 *   RegisteredServiceProvider&lt;Economy&gt; rsp =
 *       Bukkit.getServicesManager().getRegistration(Economy.class);
 *
 * 這裡讀的是「玩家個人 Vault 錢包餘額」，跟 BankHook 讀的「島嶼銀行餘額」
 * 是完全不同的兩筆錢——不要混用。
 */
public class VaultHook {

    private Economy economy;

    /**
     * 在 onEnable() 時呼叫一次，嘗試向 ServicesManager 註冊經濟服務。
     * 沒有任何經濟外掛掛在 Vault 底下時，economy 會維持 null。
     */
    public void setup(ServicesManager servicesManager) {
        RegisteredServiceProvider<Economy> rsp =
                servicesManager.getRegistration(Economy.class);
        this.economy = (rsp == null) ? null : rsp.getProvider();
    }

    public boolean isAvailable() {
        return economy != null && economy.isEnabled();
    }

    /**
     * 取得目前掛在 Vault 底下的經濟外掛名稱（例如 "Essentials Economy"），
     * 純粹給開機 log 顯示用。未安裝時回傳 "未知"。
     */
    public String getEconomyName() {
        return isAvailable() ? economy.getName() : "未知";
    }

    /**
     * 取得玩家個人 Vault 錢包餘額。Vault 未安裝或沒有經濟外掛時回傳 0.0，
     * 不回傳 null，讓呼叫端（尤其是 PlaceholderAPI 表達式）不用額外判空。
     */
    public double getPlayerBalance(OfflinePlayer player) {
        return isAvailable() ? economy.getBalance(player) : 0.0;
    }

    /**
     * 從玩家個人錢包扣款。回傳 Vault 原生的 EconomyResponse，
     * 呼叫端用 transactionSuccess() 判斷是否成功、errorMessage 顯示失敗原因。
     * Vault 未安裝時回傳 null，呼叫端要自己配合 isAvailable() 判斷。
     */
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return isAvailable() ? economy.withdrawPlayer(player, amount) : null;
    }

    /**
     * 存款進玩家個人錢包。用於「把島嶼銀行的錢提到個人錢包」這個方向。
     */
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return isAvailable() ? economy.depositPlayer(player, amount) : null;
    }

    public boolean hasEnough(OfflinePlayer player, double amount) {
        return isAvailable() && economy.has(player, amount);
    }

    public String format(double amount) {
        return isAvailable() ? economy.format(amount) : String.format("%.2f", amount);
    }
}
