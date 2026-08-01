package dev.qwe664.bbc.util;

import org.bukkit.Bukkit;

public final class ConsoleLogger {

    private static final String PREFIX = "[BBC]";

    private ConsoleLogger() {
    }

    public static void debug(String message) {
        Bukkit.getLogger().info(PREFIX + "[Debug] " + message);
    }

    public static void api(String message) {
        Bukkit.getLogger().info(PREFIX + "[API] " + message);
    }

    public static void reflection(String message) {
        Bukkit.getLogger().info(PREFIX + "[Reflection] " + message);
    }

    public static void island(String message) {
        Bukkit.getLogger().info(PREFIX + "[Island] " + message);
    }

    public static void flag(String message) {
        Bukkit.getLogger().info(PREFIX + "[Flag] " + message);
    }

    public static void warning(String message) {
        Bukkit.getLogger().warning(PREFIX + "[Warning] " + message);
    }

    public static void error(String message) {
        Bukkit.getLogger().severe(PREFIX + "[Error] " + message);
    }
}
