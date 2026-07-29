package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.cumulus.form.SimpleForm;

public class MainMenuForm extends BaseForm {

    public MainMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

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

        SimpleForm.Builder builder = SimpleForm.builder();
        builder
             .title("BentoBox")
             .content("歡迎使用 BentoBox Bedrock Companion");
              button("🏝 我的島嶼");
        player.sendMessage("§aSimpleForm Builder 建立成功！");
    }
}
