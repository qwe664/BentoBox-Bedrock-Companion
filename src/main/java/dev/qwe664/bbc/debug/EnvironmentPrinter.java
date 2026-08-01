package dev.qwe664.bbc.debug;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

public final class EnvironmentPrinter {

    private EnvironmentPrinter() {
    }

    public static void print(BentoBoxBedrockCompanion plugin) {

        Logger logger = plugin.getLogger();

        logger.info("==================================================");
        logger.info("BBC Environment");
        logger.info("==================================================");

        logger.info("Server");
        logger.info("----------------------------------------");
        logger.info("Bukkit      : " + Bukkit.getName());
        logger.info("Version     : " + Bukkit.getVersion());
        logger.info("Minecraft   : " + Bukkit.getMinecraftVersion());

        logger.info("");

        logger.info("Java");
        logger.info("----------------------------------------");
        logger.info("Vendor      : " + System.getProperty("java.vendor"));
        logger.info("Version     : " + System.getProperty("java.version"));

        logger.info("");

        logger.info("Plugins");
        logger.info("----------------------------------------");

        Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .sorted(Comparator.comparing(Plugin::getName))
                .forEach(p -> logger.info(
                        String.format("%-20s %s",
                                p.getName(),
                                p.getDescription().getVersion())
                ));

        logger.info("==================================================");
    }
}
