package dev.qwe664.bbc.command;

import org.bukkit.command.CommandSender;

/**
 * BBC Command 統一介面。
 *
 * 所有子指令皆應實作此介面。
 */
public interface BaseCommand {

    /**
     * 執行子指令。
     *
     * @param sender 指令發送者
     * @param args   完整指令參數
     * @return 是否成功處理指令
     */
    boolean execute(CommandSender sender, String[] args);
}
