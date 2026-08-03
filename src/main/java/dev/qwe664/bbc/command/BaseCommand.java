package dev.qwe664.bbc.command;

import org.bukkit.command.CommandSender;

public interface BaseCommand {

    boolean execute(CommandSender sender, String[] args);

}
