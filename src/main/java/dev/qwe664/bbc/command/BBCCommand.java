package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BBCCommand implements CommandExecutor {

    private final BentoBoxBedrockCompanion plugin;
    private final HelpCommand helpCommand;
    private final DebugCommand debugCommand;

    public BBCCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        this.helpCommand = new HelpCommand(plugin);
        this.debugCommand = new DebugCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /bbc help
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            return helpCommand.execute(sender, args);
        }

        // /bbc debug
        if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {
            return debugCommand.execute(sender, args);
        }

        // /bbc
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.YELLOW + "[BBC] 主選單僅限玩家使用。");
            sender.sendMessage(ChatColor.GRAY + "可使用：");
            sender.sendMessage(ChatColor.GRAY + "  /bbc help");
            sender.sendMessage(ChatColor.GRAY + "  /bbc debug");
            return true;
        }

        plugin.getFormManager().openMainMenu(player);
        return true;
    }
}
