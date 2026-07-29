package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

public class AdminMenuForm extends BaseForm {

    public AdminMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player player) {

        FloodgateApi api = FloodgateApi.getInstance();

        if (api == null) {
            player.sendMessage("§cFloodgate API 尚未初始化。");
            return;
        }

        if (!api.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage("§e目前只有基岩版玩家可以使用 Bedrock UI。");
            return;
        }

        var builder = SimpleForm.builder();

        builder
                .title("👮 管理工具")
                .content("🚧 功能開發中")
                .button("⬅ 返回主選單");

        builder.validResultHandler(response -> {
            if (response.clickedButtonId() == 0) {
                plugin.getFormManager().openMainMenu(player);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
