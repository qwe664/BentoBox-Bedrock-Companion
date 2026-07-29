package dev.qwe664.bbc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BBCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用此指令。");
            return true;
        }

        player.sendMessage("§aBentoBox Bedrock Companion 測試成功！");
        return true;
    }
}
