package dev.qwe664.bbc;

import dev.qwe664.bbc.command.BBCCommand;
import dev.qwe664.bbc.service.CommandService;
import dev.qwe664.bbc.hook.FloodgateHook;
import dev.qwe664.bbc.listener.PlayerJoinListener;
import dev.qwe664.bbc.listener.CommandListener; // <-- 1. 記得引入剛剛寫好的攔截器
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
    private CommandService commandService;

    @Override
    public void onEnable() {

        floodgateHook = new FloodgateHook();

        menuRegistry = new MenuRegistry();
        permissionService = new PermissionService();
        commandService = new CommandService(this);
        new MenuLoader(menuRegistry).load();
        formManager = new FormManager(this);

        // 註冊玩家加入監聽器
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(this),
                this
        );

        // 2. 註冊我們的指令攔截監聽器，讓 /is settings 可以被攔截
        getServer().getPluginManager().registerEvents(
                new CommandListener(this),
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

    public CommandService getCommandService() {
        return commandService;
    }
}
