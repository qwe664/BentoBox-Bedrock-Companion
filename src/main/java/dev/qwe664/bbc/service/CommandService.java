package dev.qwe664.bbc.service;

import org.bukkit.entity.Player;

public class CommandService {

    public boolean execute(Player player, String command) {
        return player.performCommand(command);
    }
}
