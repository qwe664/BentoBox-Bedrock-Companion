package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import dev.qwe664.bbc.developer.reflection.ReflectionAliases;

public class DebugCommand {

    private final BentoBoxBedrockCompanion plugin;

    public DebugCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {

        // /bbc debug
        if (args.length == 1) {
            showCommandList(sender);
            return true;
        }

        // /bbc debug plugins
        if (args[1].equalsIgnoreCase("plugins")) {
            showPluginStatus(sender);
            return true;
        }

        // /bbc debug methods
        if (args[1].equalsIgnoreCase("methods")) {

            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "用法：");
                sender.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods <Alias 或完整類別名稱>");
                sender.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods declared <Alias 或完整類別名稱>");
                sender.sendMessage(ChatColor.GRAY + "例如：");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods flag");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods declared flag");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods user");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods declared user");
                return true;
            }

            boolean declaredOnly = false;
            String target;

            if (args[2].equalsIgnoreCase("declared")) {

                declaredOnly = true;

                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "請輸入 Alias 或完整類別名稱。");
                    return true;
                }

                target = args[3];

            } else {

                target = args[2];
            }

            String className = ReflectionAliases.resolve(target);

            if (declaredOnly) {
                ReflectionUtil.printDeclaredMethods(className);
            } else {
                ReflectionUtil.printPublicMethods(className);
            }

            sender.sendMessage(ChatColor.GREEN
                    + "[BBC] 已將反射資訊輸出至主控台。");

            return true;
        }

        // 尚未實作的子指令
        sender.sendMessage(ChatColor.YELLOW + "[BBC] 此 Debug 功能尚未實作。");
        return true;
    }

    private void showCommandList(CommandSender sender) {

        sender.sendMessage(ChatColor.GOLD + "===== BBC Debug =====");
        sender.sendMessage(ChatColor.YELLOW + "可用子指令：");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug plugins");
        sender.sendMessage(ChatColor.GRAY + "查看插件載入狀態");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods");
        sender.sendMessage(ChatColor.GRAY + "Reflection 方法探索");
        sender.sendMessage(ChatColor.GRAY + "Alias：flag、user");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods declared");
        sender.sendMessage(ChatColor.GRAY + "只顯示類別自行宣告的 Public 方法");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug api");
        sender.sendMessage(ChatColor.GRAY + "Public API 探索（開發中）");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug island");
        sender.sendMessage(ChatColor.GRAY + "Island API 探索（開發中）");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug flags");
        sender.sendMessage(ChatColor.GRAY + "Protection Flag 探索（開發中）");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug version");
        sender.sendMessage(ChatColor.GRAY + "BBC 版本資訊（開發中）");

        sender.sendMessage(ChatColor.GOLD + "=====================");
    }

    private void showPluginStatus(CommandSender sender) {

        sender.sendMessage(ChatColor.GOLD + "===== BBC Plugin Status =====");

        sender.sendMessage(status("BBC", plugin));

        sender.sendMessage(status("BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        sender.sendMessage(status("Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        sender.sendMessage(status("Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        sender.sendMessage(status("PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        sender.sendMessage(ChatColor.GOLD + "=============================");
    }

    private String status(String name, Plugin plugin) {

        if (plugin == null) {
            return ChatColor.RED + "✘ " + name + ": Not Installed";
        }

        return ChatColor.GREEN + "✔ " + name + ": "
                + plugin.getDescription().getVersion();
    }
}
