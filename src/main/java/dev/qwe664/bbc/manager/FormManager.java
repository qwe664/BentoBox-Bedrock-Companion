package dev.qwe664.bbc.manager;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.form.AdminMenuForm;
import dev.qwe664.bbc.form.DebugMenuForm;
import dev.qwe664.bbc.form.IslandMenuForm;
import dev.qwe664.bbc.form.MainMenuForm;
import org.bukkit.entity.Player;

public class FormManager {

    private final MainMenuForm mainMenuForm;
    private final IslandMenuForm islandMenuForm;
    private final DebugMenuForm debugMenuForm;
    private final AdminMenuForm adminMenuForm;

    public FormManager(BentoBoxBedrockCompanion plugin) {
        this.mainMenuForm = new MainMenuForm(plugin);
        this.islandMenuForm = new IslandMenuForm(plugin);
        this.debugMenuForm = new DebugMenuForm(plugin);
        this.adminMenuForm = new AdminMenuForm(plugin);
    }

    public void openMainMenu(Player player) {
        mainMenuForm.open(player);
    }

    public void openIslandMenu(Player player) {
        islandMenuForm.open(player);
    }

    public void openDebugMenu(Player player) {
        debugMenuForm.open(player);
    }

    public void openAdminMenu(Player player) {
        adminMenuForm.open(player);
    }
}
