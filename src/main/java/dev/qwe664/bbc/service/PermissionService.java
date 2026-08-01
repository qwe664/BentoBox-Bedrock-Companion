package dev.qwe664.bbc.service;

import org.bukkit.entity.Player;

public class PermissionService {

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public boolean hasPlayerMenuPermission(Player player) {
        return hasPermission(player, "bbc.menu.player");
    }

    public boolean hasAdminMenuPermission(Player player) {
        return hasPermission(player, "bbc.menu.admin");
    }

    public boolean hasDebugMenuPermission(Player player) {
        return hasPermission(player, "bbc.menu.debug");
    }

    public boolean hasDeveloperPermission(Player player) {
        return hasPermission(player, "bbc.debug");
    }
}
