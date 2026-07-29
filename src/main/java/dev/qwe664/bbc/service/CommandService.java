package dev.qwe664.bbc.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandService {

    private final Plugin plugin;

    public CommandService(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String command) {
        return player.performCommand(command);
    }

    public void executeLater(Player player, String command, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.performCommand(command);
        }, delayTicks);
    }
}
