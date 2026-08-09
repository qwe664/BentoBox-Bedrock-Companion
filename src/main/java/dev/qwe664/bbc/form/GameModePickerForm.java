package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.service.BentoBoxService;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.List;

/**
 * 玩家不在任何玩法（GameModeAddon）的世界裡時（例如站在主城、大廳），
 * 光看「目前所在世界」沒辦法判斷玩家想去哪個玩法的島嶼，
 * 這時候跳出這個表單，列出伺服器目前所有已啟用的玩法，讓玩家自己選一個，
 * 選完直接執行「該玩法的指令別名 + 附加指令」（例如 "ob" + " team" → "ob team"）。
 *
 * 不是掛在 FormManager 裡的單例表單，用 new 直接實例化，
 * 跟 ProtectionCategoryForm、ChallengeLevelForm 這種帶參數的表單一致模式。
 */
public class GameModePickerForm {

    private final BentoBoxBedrockCompanion plugin;
    private final String commandSuffix;

    /**
     * @param commandSuffix 選定玩法後要接在指令別名後面執行的附加指令，
     *                      例如空字串代表單純傳送到島嶼，" team" 代表開啟隊伍管理。
     */
    public GameModePickerForm(BentoBoxBedrockCompanion plugin, String commandSuffix) {
        this.plugin = plugin;
        this.commandSuffix = commandSuffix;
    }

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

        List<BentoBoxService.GameModeChoice> choices = plugin.getBentoBoxService().getAvailableGameModes();

        var builder = SimpleForm.builder()
                .title("🏝 選擇玩法");

        if (choices.isEmpty()) {

            builder.content("目前伺服器沒有偵測到任何已啟用的空島玩法，請聯絡管理員。")
                    .button("⬅ 返回主選單");

            builder.validResultHandler(response -> plugin.getFormManager().openMainMenu(player));

            api.sendForm(player.getUniqueId(), builder);
            return;
        }

        builder.content("你目前不在任何空島世界裡，請選擇要前往的玩法：");

        for (BentoBoxService.GameModeChoice choice : choices) {
            builder.button("🏝 " + choice.name());
        }

        builder.button("⬅ 返回主選單");

        int backButtonId = choices.size();

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                plugin.getFormManager().openMainMenu(player);
                return;
            }

            String label = choices.get(clickedId).label();
            plugin.getCommandService().execute(player, label + commandSuffix);
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
