package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ReflectionUtil;
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

        // /bbc debug
        if (args.length == 1) {
            showCommandList(player);
            return true;
        }

        // /bbc debug plugins
        if (args[1].equalsIgnoreCase("plugins")) {
            showPluginStatus(player);
            return true;
        }

        // /bbc debug methods
        if (args[1].equalsIgnoreCase("methods")) {

            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "用法：");
                player.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods <Alias 或完整類別名稱>");
                player.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods declared <Alias 或完整類別名稱>");
                player.sendMessage(ChatColor.GRAY + "例如：");
                player.sendMessage(ChatColor.GRAY + "/bbc debug methods flag");
                player.sendMessage(ChatColor.GRAY + "/bbc debug methods declared flag");
                player.sendMessage(ChatColor.GRAY + "/bbc debug methods user");
                player.sendMessage(ChatColor.GRAY + "/bbc debug methods declared user");
                return true;
            }

            boolean declaredOnly = false;
            String target;

            if (args[2].equalsIgnoreCase("declared")) {

                declaredOnly = true;

                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "請輸入 Alias 或完整類別名稱。");
                    return true;
                }

                target = args[3];

            } else {

                target = args[2];
            }

            String className = switch (target.toLowerCase()) {
                case "flag" -> "world.bentobox.bentobox.api.flags.Flag";
                case "user" -> "world.bentobox.bentobox.api.user.User";
                default -> target;
            };

            if (declaredOnly) {
                ReflectionUtil.printDeclaredMethods(className);
            } else {
                ReflectionUtil.printPublicMethods(className);
            }

            player.sendMessage(ChatColor.GREEN
                    + "[BBC] 已將反射資訊輸出至主控台。");

            return true;
        }

        // 尚未實作的子指令
        player.sendMessage(ChatColor.YELLOW + "[BBC] 此 Debug 功能尚未實作。");
        return true;
    }

    private void showCommandList(Player player) {

        player.sendMessage(ChatColor.GOLD + "===== BBC Debug =====");
        player.sendMessage(ChatColor.YELLOW + "可用子指令：");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug plugins");
        player.sendMessage(ChatColor.GRAY + "查看插件載入狀態");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug methods");
        player.sendMessage(ChatColor.GRAY + "Reflection 方法探索");
        player.sendMessage(ChatColor.GRAY + "Alias：flag、user");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug methods declared");
        player.sendMessage(ChatColor.GRAY + "只顯示類別自行宣告的方法");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug api");
        player.sendMessage(ChatColor.GRAY + "Public API 探索（開發中）");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug island");
        player.sendMessage(ChatColor.GRAY + "Island API 探索（開發中）");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug flags");
        player.sendMessage(ChatColor.GRAY + "Protection Flag 探索（開發中）");
        player.sendMessage("");

        player.sendMessage(ChatColor.GREEN + "/bbc debug version");
        player.sendMessage(ChatColor.GRAY + "BBC 版本資訊（開發中）");

        player.sendMessage(ChatColor.GOLD + "=====================");
    }

    private void showPluginStatus(Player player) {

        player.sendMessage(ChatColor.GOLD + "===== BBC Plugin Status =====");

        player.sendMessage(status("BBC", plugin));

        player.sendMessage(status("BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        player.sendMessage(status("Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        player.sendMessage(status("Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        player.sendMessage(status("PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        player.sendMessage(ChatColor.GOLD + "=============================");
    }

    private String status(String name, Plugin plugin) {

        if (plugin == null) {
            return ChatColor.RED + "✘ " + name + ": Not Installed";
        }

        return ChatColor.GREEN + "✔ " + name + ": "
                + plugin.getDescription().getVersion();
    }
}
