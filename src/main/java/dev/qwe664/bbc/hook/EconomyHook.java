package dev.qwe664.bbc.hook;

import org.bukkit.OfflinePlayer;

/** BBC 使用的經濟介面，刻意不在方法簽章中引用任何 Vault 類別。 */
public interface EconomyHook {

    boolean isAvailable();

    String getEconomyName();

    double getPlayerBalance(OfflinePlayer player);

    EconomyTransactionResult withdrawPlayer(OfflinePlayer player, double amount);

    EconomyTransactionResult depositPlayer(OfflinePlayer player, double amount);

    boolean hasEnough(OfflinePlayer player, double amount);

    String format(double amount);
}
