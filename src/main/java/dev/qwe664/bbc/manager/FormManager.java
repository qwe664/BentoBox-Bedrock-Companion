package dev.qwe664.bbc.manager;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.form.MainMenuForm;
import org.bukkit.entity.Player;

public class FormManager {

    private final BentoBoxBedrockCompanion plugin;
    private final MainMenuForm mainMenuForm;

    public FormManager(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        this.mainMenuForm = new MainMenuForm(plugin);
    }

    public BentoBoxBedrockCompanion getPlugin() {
        return plugin;
    }

    public void showMainMenu(Player player) {
        mainMenuForm.open(player);
    }
}
