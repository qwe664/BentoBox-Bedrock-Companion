package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

/**
 * 傳送點功能的入口選單，分兩個子功能：
 * - 瀏覽全部：看伺服器上所有玩家的傳送點，點了直接過去
 * - 管理我的：看自己傳送點的狀態、刪除
 */
public class WarpMenuForm extends BaseForm {

    public WarpMenuForm(BentoBoxBedrockCompanion plugin) {
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

        if (!plugin.getWarpsHook().isAvailable()) {
            player.sendMessage(plugin.getLocaleService().get(player, "warp_menu.unavailable", "§c傳送點功能目前無法使用（伺服器未安裝 Warps 附加模組）。"));
            return;
        }

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title(locale.get(player, "warp_menu.title", "🚩 傳送點"))
                .content(locale.get(player, "warp_menu.content", "請選擇功能"))
                .button(locale.get(player, "warp_menu.browse", "🗺 瀏覽全部傳送點"))
                .button(locale.get(player, "warp_menu.manage", "⚙ 管理我的傳送點"))
                .button(locale.get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

        builder.validResultHandler(response -> {

            switch (response.clickedButtonId()) {

                case 0 -> plugin.getFormManager().openWarpBrowse(player);

                case 1 -> plugin.getFormManager().openWarpManage(player);

                case 2 -> plugin.getFormManager().openIslandMenu(player);

                default -> {
                }
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
