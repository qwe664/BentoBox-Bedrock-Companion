package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

public class DebugMenuForm extends BaseForm {

    public DebugMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player player) {

        var builder = SimpleForm.builder();

        builder
                .title("🛠 BBC Developer Tools")
                .content("開發工具")
                .button("🌍 環境資訊")
                .button("📦 插件資訊")
                .button("📚 BentoBox API")
                .button("🔍 Reflection")
                .button("⬅ 返回");

        builder.validResultHandler(response -> {

            switch (response.clickedButtonId()) {

                case 0, 1, 2, 3 -> {
                    player.sendMessage("§e🚧 此功能開發中...");
                }

                case 4 -> plugin.getFormManager().openMainMenu(player);

                default -> {
                }
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }
}
