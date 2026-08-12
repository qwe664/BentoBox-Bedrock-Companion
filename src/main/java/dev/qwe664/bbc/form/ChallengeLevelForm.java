package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ColorUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.challenges.database.object.Challenge;
import world.bentobox.challenges.database.object.ChallengeLevel;
import world.bentobox.challenges.managers.ChallengesManager;

import java.util.List;

/**
 * 單一關卡（等級）底下的挑戰清單。
 *
 * 不是實例化在 FormManager 裡的單例表單，而是每次點擊關卡時
 * 用 new ChallengeLevelForm(plugin, level).open(player) 建立，
 * 跟 ProtectionCategoryForm 帶參數表單的既有模式一致。
 */
public class ChallengeLevelForm {

    private final BentoBoxBedrockCompanion plugin;
    private final ChallengeLevel level;

    public ChallengeLevelForm(BentoBoxBedrockCompanion plugin, ChallengeLevel level) {
        this.plugin = plugin;
        this.level = level;
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

        ChallengesManager manager = plugin.getChallengesHook().getChallengesManager();
        World world = player.getWorld();
        User user = User.getInstance(player);

        List<Challenge> challenges = manager.getLevelChallenges(level);

        String levelName = level.getFriendlyName() == null || level.getFriendlyName().isBlank()
                ? level.getUniqueId()
                : ColorUtil.translate(level.getFriendlyName());

        var locale = plugin.getLocaleService();

        var builder = SimpleForm.builder()
                .title("🏆 " + levelName);

        if (challenges == null || challenges.isEmpty()) {

            builder.content(locale.get(player, "challenge_level.empty", "這個關卡目前還沒有設置任何挑戰。"))
                    .button(locale.get(player, "challenge_level.back-to-levels", "⬅ 返回關卡清單"));

            builder.validResultHandler(response -> new ChallengesMenuForm(plugin).open(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content(locale.get(player, "challenge_level.content", "選擇一個挑戰查看詳情："));

        for (Challenge challenge : challenges) {

            boolean complete = manager.isChallengeComplete(user, world, challenge);
            String name = challenge.getFriendlyName() == null || challenge.getFriendlyName().isBlank()
                    ? challenge.getUniqueId()
                    : ColorUtil.translate(challenge.getFriendlyName());

            String statusMark = complete ? ColorUtil.translate("&a✅ ") : ColorUtil.translate("&7⏳ ");
            String extra = "";

            if (complete && challenge.isRepeatable()) {
                long times = manager.getChallengeTimes(user, world, challenge);
                extra = "\n" + locale.get(player, "challenge_level.completed-times", "§7已完成 {times} 次").replace("{times}", String.valueOf(times));
            }

            builder.button(statusMark + ColorUtil.translate("&f") + name + extra);
        }

        builder.button(locale.get(player, "challenge_level.back-to-levels", "⬅ 返回關卡清單"));

        int backButtonId = challenges.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                new ChallengesMenuForm(plugin).open(player);
                return;
            }

            Challenge clicked = challenges.get(clickedId);
            new ChallengeDetailForm(plugin, level, clicked).open(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
