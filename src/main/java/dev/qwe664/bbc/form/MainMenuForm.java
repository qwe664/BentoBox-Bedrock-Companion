package dev.qwe664.bbc.form;
import dev.qwe664.bbc.menu.MenuButton;

import java.util.ArrayList;
import java.util.List;
import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

public class MainMenuForm extends BaseForm {

    public MainMenuForm(BentoBoxBedrockCompanion plugin) {
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
        List<MenuButton> visibleButtons = new ArrayList<>();
         builder
        .title("BentoBox")
        .content("歡迎使用 BentoBox Bedrock Companion");

for (MenuButton button : plugin.getMenuRegistry().getButtons()) {

    if (button.getPermission() == null
            || plugin.getPermissionService().hasPermission(player, button.getPermission())) {

        visibleButtons.add(button);
        builder.button(button.getTitle());
    }
}

        builder.validResultHandler(response -> {

            MenuButton clicked = visibleButtons.get(response.clickedButtonId());

             switch (clicked.getId()) {

    case "island" ->
            plugin.getFormManager().openIslandMenu(player);

    case "debug" -> {
    if (plugin.getPermissionService().hasDebugMenuPermission(player)) {
        plugin.getFormManager().openDebugMenu(player);
    }
}

    default -> {
    }
}

        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
