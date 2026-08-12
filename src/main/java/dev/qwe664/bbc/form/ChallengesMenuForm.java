package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ColorUtil;
import dev.qwe664.bbc.util.ProgressBarUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.challenges.database.object.Challenge;
import world.bentobox.challenges.database.object.ChallengeLevel;
import world.bentobox.challenges.managers.ChallengesManager;
import world.bentobox.challenges.utils.LevelStatus;

import java.util.List;

/**
 * 挑戰關卡（等級）清單。
 *
 * 每個等級一個按鈕，顯示解鎖狀態跟完成進度條。
 * 資料來源是 ChallengesManager.getAllChallengeLevelStatus(User, World)，
 * 這個方法一次回傳每個等級目前的解鎖/完成狀態，不用自己再逐一查詢。
 */
public class ChallengesMenuForm extends BaseForm {

    public ChallengesMenuForm(BentoBoxBedrockCompanion plugin) {
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

        if (!plugin.getChallengesHook().isAvailable()) {
            player.sendMessage(plugin.getLocaleService().get(player, "challenges_menu.unavailable", "§c挑戰功能目前無法使用（伺服器未安裝 Challenges 附加模組）。"));
            return;
        }

        ChallengesManager manager = plugin.getChallengesHook().getChallengesManager();
        World world = player.getWorld();
        User user = User.getInstance(player);

        List<LevelStatus> statusList = manager.getAllChallengeLevelStatus(user, world);

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title(locale.get(player, "challenges_menu.title", "🏆 挑戰關卡"));

        if (statusList == null || statusList.isEmpty()) {

            builder.content(locale.get(player, "challenges_menu.empty", "這個世界目前還沒有設置任何挑戰關卡。"))
                    .button(locale.get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

            builder.validResultHandler(response -> plugin.getFormManager().openIslandMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content(locale.get(player, "challenges_menu.content", "選擇一個關卡查看挑戰內容："));

        for (LevelStatus status : statusList) {

            ChallengeLevel level = status.getLevel();
            List<Challenge> levelChallenges = manager.getLevelChallenges(level);
            int total = levelChallenges.size();

            // 注意：LevelStatus.getNumberOfChallengesStillToDo() 語意是「前一關還剩幾個沒做完」，
            // 用來判斷這一關要不要解鎖，不是「這一關本身還剩幾個」——不能拿來算這關的進度條。
            // 第一關沒有前一關，這個值結構上永遠是 0，直接拿來減會讓第一關進度條恆為滿格。
            // 正確做法是跟 ChallengeLevelForm 一樣，逐一檢查這關底下每個挑戰的完成狀態。
            int completed = (int) levelChallenges.stream()
                    .filter(challenge -> manager.isChallengeComplete(user, world, challenge))
                    .count();

            String lockState = status.isUnlocked() ? "" : ColorUtil.translate("&c🔒 ");
            String levelName = level.getFriendlyName() == null || level.getFriendlyName().isBlank()
                    ? level.getUniqueId()
                    : ColorUtil.translate(level.getFriendlyName());

            String buttonText = lockState + ColorUtil.translate("&f") + levelName
                    + "\n" + ProgressBarUtil.build(completed, total);

            builder.button(buttonText);
        }

        builder.button(locale.get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

        int backButtonId = statusList.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openIslandMenu(player);
                return;
            }

            LevelStatus clickedStatus = statusList.get(clickedId);

            if (!clickedStatus.isUnlocked()) {
                player.sendMessage(locale.get(player, "challenges_menu.locked", "§c這個關卡尚未解鎖，請先完成前面的關卡。"));
                open(player);
                return;
            }

            new ChallengeLevelForm(plugin, clickedStatus.getLevel()).open(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
