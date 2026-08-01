package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            player.sendMessage(ChatColor.GREEN + "Debug system is working.");
            player.sendMessage(ChatColor.GOLD + "=====================");
            return true;
        }

        // /bbc
        plugin.getFormManager().openMainMenu(player);
        return true;
    }
}
