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

        boolean warpsAvailable = plugin.getWarpsHook().isAvailable();
        boolean challengesAvailable = plugin.getChallengesHook().isAvailable();

        var builder = SimpleForm.builder();

        builder
                .title("🏝 我的島嶼")
                .content("請選擇功能")
                .button("🏠 傳送到島嶼")
                .button("👥 隊伍")
                .button("⚙ 島嶼設定")
                .button("🛡 保護設定")
                .button("📊 島嶼資訊");

        // Warps、Challenges 都是軟依賴，伺服器沒裝的話就不顯示對應按鈕，
        // 用一份「動態按鈕」清單記錄實際被加進去的按鈕順序，
        // 這樣不管兩者哪一個有裝、都有裝、都沒裝，編號永遠對得上。
        List<Runnable> dynamicActions = new ArrayList<>();

        if (warpsAvailable) {
            builder.button("🚩 傳送點");
            dynamicActions.add(() -> plugin.getFormManager().openWarpMenu(player));
        }

        if (challengesAvailable) {
            builder.button("🏆 挑戰");
            dynamicActions.add(() -> plugin.getFormManager().openChallengesMenu(player));
        }

        builder.button("⬅ 返回主選單");

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

                case 0 -> plugin.getCommandService().execute(player, "is");

                case 1 -> {
                    // BentoBox 本身有內建的隊伍管理指令（邀請/踢除/升降階），
                    // 這裡直接轉發過去，讓玩家跳到 BentoBox 原生的隊伍介面，
                    // 跟上面「傳送到島嶼」按鈕（case 0）用同一種做法，風格一致。
                    plugin.getCommandService().execute(player, "is team");
                }

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
