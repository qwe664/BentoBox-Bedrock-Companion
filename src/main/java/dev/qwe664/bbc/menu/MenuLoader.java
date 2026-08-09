package dev.qwe664.bbc.menu;

public class MenuLoader {

    private final MenuRegistry menuRegistry;

    public MenuLoader(MenuRegistry menuRegistry) {
        this.menuRegistry = menuRegistry;
    }

    public void load() {

        menuRegistry.clear();

        // 玩家功能
        menuRegistry.register(new MenuButton(
                "island",
                "🏝 我的島嶼",
                MenuCategory.PLAYER,
                "bbc.menu.island"
        ));

        // 回大廳／出生點，不限身分組，所有玩家都能用
        menuRegistry.register(new MenuButton(
                "lobby",
                "🚪 回大廳",
                MenuCategory.PLAYER,
                null
        ));

        // 管理工具
        menuRegistry.register(new MenuButton(
                "admin",
                "👮 管理工具",
                MenuCategory.ADMIN,
                "bbc.menu.admin"
        ));

        // 開發工具
        menuRegistry.register(new MenuButton(
                "debug",
                "🛠 開發工具",
                MenuCategory.DEBUG,
                "bbc.menu.debug"
        ));
    }
}
