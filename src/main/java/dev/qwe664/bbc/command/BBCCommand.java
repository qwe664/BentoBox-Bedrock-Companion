package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BBCCommand implements CommandExecutor {

    private final BentoBoxBedrockCompanion plugin;

    public BBCCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用此指令。");
            return true;
        }

        // /bbc debug
        if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {

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

        // /bbc
        plugin.getFormManager().openMainMenu(player);

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
