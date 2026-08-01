package dev.qwe664.bbc.util;

import org.bukkit.Bukkit;

public final class ConsoleLogger {

    private static final String PREFIX = "[BBC]";

    private static final String DEBUG = "[除錯]";
    private static final String API = "[API]";
    private static final String REFLECTION = "[反射]";
    private static final String ISLAND = "[島嶼]";
    private static final String FLAG = "[旗標]";
    private static final String WARNING = "[警告]";
    private static final String ERROR = "[錯誤]";

    private ConsoleLogger() {
    }

    public static void debug(String message) {
        Bukkit.getLogger().info(PREFIX + DEBUG + " " + message);
    }

    public static void api(String message) {
        Bukkit.getLogger().info(PREFIX + API + " " + message);
    }

    public static void reflection(String message) {
        Bukkit.getLogger().info(PREFIX + REFLECTION + " " + message);
    }

    public static void island(String message) {
        Bukkit.getLogger().info(PREFIX + ISLAND + " " + message);
    }

    public static void flag(String message) {
        Bukkit.getLogger().info(PREFIX + FLAG + " " + message);
    }

    public static void warning(String message) {
        Bukkit.getLogger().warning(PREFIX + WARNING + " " + message);
    }

    public static void error(String message) {
        Bukkit.getLogger().severe(PREFIX + ERROR + " " + message);
    }
}
