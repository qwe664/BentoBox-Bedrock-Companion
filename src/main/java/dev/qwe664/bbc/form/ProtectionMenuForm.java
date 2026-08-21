package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.menu.ProtectionCategory;
import dev.qwe664.bbc.menu.ProtectionCategories;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.List;

/**
 * 島嶼保護設定的分類選單。
 *
 * BentoBox 的 Flag.Type.PROTECTION 旗標總共有 91 個，無法一次塞進一張表單，
 * 所以先讓玩家挑分類（建築、容器、生物互動…），再進到 ProtectionCategoryForm
 * 針對該分類底下的旗標逐一設定排名。
 */
public class ProtectionMenuForm extends BaseForm {

    public ProtectionMenuForm(BentoBoxBedrockCompanion plugin) {
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

        List<ProtectionCategory> categories = ProtectionCategories.ALL;

        var builder = SimpleForm.builder()
                .title(plugin.getLocaleService().get(player, "protection_menu.title", "🛡 島嶼保護設定"))
                .content(plugin.getLocaleService().get(player, "protection_menu.content", "選擇要設定的分類"));

        var locale = plugin.getLocaleService();

        for (ProtectionCategory category : categories) {
            builder.button(locale.get(player, "protection_categories." + category.id(), category.title()));
        }

        builder.button(plugin.getLocaleService().get(player, "common.back-to-island-menu", "⬅ 返回島嶼選單"));

        builder.validResultHandler(response -> {

            int clicked = response.clickedButtonId();

            if (clicked < categories.size()) {
                new ProtectionCategoryForm(plugin, categories.get(clicked)).open(player);
            } else {
                plugin.getFormManager().openIslandMenu(player);
            }
        });

        api.sendForm(player.getUniqueId(), builder);
    }
}
