package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements BaseCommand {

    private final BentoBoxBedrockCompanion plugin;

    public HelpCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        showHelp(sender);
        return true;
    }

    private void showHelp(CommandSender sender) {

        sender.sendMessage(ChatColor.GOLD + "========== BBC ==========");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.YELLOW + "General");
        sender.sendMessage(ChatColor.GREEN + "/bbc");
        sender.sendMessage(ChatColor.GRAY + "基岩版：開啟 BBC 表單主選單");
        sender.sendMessage(ChatColor.GRAY + "Java 版：直接轉發到 /is");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc help");
        sender.sendMessage(ChatColor.GRAY + "顯示此說明");
        sender.sendMessage("");

        // 只有具備開發者權限的人才看得到後面這段，
        // 一般玩家看到一堆 Debug 指令反而只會覺得困惑。
        if (sender instanceof Player player
                && plugin.getPermissionService().hasDeveloperPermission(player)) {

            sender.sendMessage(ChatColor.YELLOW + "Developer");
            sender.sendMessage(ChatColor.GREEN + "/bbc debug");
            sender.sendMessage(ChatColor.GRAY + "顯示 Debug 子指令清單");
            sender.sendMessage("");

            sender.sendMessage(ChatColor.GREEN + "/bbc debug plugins");
            sender.sendMessage(ChatColor.GRAY + "查看插件載入狀態");
            sender.sendMessage("");

            sender.sendMessage(ChatColor.GREEN + "/bbc debug methods <alias|類別名稱>");
            sender.sendMessage(ChatColor.GRAY + "Reflection 方法探索（可加 declared 只看自身宣告的方法）");
            sender.sendMessage("");
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "更多功能將於後續版本加入。");
        sender.sendMessage(ChatColor.GOLD + "=========================");
    }
}
