package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bank.BankResponse;
import world.bentobox.bank.data.Money;
import world.bentobox.bank.data.TxType;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

/**
 * 玩家個人 Vault 錢包 ↔ 島嶼銀行 互轉。
 *
 * 需要 Vault（讀寫玩家錢包）跟 Bank 附加模組（讀寫島嶼銀行）同時安裝，
 * 少一個都不會顯示這顆按鈕（IslandMenuForm 那邊已經做過 isAvailable() 判斷）。
 *
 * 反編譯實際伺服器 Bank-1.10.1.jar 確認：
 * BankManager.deposit(User, Island, Money, TxType) / withdraw(...) 都是
 * 非同步方法，回傳 CompletableFuture&lt;BankResponse&gt;；BankResponse
 * 只有 SUCCESS、FAILURE_NO_ISLAND、FAILURE_LOW_BALANCE、FAILURE_LOAD_ERROR
 * 四種結果，不會拋例外，非猜測。
 *
 * 存款（錢包→銀行）：先從 Vault 錢包扣款，扣款成功才呼叫 BankManager.deposit()
 * 存進島嶼銀行；如果 deposit() 之後回傳失敗（例如剛好島嶼被刪除），
 * 會把剛剛扣掉的錢用 Vault depositPlayer() 退回玩家錢包，不讓玩家平白損失。
 *
 * 提款（銀行→錢包）：先呼叫 BankManager.withdraw() 從島嶼銀行扣款，
 * 銀行端扣款成功才用 Vault depositPlayer() 存進玩家個人錢包；
 * 銀行端本來就會自己判斷餘額夠不夠（FAILURE_LOW_BALANCE），不用另外檢查。
 *
 * BankManager 的方法是非同步（CompletableFuture），完成時的 callback
 * 不保證在主執行緒，所有回頭要跟玩家互動（sendMessage）的地方
 * 都用 Bukkit.getScheduler().runTask() 排回主執行緒再執行。
 */
public class WalletBankForm extends BaseForm {

