package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

public class IslandMenuForm extends BaseForm {

    public IslandMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player player) {

        boolean warpsAvailable = plugin.getWarpsHook().isAvailable();

        var builder = SimpleForm.builder();

        builder
                .title("🏝 我的島嶼")
                .content("請選擇功能")
                .button("🏠 傳送到島嶼")
                .button("👥 隊伍")
                .button("⚙ 島嶼設定")
                .button("🛡 保護設定")
                .button("📊 島嶼資訊");

        // Warps 是軟依賴，伺服器沒裝的話就不顯示這顆按鈕，
        // 後面返回按鈕的編號要跟著往前遞補。
        if (warpsAvailable) {
            builder.button("🚩 傳送點");
        }

        builder.button("⬅ 返回主選單");

        int backButtonId = warpsAvailable ? 6 : 5;

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openMainMenu(player);
                return;
            }

            switch (clickedId) {

                case 0 -> {
                plugin.getCommandService().execute(player, "is");
                 }

                case 1 -> {
                    // BentoBox 本身有內建的隊伍管理指令（邀請/踢除/升降階），
                    // 這裡直接轉發過去，讓玩家跳到 BentoBox 原生的隊伍介面，
                    // 跟上面「傳送到島嶼」按鈕（case 0）用同一種做法，風格一致。
                    plugin.getCommandService().execute(player, "is team");
                }

                case 2 -> plugin.getFormManager().openSettingsMenu(player);

                case 3 -> plugin.getFormManager().openProtectionMenu(player);

                case 4 -> plugin.getFormManager().openIslandInfo(player);

                case 5 -> {
                    if (warpsAvailable) {
                        plugin.getFormManager().openWarpMenu(player);
                    }
                }

                default -> {
                }
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }
}
