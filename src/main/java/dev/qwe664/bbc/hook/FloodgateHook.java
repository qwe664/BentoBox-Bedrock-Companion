package dev.qwe664.bbc.hook;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateHook {

    /**
     * 判斷玩家是否為 Bedrock 玩家
     *
     * @param player Bukkit 玩家
     * @return true = Bedrock，false = Java
     */
    public boolean isBedrock(Player player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }
}
