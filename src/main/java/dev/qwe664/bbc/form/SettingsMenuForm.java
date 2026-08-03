package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.api.flags.Flag;

public class SettingsMenuForm {

    private final BentoBoxBedrockCompanion plugin;

    public SettingsMenuForm(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        // 取得玩家在當前世界的島嶼
        Island island = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        if (island == null) {
            player.sendMessage("§c妳目前沒有島嶼！");
            return;
        }

        // 取得 BentoBox 的 Flags 管理器來讀取狀態
        // 註：不同版本的 BentoBox 取得 flag 狀態的方法可能略有不同，
        // 若 island.getFlag(...) 存在，可以直接讀取；若無，可透過 FlagsManager 查詢。
        boolean pvpState = island.isAllowed(world.bentobox.bentobox.api.flags.Flags.PVP);
        boolean monsterState = island.isAllowed(world.bentobox.bentobox.api.flags.Flags.MONSTER_SPAWN);
        boolean fireState = island.isAllowed(world.bentobox.bentobox.api.flags.Flags.FIRE_SPREAD);

        // 建立 Geyser 自定義表單
        CustomForm.Builder builder = CustomForm.builder()
                .title("島嶼設定管理")
                .toggle("允許 PvP", pvpState)
                .toggle("允許怪物生成", monsterState)
                .toggle("允許火勢蔓延", fireState);

        builder.validResultHandler((form, response) -> {
            boolean pvp = response.asToggle(0);
            boolean monsterSpawn = response.asToggle(1);
            boolean fireSpread = response.asToggle(2);

            // 寫回設定
            island.setFlag(world.bentobox.bentobox.api.flags.Flags.PVP, pvp);
            island.setFlag(world.bentobox.bentobox.api.flags.Flags.MONSTER_SPAWN, monsterSpawn);
            island.setFlag(world.bentobox.bentobox.api.flags.Flags.FIRE_SPREAD, fireSpread);

            player.sendMessage("§a島嶼設定已成功更新！");
        });

        org.geysermc.geyser.api.GeyserApi.api().connectionByUuid(player.getUniqueId()).sendForm(builder.build());
    }
}
