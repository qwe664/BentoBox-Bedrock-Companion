package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.challenges.ChallengesAddon;

import java.util.List;

/**
 * 管理員專用：把 Challenges 官方內建的「預設挑戰組」（5 個等級、
 * 57 個挑戰）匯入指定玩法。
 *
 * 反編譯實際伺服器 Challenges-1.8.0.jar 確認整個匯入機制的關鍵細節，
 * 不是猜測：
 *
 * - Challenges 官方指令（/<玩法admin指令> challenges）沒有任何一個
 *   聊天子指令可以直接匯入預設挑戰——反編譯 ChallengesAdminCommand
 *   確認註冊的子指令只有 reload/complete/reset/show/migrate 五個，
 *   「匯入預設挑戰」這個動作只存在於官方箱子 GUI（AdminPanel）裡，
 *   聊天指令完全碰不到，這也是這個功能沒辦法直接轉發指令、
 *   一定要自己呼叫 API 的原因。
 *
 * - ChallengesAddon.onEnable() 會呼叫
 *   saveResource("default.json", false)，把附加模組自帶的官方預設
 *   挑戰組（5 個等級、57 個挑戰）複製到
 *   plugins/BentoBox/addons/Challenges/default.json，伺服器每次開機
 *   都會確保這個檔案存在。
 *
 * - 真正負責匯入的是 ChallengesImportManager.importDatabaseFile(User,
 *   World, String fileName)（透過 ChallengesAddon.getImportManager()
 *   取得），這個方法會去讀 <fileName>.json，所以固定傳入 "default"
 *   就是讀取上面那個官方預設挑戰組。
 *
 * - 這個方法內部本身就會先呼叫 wipeDatabase() 清空該玩法「現有」的
 *   挑戰資料，是破壞性操作，因此在呼叫前一定要自己做二次確認
 *   （官方箱子 GUI 或聊天指令走的是 BentoBox 自己的
 *   ConfirmableCommand 二次確認流程，我們繞過官方指令直接呼叫 API，
 *   所以這個確認步驟要自己補上，不能省略）。
 *
 * 之前使用者誤觸的「匯入了一個叫 Example Level 的空關卡」，
 * 是另一條完全不同的路徑（importFile() 讀取的是 YAML 格式的
 * template.yml 範例檔，不是這裡的 default.json），兩者互不相關。
 */
public class AdminChallengesImportForm {

    private final BentoBoxBedrockCompanion plugin;

    public AdminChallengesImportForm(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

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

        if (!plugin.getChallengesHook().isAvailable()) {
            player.sendMessage(plugin.getLocaleService().get(player, "challenges_menu.unavailable", "§c挑戰功能目前無法使用（伺服器未安裝 Challenges 附加模組）。"));
            return;
        }

        List<GameModeAddon> gameModes = plugin.getBentoBoxService().getBentoBox().getAddonsManager().getGameModeAddons();

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title(locale.get(player, "admin_challenges_import.title", "📥 匯入官方預設挑戰"));

        if (gameModes.isEmpty()) {

            builder.content(locale.get(player, "admin_challenges_import.no-gamemodes", "目前伺服器沒有偵測到任何已啟用的空島玩法。"))
                    .button(locale.get(player, "admin_challenges_import.back", "⬅ 返回管理工具"));

            builder.validResultHandler(response -> plugin.getFormManager().openAdminMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content(locale.get(player, "admin_challenges_import.content", "選擇要匯入官方預設挑戰組（5 個等級、57 個挑戰）的玩法：\n§c這個動作會清空該玩法現有的挑戰資料，無法復原。"));

        for (GameModeAddon gameMode : gameModes) {
            builder.button("🏝 " + gameMode.getDescription().getName());
        }

        builder.button(locale.get(player, "admin_challenges_import.back", "⬅ 返回管理工具"));

        int backButtonId = gameModes.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openAdminMenu(player);
                return;
            }

            openConfirm(player, gameModes.get(clickedId));
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    private void openConfirm(Player player, GameModeAddon gameMode) {

        var locale = plugin.getLocaleService();
        String gameModeName = gameMode.getDescription().getName();

        var confirm = SimpleForm.builder()
                .title(locale.get(player, "admin_challenges_import.confirm-title", "⚠ 確認匯入"))
                .content(locale.get(player, "admin_challenges_import.confirm-content",
                                "即將把「{gamemode}」現有的挑戰資料全部清除，改用官方預設挑戰組（5 個等級、57 個挑戰）取代。\n\n這個動作無法復原，確定要繼續嗎？")
                        .replace("{gamemode}", gameModeName))
                .button(locale.get(player, "admin_challenges_import.confirm-yes", "✅ 確認匯入"))
                .button(locale.get(player, "admin_challenges_import.confirm-no", "❌ 取消"));

        confirm.validResultHandler(response -> {

            if (response.clickedButtonId() != 0) {
                player.sendMessage(locale.get(player, "admin_challenges_import.cancelled", "§7已取消。"));
                open(player);
                return;
            }

            ChallengesAddon addon = plugin.getChallengesHook().getChallengesAddon();
            World world = gameMode.getOverWorld();
            User user = User.getInstance(player);

            addon.getImportManager().importDatabaseFile(user, world, "default");

            player.sendMessage(locale.get(player, "admin_challenges_import.success", "§a已將官方預設挑戰組匯入「{gamemode}」。")
                    .replace("{gamemode}", gameModeName));

            plugin.getFormManager().openAdminMenu(player);
        });

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), confirm);
    }
}
