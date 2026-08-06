package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.database.objects.Island;

import java.util.UUID;

/**
 * 管理員用：輸入玩家名稱，查詢並傳送到該玩家的島嶼。
 *
 * 玩家名稱 → UUID 這一步用 Bukkit 原生的 OfflinePlayer 查，
 * 不依賴 BentoBox 內部的名稱快取，只要那個名字曾經加入過本伺服器就查得到，
 * 跟島嶼是否存在、玩家現在在不在線都無關。
 */
public class AdminIslandTeleportForm extends BaseForm {

    public AdminIslandTeleportForm(BentoBoxBedrockCompanion plugin) {
        super(plugin);
    }

    @Override
    public void open(Player admin) {

        FloodgateApi api = FloodgateApi.getInstance();

        if (api == null) {
            admin.sendMessage("§cFloodgate API 尚未初始化。");
            return;
        }

        CustomForm.Builder builder = CustomForm.builder()
                .title("🔍 查詢／傳送到玩家島嶼")
                .input("玩家名稱", "輸入完整的遊戲內名稱");

        builder.validResultHandler(response -> {

            String targetName = response.asInput(0);

            if (targetName == null || targetName.isBlank()) {
                admin.sendMessage("§c請輸入玩家名稱。");
                return;
            }

            targetName = targetName.trim();

            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            UUID targetUuid = target.getUniqueId();

            if (!target.hasPlayedBefore() && !target.isOnline()) {
                admin.sendMessage("§c找不到玩家：" + targetName + "（這個名字沒有加入過本伺服器）");
                return;
            }

            Island island = plugin.getBentoBoxService().getIslandsManager()
                    .getIsland(admin.getWorld(), targetUuid);

            if (island == null) {
                admin.sendMessage("§e" + targetName + " 目前沒有島嶼。");
                return;
            }

            if (island.getCenter() == null) {
                admin.sendMessage("§c這座島嶼沒有可傳送的中心點資料。");
                return;
            }

            admin.teleport(island.getCenter());
            admin.sendMessage("§a已傳送到 " + targetName + " 的島嶼。");
        });

        api.sendForm(admin.getUniqueId(), builder);
    }
}
