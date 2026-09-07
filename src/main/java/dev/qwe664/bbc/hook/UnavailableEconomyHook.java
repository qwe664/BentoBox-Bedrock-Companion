package dev.qwe664.bbc.hook;

import org.bukkit.OfflinePlayer;

/** Vault 或經濟提供者不存在時使用，不需要載入任何 Vault API 類別。 */
public final class UnavailableEconomyHook implements EconomyHook {

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String getEconomyName() {
        return "未知";
    }

    @Override
    public double getPlayerBalance(OfflinePlayer player) {
        return 0.0;
    }

    @Override
    public EconomyTransactionResult withdrawPlayer(OfflinePlayer player, double amount) {
        return EconomyTransactionResult.failed("Economy unavailable");
    }

    @Override
    public EconomyTransactionResult depositPlayer(OfflinePlayer player, double amount) {
        return EconomyTransactionResult.failed("Economy unavailable");
    }

    @Override
    public boolean hasEnough(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }
}
