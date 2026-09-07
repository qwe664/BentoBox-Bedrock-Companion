package dev.qwe664.bbc.hook;

/** 不暴露 Vault 類別的 BBC 經濟交易結果。 */
public record EconomyTransactionResult(boolean success, String errorMessage) {

    public static EconomyTransactionResult succeeded() {
        return new EconomyTransactionResult(true, "");
    }

    public static EconomyTransactionResult failed(String errorMessage) {
        return new EconomyTransactionResult(false, errorMessage == null ? "" : errorMessage);
    }
}
