package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DebugCommand {

    private final BentoBoxBedrockCompanion plugin;

    public DebugCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {

        player.sendMessage(ChatColor.GOLD + "===== BBC Debug =====");

        player.sendMessage(status("BBC", plugin));

        player.sendMessage(status("BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        player.sendMessage(status("Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        player.sendMessage(status("Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        player.sendMessage(status("PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        player.sendMessage(ChatColor.GOLD + "=====================");

        return true;
    }

    private String status(String name, Plugin plugin) {

        if (plugin == null) {
            return ChatColor.RED + "✘ " + name + ": Not Installed";
        }

        return ChatColor.GREEN + "✔ " + name + ": "
                + plugin.getDescription().getVersion();
    }
}
