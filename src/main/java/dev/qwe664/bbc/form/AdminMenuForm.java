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
                .content("選擇要執行的管理操作")
                .button("🔍 查詢／傳送到玩家島嶼")
                .button("♻ 重載插件設定")
                .button("⬅ 返回主選單");

        builder.validResultHandler(response -> {

            switch (response.clickedButtonId()) {

                case 0 -> plugin.getFormManager().openAdminIslandTeleport(player);

                case 1 -> openReloadConfirm(player);

                case 2 -> plugin.getFormManager().openMainMenu(player);

                default -> {
                }
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    /**
     * 重載會影響全伺服器所有島嶼的設定，執行前多一層確認，
     * 避免手滑點到造成非預期的影響。
     */
    private void openReloadConfirm(Player player) {

        var confirm = SimpleForm.builder()
                .title("♻ 重載插件設定")
                .content("即將執行 /bentobox reload，這會重新載入 BentoBox 的設定檔，\n影響範圍是整個伺服器。確定要執行嗎？")
                .button("✅ 確認重載")
                .button("❌ 取消");

        confirm.validResultHandler(response -> {

            if (response.clickedButtonId() == 0) {
                plugin.getConfigService().reload();
                plugin.getLocaleService().load();
                plugin.getCommandService().execute(player, "bentobox reload");
                player.sendMessage("§a已重新載入 BBC 設定檔與語言檔，並送出 /bentobox reload 指令。");
            } else {
                player.sendMessage("§7已取消。");
            }
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), confirm);
    }
}
