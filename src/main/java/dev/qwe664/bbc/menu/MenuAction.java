package dev.qwe664.bbc.menu;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface MenuAction {

    void execute(Player player);

}
