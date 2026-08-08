package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.warps.managers.WarpSignsManager;
import world.bentobox.warps.objects.PlayerWarp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 瀏覽全伺服器所有玩家設置的傳送點（Warp），點擊直接傳送過去。
 *
 * 資料來源是 WarpSignsManager.getWarpMap()，回傳 <玩家 UUID, PlayerWarp> 的對照表，
 * 這裡把它轉成一顆顆按鈕，按鈕文字用玩家名字，點了就呼叫 public 的 warpPlayer(World, User, UUID) 實際傳送。
 */
public class WarpBrowseForm extends BaseForm {

    public WarpBrowseForm(BentoBoxBedrockCompanion plugin) {
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

        Map<UUID, PlayerWarp> warpMap = warpSignsManager.getWarpMap(world);

        var builder = SimpleForm.builder()
                .title("🚩 傳送點列表");

        if (warpMap == null || warpMap.isEmpty()) {

            builder.content("目前還沒有玩家設置傳送點。")
                    .button("⬅ 返回島嶼選單");

            builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content("選擇一個傳送點前往：");

        List<UUID> ownerList = new ArrayList<>(warpMap.keySet());

        for (UUID ownerUuid : ownerList) {
            String ownerName = plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);
            builder.button("🚩 " + (ownerName == null || ownerName.isBlank() ? "未知玩家" : ownerName));
        }

        builder.button("⬅ 返回島嶼選單");

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            // 最後一顆是返回按鈕
            if (clickedId == ownerList.size()) {
                plugin.getFormManager().openIslandMenu(player);
                return;
            }

            UUID targetOwnerUuid = ownerList.get(clickedId);
            Location warpLocation = warpSignsManager.getWarpLocation(world, targetOwnerUuid);

            if (warpLocation == null) {
                player.sendMessage("§c這個傳送點已經不存在了。");
                open(player);
                return;
            }

            User user = User.getInstance(player);
            warpSignsManager.warpPlayer(world, user, targetOwnerUuid);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
