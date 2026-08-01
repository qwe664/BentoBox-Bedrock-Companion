package dev.qwe664.bbc.developer.api;

import dev.qwe664.bbc.util.ConsoleLogger;

public final class ApiExplorer {

    private ApiExplorer() {
    }

    public static void explore(String className) {

        ConsoleLogger.api("========================================");
        ConsoleLogger.api("API Explorer");
        ConsoleLogger.api("");
        ConsoleLogger.api("目標類別：" + className);
        ConsoleLogger.api("");
        ConsoleLogger.api("功能開發中...");
        ConsoleLogger.api("========================================");
    }
}
