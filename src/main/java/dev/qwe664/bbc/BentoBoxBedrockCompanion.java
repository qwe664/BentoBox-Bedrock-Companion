package dev.qwe664.bbc;

import dev.qwe664.bbc.hook.FloodgateHook;
import dev.qwe664.bbc.manager.FormManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBoxBedrockCompanion extends JavaPlugin {

    private FloodgateHook floodgateHook;
    private FormManager formManager;

    @Override
    public void onEnable() {
        floodgateHook = new FloodgateHook();
        formManager = new FormManager();

        getLogger().info("BentoBox Bedrock Companion has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BentoBox Bedrock Companion has been disabled!");
    }

    public FloodgateHook getFloodgateHook() {
        return floodgateHook;
    }

    public FormManager getFormManager() {
        return formManager;
    }
}
