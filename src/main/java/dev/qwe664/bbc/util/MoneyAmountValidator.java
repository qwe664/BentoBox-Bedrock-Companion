package dev.qwe664.bbc.util;

/** 驗證可安全交給 Vault 與 BentoBox Bank 的金額。 */
public final class MoneyAmountValidator {

    private MoneyAmountValidator() {
    }

    public static boolean isFinite(double amount) {
        return Double.isFinite(amount);
    }

    public static boolean isPositive(double amount) {
        return amount > 0.0;
    }
}
