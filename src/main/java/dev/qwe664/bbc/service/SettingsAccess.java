package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;

/** Permission checks shared by both Bedrock settings forms. */
public final class SettingsAccess {
    private SettingsAccess() {}

    public static boolean canOpen(Player player, Island island) {
        String prefix = BentoBox.getInstance().getIWM().getPermissionPrefix(island.getWorld());
        return player.hasPermission(prefix + "island.settings");
    }

    public static boolean canEdit(Player player, Island island, Flag flag) {
        var worlds = BentoBox.getInstance().getIWM();
        String prefix = worlds.getPermissionPrefix(island.getWorld());
        boolean admin = player.isOp() || player.hasPermission(prefix + "admin.settings");
        boolean visible = player.isOp() || !worlds.getHiddenFlags(island.getWorld()).contains(flag.getID());
        boolean applicable = flag.getGameModes().isEmpty()
                || worlds.getAddon(island.getWorld()).map(flag.getGameModes()::contains).orElse(false);
        return canOpen(player, island) && visible && applicable && SettingsPermissionPolicy.permits(admin,
                island.isAllowed(User.getInstance(player), Flags.CHANGE_SETTINGS),
                player.hasPermission(prefix + "settings." + flag.getID())
                        || player.hasPermission(prefix + "settings.*"));
    }

    public static void deny(BentoBoxBedrockCompanion plugin, Player player) {
        player.sendMessage(plugin.getLocaleService().get(player, "common.settings-update-denied",
                "§c設定未更新：權限不足、島嶼已失效、設定已變動或仍在冷卻中。請重新開啟表單。"));
    }
}
