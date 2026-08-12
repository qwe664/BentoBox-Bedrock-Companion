package dev.qwe664.bbc;

import dev.qwe664.bbc.command.BBCCommand;
import dev.qwe664.bbc.service.CommandService;
import dev.qwe664.bbc.hook.FloodgateHook;
import dev.qwe664.bbc.hook.WarpsHook;
import dev.qwe664.bbc.hook.VisitHook;
import dev.qwe664.bbc.hook.ChallengesHook;
import dev.qwe664.bbc.hook.BankHook;
import dev.qwe664.bbc.hook.VaultHook;
import dev.qwe664.bbc.placeholder.BBCExpansion;
import dev.qwe664.bbc.listener.PlayerJoinListener;
import dev.qwe664.bbc.listener.CommandListener; // <-- 1. 記得引入剛剛寫好的攔截器
import dev.qwe664.bbc.listener.MenuItemListener;
import dev.qwe664.bbc.manager.FormManager;
import dev.qwe664.bbc.menu.MenuRegistry;
import dev.qwe664.bbc.service.BentoBoxService;
import dev.qwe664.bbc.service.ConfigService;
import dev.qwe664.bbc.service.LocaleService;
import dev.qwe664.bbc.service.LuckPermsService;
import dev.qwe664.bbc.service.PermissionService;
import dev.qwe664.bbc.menu.MenuLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBoxBedrockCompanion extends JavaPlugin {

    private FloodgateHook floodgateHook;
    private WarpsHook warpsHook;
    private VisitHook visitHook;
    private ChallengesHook challengesHook;
    private BankHook bankHook;
    private VaultHook vaultHook;
    private FormManager formManager;

    private MenuRegistry menuRegistry;
    private PermissionService permissionService;
    private CommandService commandService;
    private BentoBoxService bentoBoxService;
    private LuckPermsService luckPermsService;
    private ConfigService configService;
    private LocaleService localeService;

    @Override
    public void onEnable() {

        // 設定檔最先載入，其他服務（表單、hook）都可能需要讀取它。
        configService = new ConfigService(this);
        localeService = new LocaleService(this);

        floodgateHook = new FloodgateHook();
        warpsHook = new WarpsHook();
        visitHook = new VisitHook();
        challengesHook = new ChallengesHook();
        bankHook = new BankHook();
        vaultHook = new VaultHook();

        menuRegistry = new MenuRegistry();
        permissionService = new PermissionService();
        commandService = new CommandService(this);
        bentoBoxService = new BentoBoxService(this);
        luckPermsService = new LuckPermsService();
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

        // 3. 註冊選單物品的右鍵監聽器
        getServer().getPluginManager().registerEvents(
                new MenuItemListener(this),
                this
        );

        if (getCommand("bbc") != null) {
            getCommand("bbc").setExecutor(new BBCCommand(this));
        }

        if (luckPermsService.isEnabled()) {
            getLogger().info("已偵測到 LuckPerms，權限組資訊功能已啟用。");
        } else {
            getLogger().info("未偵測到 LuckPerms，權限組資訊功能將不會顯示（不影響其他功能）。");
        }

        if (warpsHook.isAvailable()) {
            getLogger().info("已偵測到 Warps 附加模組，傳送點功能已啟用。");
        } else {
            getLogger().info("未偵測到 Warps 附加模組，傳送點功能將不會顯示（不影響其他功能）。");
        }

        if (challengesHook.isAvailable()) {
            getLogger().info("已偵測到 Challenges 附加模組，挑戰功能已啟用。");
        } else {
            getLogger().info("未偵測到 Challenges 附加模組，挑戰功能將不會顯示（不影響其他功能）。");
        }

        if (visitHook.isAvailable()) {
            getLogger().info("已偵測到 Visit 附加模組，拜訪島嶼功能已啟用。");
        } else {
            getLogger().info("未偵測到 Visit 附加模組，拜訪島嶼功能將不會顯示（不影響其他功能）。");
        }

        if (bankHook.isAvailable()) {
            getLogger().info("已偵測到 Bank 附加模組，島嶼餘額變數已啟用。");
        } else {
            getLogger().info("未偵測到 Bank 附加模組，%bbc_island_money% 變數固定回傳 0（不影響其他功能）。");
        }

        vaultHook.setup(getServer().getServicesManager());
        if (vaultHook.isAvailable()) {
            getLogger().info("已偵測到 Vault 經濟系統（" + vaultHook.getEconomyName() + "），玩家錢包功能已啟用。");
        } else {
            getLogger().info("未偵測到 Vault 經濟系統，%bbc_player_money% 變數固定回傳 0、存提款功能將不會顯示（不影響其他功能）。");
        }

        // PlaceholderAPI 是軟依賴，未安裝時完全跳過註冊，不影響其他功能。
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new BBCExpansion(this).register();
            getLogger().info("已偵測到 PlaceholderAPI，已註冊 %bbc_*% 變數。");
        } else {
            getLogger().info("未偵測到 PlaceholderAPI，%bbc_*% 變數將無法使用（不影響其他功能）。");
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

    public WarpsHook getWarpsHook() {
        return warpsHook;
    }

    public VisitHook getVisitHook() {
        return visitHook;
    }

    public ChallengesHook getChallengesHook() {
        return challengesHook;
    }

    public BankHook getBankHook() {
        return bankHook;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
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

    public BentoBoxService getBentoBoxService() {
        return bentoBoxService;
    }

    public LuckPermsService getLuckPermsService() {
        return luckPermsService;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    public LocaleService getLocaleService() {
        return localeService;
    }
}
