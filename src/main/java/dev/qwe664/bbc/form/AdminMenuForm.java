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
            player.sendMessage(plugin.getLocaleService().get(player, "common.floodgate-not-ready", "§cFloodgate API 尚未初始化。"));
            return;
        }

        if (!api.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage(plugin.getLocaleService().get(player, "common.bedrock-only", "§e目前只有基岩版玩家可以使用 Bedrock UI。"));
            return;
        }

        var builder = SimpleForm.builder();

        var locale = plugin.getLocaleService();

        java.util.List<Runnable> actions = new java.util.ArrayList<>();

        builder.title(locale.get(player, "admin_menu.title", "👮 管理工具"))
                .content(locale.get(player, "admin_menu.content", "選擇要執行的管理操作"));

        builder.button(locale.get(player, "admin_menu.teleport", "🔍 查詢／傳送到玩家島嶼"));
        actions.add(() -> plugin.getFormManager().openAdminIslandTeleport(player));

        if (plugin.getChallengesHook().isAvailable()) {
            builder.button(locale.get(player, "admin_menu.import-challenges", "📥 匯入官方預設挑戰"));
            actions.add(() -> new AdminChallengesImportForm(plugin).open(player));
        }

        builder.button(locale.get(player, "admin_menu.reload", "♻ 重載插件設定"));
        actions.add(() -> openReloadConfirm(player));

        builder.button(locale.get(player, "admin_menu.back", "⬅ 返回主選單"));
        int backButtonId = actions.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openMainMenu(player);
                return;
            }

            actions.get(clickedId).run();
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    /**
     * 重載會影響全伺服器所有島嶼的設定，執行前多一層確認，
     * 避免手滑點到造成非預期的影響。
     */
    private void openReloadConfirm(Player player) {

        var locale = plugin.getLocaleService();

        var confirm = SimpleForm.builder()
                .title(locale.get(player, "admin_menu.reload-confirm-title", "♻ 重載插件設定"))
                .content(locale.get(player, "admin_menu.reload-confirm-content", "即將執行 /bentobox reload，這會重新載入 BentoBox 的設定檔，\n影響範圍是整個伺服器。確定要執行嗎？"))
                .button(locale.get(player, "admin_menu.reload-confirm-yes", "✅ 確認重載"))
                .button(locale.get(player, "admin_menu.reload-confirm-no", "❌ 取消"));

        confirm.validResultHandler(response -> {

            if (response.clickedButtonId() == 0) {
                plugin.getConfigService().reload();
                plugin.getLocaleService().load();
                plugin.getCommandService().execute(player, "bentobox reload");
                player.sendMessage(locale.get(player, "admin_menu.reload-done", "§a已重新載入 BBC 設定檔與語言檔，並送出 /bentobox reload 指令。"));
            } else {
                player.sendMessage(locale.get(player, "admin_menu.reload-cancelled", "§7已取消。"));
            }
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), confirm);
    }
}
