package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.MenuItem;
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
            if (!(sender instanceof Player player) || plugin.getPermissionService().hasDeveloperPermission(player)) {
                return debugCommand.execute(sender, args);
            }
            sender.sendMessage(ChatColor.RED + "[BBC] 你沒有權限使用這個指令。");
            return true;
        }

        // /bbc item — 補領選單物品，主要給更新前就已加入的舊玩家使用
        // （PlayerJoinListener 只在「加入伺服器」那一刻補發，已經在線的人不會觸發）。
        if (args.length > 0 && args[0].equalsIgnoreCase("item")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.YELLOW + "[BBC] 這個指令僅限玩家使用。");
                return true;
            }
            player.getInventory().addItem(MenuItem.create(plugin));
            player.sendMessage(ChatColor.GREEN + "[BBC] 已給予選單物品。");
            return true;
        }

        // /bbc
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.YELLOW + "[BBC] 主選單僅限玩家使用。");
            sender.sendMessage(ChatColor.GRAY + "可使用：");
            sender.sendMessage(ChatColor.GRAY + "  /bbc help");
            sender.sendMessage(ChatColor.GRAY + "  /bbc debug");
            return true;
        }

        // 基岩版玩家開啟 BBC 的表單主選單；
        // Java 版玩家不需要繞這一層，BentoBox 本身就是為 Java 版設計的，
        // 直接轉發到 /is 即可（沒有島嶼時 BentoBox 會自動跳出建立島嶼的原生 GUI）。
        if (plugin.getFloodgateHook().isBedrock(player)) {
            plugin.getFormManager().openMainMenu(player);
        } else {
            plugin.getCommandService().execute(player, "is");
        }

        return true;
    }
}
