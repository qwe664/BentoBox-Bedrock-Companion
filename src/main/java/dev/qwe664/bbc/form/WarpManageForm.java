package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.warps.managers.WarpSignsManager;
import world.bentobox.warps.objects.PlayerWarp;

/**
 * 管理自己設置的傳送點：查看狀態、刪除。
 *
 * 建立傳送點本身要靠玩家實際去插一個寫著 [WELCOME] 的招牌（Warps 附加模組原生機制），
 * 這個表單無法取代那個動作，只負責「查看現況」跟「刪除」。
 */
public class WarpManageForm extends BaseForm {

    public WarpManageForm(BentoBoxBedrockCompanion plugin) {
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

        if (!plugin.getWarpsHook().isAvailable()) {
            player.sendMessage("§c傳送點功能目前無法使用（伺服器未安裝 Warps 附加模組）。");
            return;
        }

        WarpSignsManager warpSignsManager = plugin.getWarpsHook().getWarpSignsManager();
        World world = player.getWorld();

        PlayerWarp playerWarp = warpSignsManager.getPlayerWarp(world, player.getUniqueId());

        var builder = SimpleForm.builder().title("🚩 我的傳送點");

        if (playerWarp == null) {

            builder.content(
                    "你目前還沒有設置傳送點。\n\n"
                            + "在自己島上放一個招牌，第一行寫 [WELCOME]，"
                            + "就能建立屬於你的傳送點。"
            ).button("⬅ 返回島嶼選單");

            builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        Location location = playerWarp.getLocation();
        String status = playerWarp.isEnabled() ? "§a開放中" : "§c已關閉";

        String content = "狀態：" + status + "\n\n"
                + "位置：" + formatLocation(location);

        builder.content(content)
                .button("🗑 刪除傳送點")
                .button("⬅ 返回島嶼選單");

        builder.validResultHandler(response -> {

            switch (response.clickedButtonId()) {

                case 0 -> {
                    warpSignsManager.removeWarp(world, player.getUniqueId());
                    player.sendMessage("§a已刪除你的傳送點。");
                    plugin.getFormManager().openIslandMenu(player);
                }

                case 1 -> plugin.getFormManager().openIslandMenu(player);

                default -> {
                }
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    private String formatLocation(Location location) {

        if (location == null) {
            return "（未知）";
        }

        return String.format(
                "X: %d, Y: %d, Z: %d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }
}
