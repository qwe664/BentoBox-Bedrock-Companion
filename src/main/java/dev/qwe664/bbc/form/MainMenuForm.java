package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.menu.MenuButton;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.List;

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

        String welcomeContent = "歡迎使用 BentoBox 基岩版專屬選單";

        if (plugin.getLuckPermsService().isEnabled()) {

            String group = plugin.getLuckPermsService().getPrimaryGroup(player);

            if (group != null) {
                welcomeContent += "\n§7身分組：§f" + group;
            }
        }

        String gameModeName = plugin.getBentoBoxService()
                .getCurrentGameModeName(player)
                .orElse("大廳");

        welcomeContent += "\n§7目前所在：§f" + gameModeName;

        builder
                .title("BentoBox")
                .content(welcomeContent);

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

                case "lobby" ->
                        // 用伺服器既有的 EssentialsX /spawn 指令回到大廳，
                        // 不用自己重新實作傳送邏輯。
                        plugin.getCommandService().execute(player, "spawn");

                case "admin" ->
                        plugin.getFormManager().openAdminMenu(player);

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
