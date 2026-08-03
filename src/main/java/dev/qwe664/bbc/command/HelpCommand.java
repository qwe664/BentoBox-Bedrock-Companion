package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
        sender.sendMessage(ChatColor.GRAY + "開啟 BBC 主選單");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc help");
        sender.sendMessage(ChatColor.GRAY + "顯示此說明");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.YELLOW + "Developer");
        sender.sendMessage(ChatColor.GREEN + "/bbc debug");
        sender.sendMessage(ChatColor.GRAY + "Developer Tools");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.DARK_GRAY + "更多功能將於後續版本加入。");
        sender.sendMessage(ChatColor.GOLD + "=========================");
    }
}
