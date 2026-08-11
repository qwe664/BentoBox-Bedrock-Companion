package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.List;

public class IslandMenuForm extends BaseForm {

    private static final int FIXED_BUTTON_COUNT = 5;

    public IslandMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player player) {

        boolean warpsAvailable = plugin.getWarpsHook().isAvailable()
                && plugin.getConfigService().isFeatureEnabled("warps");
        boolean challengesAvailable = plugin.getChallengesHook().isAvailable()
                && plugin.getConfigService().isFeatureEnabled("challenges");
        boolean visitAvailable = plugin.getVisitHook().isAvailable()
                && plugin.getConfigService().isFeatureEnabled("visit");
        boolean walletBankAvailable = plugin.getVaultHook().isAvailable()
                && plugin.getBankHook().isAvailable()
                && plugin.getConfigService().isFeatureEnabled("wallet-bank");

        var builder = SimpleForm.builder();

        var locale = plugin.getLocaleService();

        builder
                .title(locale.get(player, "island_menu.title", "🏝 我的島嶼"))
                .content(locale.get(player, "island_menu.content", "請選擇功能"))
                .button(locale.get(player, "island_menu.go-home", "🏠 傳送到島嶼"))
                .button(locale.get(player, "island_menu.team", "👥 隊伍"))
                .button(locale.get(player, "island_menu.settings", "⚙ 島嶼設定"))
                .button(locale.get(player, "island_menu.protection", "🛡 保護設定"))
                .button(locale.get(player, "island_menu.info", "📊 島嶼資訊"));

        // Warps、Challenges 都是軟依賴，伺服器沒裝的話就不顯示對應按鈕，
        // 用一份「動態按鈕」清單記錄實際被加進去的按鈕順序，
        // 這樣不管兩者哪一個有裝、都有裝、都沒裝，編號永遠對得上。
        List<Runnable> dynamicActions = new ArrayList<>();

        if (warpsAvailable) {
            builder.button(locale.get(player, "island_menu.warps", "🚩 傳送點"));
            dynamicActions.add(() -> plugin.getFormManager().openWarpMenu(player));
        }

        if (challengesAvailable) {
            builder.button(locale.get(player, "island_menu.challenges", "🏆 挑戰"));
            dynamicActions.add(() -> plugin.getFormManager().openChallengesMenu(player));
        }

        if (visitAvailable) {
            builder.button(locale.get(player, "island_menu.visit", "🧭 拜訪島嶼"));
            dynamicActions.add(() -> plugin.getFormManager().openVisitBrowse(player));
        }

        if (walletBankAvailable) {
            builder.button(locale.get(player, "island_menu.wallet-bank", "💰 存提款"));
            dynamicActions.add(() -> plugin.getFormManager().openWalletBank(player));
        }

        builder.button(locale.get(player, "island_menu.back-to-main", "⬅ 返回主選單"));

        int backButtonId = FIXED_BUTTON_COUNT + dynamicActions.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openMainMenu(player);
                return;
            }

            if (clickedId >= FIXED_BUTTON_COUNT) {
                dynamicActions.get(clickedId - FIXED_BUTTON_COUNT).run();
                return;
            }

            switch (clickedId) {

                case 0 -> {
                    // 找得到「玩家目前所在世界」對應的玩法就直接傳送；
                    // 找不到（例如玩家站在主城，不屬於任何玩法的世界）
                    // 就跳出選單讓玩家自己選要去哪個玩法，不再靜默失敗。
                    plugin.getBentoBoxService().getPlayerCommandLabel(player)
                            .ifPresentOrElse(
                                    label -> plugin.getCommandService().execute(player, label),
                                    () -> new GameModePickerForm(plugin, "").open(player)
                            );
                }

                case 1 -> plugin.getFormManager().openTeamMenu(player);

                case 2 -> plugin.getFormManager().openSettingsMenu(player);

                case 3 -> plugin.getFormManager().openProtectionMenu(player);

                case 4 -> plugin.getFormManager().openIslandInfo(player);

                default -> {
                }
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }
}
