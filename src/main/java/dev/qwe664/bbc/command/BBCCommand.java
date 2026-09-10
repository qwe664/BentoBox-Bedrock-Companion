package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.MenuItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.qwe664.bbc.util.LegacyText.send;

public class BBCCommand implements CommandExecutor {

    private final BentoBoxBedrockCompanion plugin;
    private final HelpCommand helpCommand;
    private final DebugCommand debugCommand;

    public BBCCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        this.helpCommand = new HelpCommand(plugin);
        this.debugCommand = new DebugCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /bbc help
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            return helpCommand.execute(sender, args);
        }

        // /bbc debug
        if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {
            if (!(sender instanceof Player player) || plugin.getPermissionService().hasDeveloperPermission(player)) {
                return debugCommand.execute(sender, args);
            }
            send(sender, plugin.getLocaleService().get(sender, "command.no-permission",
                    "§c[BBC] 你沒有權限使用這個指令。"));
            return true;
        }

        // /bbc item — 補領選單物品，主要給更新前就已加入的舊玩家使用
        // （PlayerJoinListener 只在「加入伺服器」那一刻補發，已經在線的人不會觸發）。
        if (args.length > 0 && args[0].equalsIgnoreCase("item")) {
            if (!(sender instanceof Player player)) {
                send(sender, plugin.getLocaleService().get(sender, "command.player-only",
                        "§e[BBC] 這個指令僅限玩家使用。"));
                return true;
            }
            player.getInventory().addItem(MenuItem.create(plugin));
            send(player, plugin.getLocaleService().get(player, "command.item-received",
                    "§a[BBC] 已給予選單物品。"));
            return true;
        }

        // /bbc
        if (!(sender instanceof Player player)) {
            send(sender, plugin.getLocaleService().get(sender, "command.menu-player-only",
                    "§e[BBC] 主選單僅限玩家使用。"));
            send(sender, plugin.getLocaleService().get(sender, "command.available",
                    "§7可使用："));
            send(sender, "§7  /bbc help");
            send(sender, "§7  /bbc debug");
            return true;
        }

        // 基岩版玩家開啟 BBC 的表單主選單；
        // Java 版玩家不需要繞這一層，BentoBox 本身就是為 Java 版設計的，
        // 直接轉發到該世界對應玩法的指令即可（動態查詢，不寫死 "is"，
        // 沒有島嶼時 BentoBox 會自動跳出建立島嶼的原生 GUI）。
        if (plugin.getFloodgateHook().isBedrock(player)) {
            plugin.getFormManager().openMainMenu(player);
        } else {
            plugin.getBentoBoxService().getPlayerCommandLabel(player).ifPresentOrElse(
                    gamemodeLabel -> plugin.getCommandService().execute(player, gamemodeLabel),
                    () -> {
                        // 站在主城這類不屬於任何玩法的世界，光看目前世界猜不出要去哪個玩法，
                        // 基岩版表單只有 Floodgate 玩家能開，Java 玩家這裡改用聊天訊息列出選項。
                        send(player, plugin.getLocaleService().get(player, "command.choose-game-mode",
                                "§e[BBC] 你目前不在任何空島世界裡，請選擇要前往的玩法："));
                        plugin.getBentoBoxService().getAvailableGameModes().forEach(choice ->
                                send(player, "§7  /" + choice.label() + " §7(" + choice.name() + ")"));
                    }
            );
        }

        return true;
    }
}
