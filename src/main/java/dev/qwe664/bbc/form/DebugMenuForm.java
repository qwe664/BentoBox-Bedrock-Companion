package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.developer.reflection.ReflectionAliases;
import dev.qwe664.bbc.util.ReflectionUtil;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import dev.qwe664.bbc.debug.EnvironmentPrinter;
import dev.qwe664.bbc.command.DebugCommand;

public class DebugMenuForm extends BaseForm {

    private final DebugCommand debugCommand;

    public DebugMenuForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
        this.debugCommand = new DebugCommand(plugin);
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

                case 0 -> {
                       EnvironmentPrinter.print(plugin);
                       player.sendMessage("§a✔ 已輸出至伺服器 Console（DiscordSRV 將同步至 Discord）。");
                      }

                case 1 -> debugCommand.showPluginStatus(player);

                case 2 -> debugCommand.showApiInfo(player);

                case 3 -> openReflectionInputForm(player);

                case 4 -> plugin.getFormManager().openMainMenu(player);

                default -> {
                }
            }

        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }

    /**
     * 跳出一個輸入表單，讓玩家輸入類別名稱（或 Alias，如 flag、user），
     * 交給跟 /bbc debug methods 指令同一套邏輯處理，結果一樣輸出到主控台。
     */
    private void openReflectionInputForm(Player player) {

        CustomForm.Builder builder = CustomForm.builder()
                .title("🔍 Reflection 查詢")
                .input("類別名稱或 Alias", "例如：flag、user，或完整類別路徑")
                .dropdown("查詢範圍", "僅 Public 方法（含繼承）", "僅本類別自行宣告的方法");

        builder.validResultHandler(response -> {

            String target = response.asInput(0);
            int scope = response.asDropdown(1);

            if (target == null || target.isBlank()) {
                player.sendMessage("§c請輸入類別名稱或 Alias。");
                return;
            }

            String className = ReflectionAliases.resolve(target.trim());

            if (scope == 0) {
                ReflectionUtil.printPublicMethods(className);
            } else {
                ReflectionUtil.printDeclaredMethods(className);
            }

            player.sendMessage("§a✔ 已輸出至伺服器 Console（DiscordSRV 將同步至 Discord）。");
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), builder);
    }
}