    public WalletBankForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player player) {

        FloodgateApi api = FloodgateApi.getInstance();

        if (api == null) {
            player.sendMessage(plugin.getLocaleService().get(player, "common.floodgate-not-ready", "§cFloodgate API 尚未初始化。"));
            return;
        }

        if (!plugin.getVaultHook().isAvailable()) {
            player.sendMessage(plugin.getLocaleService().get(player, "wallet_bank.no-economy", "§c目前沒有經濟外掛掛在 Vault 底下，無法使用存提款功能。"));
            return;
        }

        if (!plugin.getBankHook().isAvailable()) {
            player.sendMessage(plugin.getLocaleService().get(player, "wallet_bank.no-bank-addon", "§c伺服器未安裝 Bank 附加模組，無法使用存提款功能。"));
            return;
        }

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        if (island == null) {
            player.sendMessage(plugin.getLocaleService().get(player, "wallet_bank.no-island", "§c你目前沒有島嶼，無法使用存提款功能。"));
            return;
        }

        double walletBalance = plugin.getVaultHook().getPlayerBalance(player);
        double bankBalance = plugin.getBankHook().getIslandBalance(island);

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title(locale.get(player, "wallet_bank.title", "💰 存提款"))
                .content(locale.get(player, "wallet_bank.wallet-balance-label", "錢包餘額：") + plugin.getVaultHook().format(walletBalance)
                        + "\n" + locale.get(player, "wallet_bank.bank-balance-label", "島嶼銀行餘額：") + String.format("%.2f", bankBalance))
                .button(locale.get(player, "wallet_bank.deposit-button", "⬆ 存款（錢包 → 島嶼銀行）"))
                .button(locale.get(player, "wallet_bank.withdraw-button", "⬇ 提款（島嶼銀行 → 錢包）"))
                .button(locale.get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

        builder.validResultHandler(response -> {
            switch (response.clickedButtonId()) {
                case 0 -> openAmountInput(player, island, true);
                case 1 -> openAmountInput(player, island, false);
                default -> plugin.getFormManager().openIslandMenu(player);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    /**
     * @param deposit true = 錢包存進銀行，false = 銀行提到錢包
     */
    private void openAmountInput(Player player, Island island, boolean deposit) {

        FloodgateApi api = FloodgateApi.getInstance();
        var locale = plugin.getLocaleService();

        String title = deposit
                ? locale.get(player, "wallet_bank.deposit-title", "⬆ 存款")
                : locale.get(player, "wallet_bank.withdraw-title", "⬇ 提款");

        String verb = deposit
                ? locale.get(player, "wallet_bank.deposit-verb", "存入")
                : locale.get(player, "wallet_bank.withdraw-verb", "提出");

        CustomForm.Builder builder = CustomForm.builder()
                .title(title)
                .input(locale.get(player, "wallet_bank.amount-label", "金額"),
                        locale.get(player, "wallet_bank.amount-placeholder", "輸入要{verb}的金額").replace("{verb}", verb));

        builder.validResultHandler(response -> {

            String rawAmount = response.asInput(0);
            double amount;

            try {
                amount = Double.parseDouble(rawAmount == null ? "" : rawAmount.trim());
            } catch (NumberFormatException e) {
                player.sendMessage(locale.get(player, "wallet_bank.invalid-amount", "§c請輸入有效的數字金額。"));
                return;
            }

            if (amount <= 0) {
                player.sendMessage(locale.get(player, "wallet_bank.amount-must-be-positive", "§c金額必須大於 0。"));
                return;
            }

            if (deposit) {
                depositToBank(player, island, amount);
            } else {
                withdrawFromBank(player, island, amount);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    private void depositToBank(Player player, Island island, double amount) {

        var locale = plugin.getLocaleService();

        if (!plugin.getVaultHook().hasEnough(player, amount)) {
            player.sendMessage(locale.get(player, "wallet_bank.insufficient-wallet", "§c你的錢包餘額不足。"));
            return;
        }

        var withdrawResponse = plugin.getVaultHook().withdrawPlayer(player, amount);

        if (withdrawResponse == null || !withdrawResponse.transactionSuccess()) {
            player.sendMessage(locale.get(player, "wallet_bank.wallet-withdraw-failed", "§c從錢包扣款失敗：")
                    + (withdrawResponse == null ? locale.get(player, "wallet_bank.unknown-error", "未知錯誤") : withdrawResponse.errorMessage));
            return;
        }

        User user = User.getInstance(player);
        Money amountMoney = new Money(amount);

        plugin.getBankHook().getBankAddon().getBankManager()
                .deposit(user, island, amountMoney, TxType.DEPOSIT)
                .thenAccept(bankResponse -> Bukkit.getScheduler().runTask(plugin, () -> {

                    if (bankResponse == BankResponse.SUCCESS) {
                        player.sendMessage(locale.get(player, "wallet_bank.deposit-success", "§a已將 {amount} 存入島嶼銀行。")
                                .replace("{amount}", plugin.getVaultHook().format(amount)));
                    } else {
                        // 銀行端存款失敗，把剛剛扣掉的錢退回玩家錢包，不讓玩家平白損失。
                        plugin.getVaultHook().depositPlayer(player, amount);
                        player.sendMessage(locale.get(player, "wallet_bank.deposit-failed", "§c存款失敗（{reason}），金額已退回錢包。")
                                .replace("{reason}", bankResponse.toString()));
                    }
                }));
    }

    private void withdrawFromBank(Player player, Island island, double amount) {

        var locale = plugin.getLocaleService();
        double bankBalance = plugin.getBankHook().getIslandBalance(island);

        if (bankBalance < amount) {
            player.sendMessage(locale.get(player, "wallet_bank.insufficient-bank", "§c島嶼銀行餘額不足。"));
            return;
        }

        User user = User.getInstance(player);
        Money amountMoney = new Money(amount);

        plugin.getBankHook().getBankAddon().getBankManager()
                .withdraw(user, island, amountMoney, TxType.WITHDRAW)
                .thenAccept(bankResponse -> Bukkit.getScheduler().runTask(plugin, () -> {

                    if (bankResponse == BankResponse.SUCCESS) {
                        plugin.getVaultHook().depositPlayer(player, amount);
                        player.sendMessage(locale.get(player, "wallet_bank.withdraw-success", "§a已從島嶼銀行提出 {amount} 到錢包。")
                                .replace("{amount}", plugin.getVaultHook().format(amount)));
                    } else {
                        player.sendMessage(locale.get(player, "wallet_bank.withdraw-failed", "§c提款失敗（{reason}）。")
                                .replace("{reason}", bankResponse.toString()));
                    }
                }));
    }
}
