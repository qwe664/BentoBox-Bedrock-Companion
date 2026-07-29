package dev.qwe664.bbc.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRegistry {

    private final List<MenuButton> buttons = new ArrayList<>();

    public void register(MenuButton button) {
        buttons.add(button);
    }

    public List<MenuButton> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public List<MenuButton> getButtons(MenuCategory category) {
        return buttons.stream()
                .filter(button -> button.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void clear() {
        buttons.clear();
    }
}
