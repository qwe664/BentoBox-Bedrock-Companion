package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

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

        player.sendMessage("§a偵測到基岩版玩家，準備開啟 Bedrock Form...");
    }
}
