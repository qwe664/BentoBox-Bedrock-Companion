package dev.qwe664.bbc;

import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBoxBedrockCompanion extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("BentoBox Bedrock Companion has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BentoBox Bedrock Companion has been disabled!");
    }
}
