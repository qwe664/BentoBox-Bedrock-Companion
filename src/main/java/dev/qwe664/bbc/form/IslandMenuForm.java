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

        var builder = SimpleForm.builder();

        builder
                .title("🏝 我的島嶼")
                .content("請選擇功能")
                .button("🏠 傳送到島嶼")
                .button("👥 隊伍")
                .button("⚙ 島嶼設定")
                .button("📊 島嶼資訊")
                .button("⬅ 返回主選單");

        builder.validResultHandler(response -> {

            switch (response.clickedButtonId()) {

                case 0 -> {
                plugin.getCommandService().execute(player, "is");
                 }

                case 1 -> {
                    // TODO 隊伍
                }

                case 2 -> {
                plugin.getCommandService().executeLater(player, "is settings", 20);
                 }

                case 3 -> {
                    // TODO 島嶼資訊
                }

                case 4 -> plugin.getFormManager().openMainMenu(player);

                default -> {
                }
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }
}
