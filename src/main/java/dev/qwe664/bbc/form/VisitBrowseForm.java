package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.database.objects.Island;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 瀏覽玩家目前所在玩法裡所有已擁有的島嶼，點擊直接前往拜訪。
 *
 * 讀取島嶼清單直接呼叫官方 API（安全，純讀取）：
 * IslandsManager.getIslands(World) 取得該世界所有島嶼，過濾 isOwned()
 * （排除還沒被任何人認領的島）；反編譯 Visit-1.7.0.jar 的
 * VisitPanel 建構子確認排序邏輯（島嶼名稱優先、沒設名字就用島主名字，
 * 不分大小寫排序），這裡照做保持跟 Visit 原生介面一致的瀏覽體驗。
 *
 * 但「實際傳送過去」這個動作 —— 反編譯確認 VisitAddonManager 的
 * processTeleportation() 內部還牽涉付費/課稅、跟島主分潤、玩家背包
 * 掉落中不能傳送等一整套判斷邏輯（preprocessTeleportation），
 * 一律轉發成 Visit 官方指令 "<玩法別名> visit <島主UUID> bypass" 執行
 * （反編譯 VisitPlayerCommand.execute() 確認這是原生 GUI 自己點擊
 * 「確認拜訪」按鈕時實際呼叫的指令格式，bypass 參數代表略過官方
 * GUI 自己的付費二次確認，因為我們這邊沒有另外做付費確認畫面），
 * 不自己重寫這整套判斷，避免跟官方權限檢查、事件觸發行為不一致
 * ——沿用專案一貫的「讀取直接呼叫 API、會改變狀態的動作轉發官方指令」原則。
 */
public class VisitBrowseForm extends BaseForm {

    public VisitBrowseForm(BentoBoxBedrockCompanion plugin) {
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

        if (!plugin.getVisitHook().isAvailable()) {
            player.sendMessage("§c拜訪島嶼功能目前無法使用（伺服器未安裝 Visit 附加模組）。");
            return;
        }

        World world = player.getWorld();

        List<Island> islandList = new ArrayList<>(
                plugin.getBentoBoxService().getIslandsManager().getIslands(world));

        islandList.removeIf(island -> !island.isOwned());

        islandList.sort((a, b) -> {
            String nameA = a.getName() != null ? a.getName()
                    : plugin.getBentoBoxService().getPlayersManager().getName(a.getOwner());
            String nameB = b.getName() != null ? b.getName()
                    : plugin.getBentoBoxService().getPlayersManager().getName(b.getOwner());
            return nameA.compareToIgnoreCase(nameB);
        });

        var builder = SimpleForm.builder()
                .title("🧭 拜訪島嶼");

        if (islandList.isEmpty()) {

            builder.content("目前這個玩法裡還沒有可以拜訪的島嶼。")
                    .button("⬅ 返回島嶼選單");

            builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content("選擇一座島嶼前往拜訪：");

        for (Island island : islandList) {
            String ownerName = plugin.getBentoBoxService().getPlayersManager().getName(island.getOwner());
            String islandName = island.getName() != null ? island.getName()
                    : (ownerName == null || ownerName.isBlank() ? "未知玩家" : ownerName);
            String selfTag = island.getOwner() != null && island.getOwner().equals(player.getUniqueId()) ? "（你）" : "";
            builder.button("🏝 " + islandName + selfTag + "\n§7" + (ownerName == null ? "" : ownerName));
        }

        builder.button("⬅ 返回島嶼選單");

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == islandList.size()) {
                plugin.getFormManager().openIslandMenu(player);
                return;
            }

            UUID targetOwnerUuid = islandList.get(clickedId).getOwner();

            if (targetOwnerUuid == null) {
                player.sendMessage("§c這座島嶼目前沒有島主，無法拜訪。");
                open(player);
                return;
            }

            plugin.getBentoBoxService().getPlayerCommandLabel(player)
                    .ifPresentOrElse(
                            label -> plugin.getCommandService().execute(player,
                                    label + " visit " + targetOwnerUuid + " bypass"),
                            () -> player.sendMessage("§c查不到目前玩法，無法執行操作。")
                    );
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
