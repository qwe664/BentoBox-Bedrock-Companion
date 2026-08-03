package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.api.GeyserApi;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

public class SettingsMenuForm {

    private final BentoBoxBedrockCompanion plugin;

    public SettingsMenuForm(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Island island = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        if (island == null) {
            player.sendMessage("§c妳目前沒有島嶼！");
            return;
        }

        // 使用 BentoBox 支援的字串旗標讀取狀態
        boolean pvpState = island.isAllowed("PVP");
        boolean monsterState = island.isAllowed("MONSTER_SPAWN");
        boolean fireState = island.isAllowed("FIRE_SPREAD");

        CustomForm.Builder builder = CustomForm.builder()
                .title("島嶼設定管理")
                .toggle("允許 PvP", pvpState)
                .toggle("允許怪物生成", monsterState)
                .toggle("允許火勢蔓延", fireState);

        builder.validResultHandler((form, response) -> {
            boolean pvp = response.asToggle(0);
            boolean monsterSpawn = response.asToggle(1);
            boolean fireSpread = response.asToggle(2);

            // 使用字串寫回設定
            island.setFlag("PVP", pvp);
            island.setFlag("MONSTER_SPAWN", monsterSpawn);
            island.setFlag("FIRE_SPREAD", fireSpread);

            player.sendMessage("§a島嶼設定已成功更新！");
        });

        // 傳送表單給基岩版玩家
        GeyserApi.api().connectionByUuid(player.getUniqueId()).sendForm(builder.build());
    }
}
