package dev.qwe664.bbc.manager;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.form.AdminMenuForm;
import dev.qwe664.bbc.form.DebugMenuForm;
import dev.qwe664.bbc.form.IslandMenuForm;
import dev.qwe664.bbc.form.MainMenuForm;
import dev.qwe664.bbc.form.SettingsMenuForm;
import dev.qwe664.bbc.form.ProtectionMenuForm;
import org.bukkit.entity.Player;

public class FormManager {

    private final MainMenuForm mainMenuForm;
    private final IslandMenuForm islandMenuForm;
    private final DebugMenuForm debugMenuForm;
    private final AdminMenuForm adminMenuForm;
    private final SettingsMenuForm settingsMenuForm;
    private final ProtectionMenuForm protectionMenuForm;

    public FormManager(BentoBoxBedrockCompanion plugin) {
        this.mainMenuForm = new MainMenuForm(plugin);
        this.islandMenuForm = new IslandMenuForm(plugin);
        this.debugMenuForm = new DebugMenuForm(plugin);
        this.adminMenuForm = new AdminMenuForm(plugin);
        this.settingsMenuForm = new SettingsMenuForm(plugin);
        this.protectionMenuForm = new ProtectionMenuForm(plugin);
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

    public void openSettingsMenu(Player player) {
        settingsMenuForm.open(player);
    }

    public void openProtectionMenu(Player player) {
        protectionMenuForm.open(player);
    }
}
