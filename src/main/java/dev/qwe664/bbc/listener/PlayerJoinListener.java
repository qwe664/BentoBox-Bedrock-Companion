package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public PlayerJoinListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getFloodgateHook().isBedrock(event.getPlayer())) {
            plugin.getLogger().info(event.getPlayer().getName() + " joined as Bedrock.");
        } else {
            plugin.getLogger().info(event.getPlayer().getName() + " joined as Java.");
        }
    }
}
