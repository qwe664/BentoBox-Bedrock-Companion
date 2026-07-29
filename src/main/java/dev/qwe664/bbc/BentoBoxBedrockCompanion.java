package dev.qwe664.bbc;

import dev.qwe664.bbc.command.BBCCommand;
import dev.qwe664.bbc.hook.FloodgateHook;
import dev.qwe664.bbc.listener.PlayerJoinListener;
import dev.qwe664.bbc.manager.FormManager;
import dev.qwe664.bbc.menu.MenuRegistry;
import dev.qwe664.bbc.service.PermissionService;
import dev.qwe664.bbc.menu.MenuLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBoxBedrockCompanion extends JavaPlugin {

    private FloodgateHook floodgateHook;
    private FormManager formManager;

    private MenuRegistry menuRegistry;
    private PermissionService permissionService;

    @Override
    public void onEnable() {

        floodgateHook = new FloodgateHook();

        menuRegistry = new MenuRegistry();
        permissionService = new PermissionService();
        new MenuLoader(menuRegistry).load();
        formManager = new FormManager(this);

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(this),
                this
        );

        if (getCommand("bbc") != null) {
            getCommand("bbc").setExecutor(new BBCCommand(this));
        }

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

    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }
}
