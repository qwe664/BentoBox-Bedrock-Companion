package dev.qwe664.bbc.menu;

public class MenuButton {

    private final String id;
    private final String title;
    private final MenuCategory category;
    private final String permission;
    private final MenuAction action;

    public MenuButton(
            String id,
            String title,
            MenuCategory category,
            String permission,
            MenuAction action
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.permission = permission;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public String getPermission() {
        return permission;
    }

}
