package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerJoinListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public PlayerJoinListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (plugin.getFloodgateHook().isBedrock(player)) {
            plugin.getLogger().info(player.getName() + " joined as Bedrock.");
        } else {
            plugin.getLogger().info(player.getName() + " joined as Java.");
        }

        giveMenuItemIfMissing(player);
    }

    /**
     * 背包裡沒有選單物品才補一份，避免玩家每次重登都拿到重複的。
     */
    private void giveMenuItemIfMissing(Player player) {

        PlayerInventory inventory = player.getInventory();

        for (ItemStack content : inventory.getContents()) {
            if (MenuItem.isMenuItem(plugin, content)) {
                return;
            }
        }

        inventory.addItem(MenuItem.create(plugin));
    }
}
