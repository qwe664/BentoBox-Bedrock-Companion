package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev.qwe664.bbc.developer.reflection.ReflectionAliases;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.bentobox.managers.FlagsManager;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static dev.qwe664.bbc.util.LegacyText.send;

public class DebugCommand {

    private final BentoBoxBedrockCompanion plugin;

    public DebugCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {

        // /bbc debug
        if (args.length == 1) {
            showCommandList(sender);
            return true;
        }

        // /bbc debug plugins
        if (args[1].equalsIgnoreCase("plugins")) {
            showPluginStatus(sender);
            return true;
        }

        // /bbc debug methods
        if (args[1].equalsIgnoreCase("methods")) {

            if (args.length < 3) {
                send(sender, text(sender, "debug_command.usage", "§c用法："));
                send(sender, text(sender, "debug_command.methods-usage",
                        "§e/bbc debug methods <Alias 或完整類別名稱>"));
                send(sender, text(sender, "debug_command.declared-usage",
                        "§e/bbc debug methods declared <Alias 或完整類別名稱>"));
                send(sender, text(sender, "debug_command.examples", "§7例如："));
                send(sender, "§7/bbc debug methods flag");
                send(sender, "§7/bbc debug methods declared flag");
                send(sender, "§7/bbc debug methods user");
                send(sender, "§7/bbc debug methods declared user");
                return true;
            }

            boolean declaredOnly = false;
            String target;

            if (args[2].equalsIgnoreCase("declared")) {

                declaredOnly = true;

                if (args.length < 4) {
                    send(sender, text(sender, "debug_command.alias-required",
                            "§c請輸入 Alias 或完整類別名稱。"));
                    return true;
                }

                target = args[3];

            } else {

                target = args[2];
            }

            String className = ReflectionAliases.resolve(target);

            if (declaredOnly) {
                ReflectionUtil.printDeclaredMethods(className);
            } else {
                ReflectionUtil.printPublicMethods(className);
            }

            send(sender, text(sender, "debug_command.reflection-output",
                    "§a[BBC] 已將反射資訊輸出至主控台。"));

            return true;
        }

        // /bbc debug api
        if (args[1].equalsIgnoreCase("api")) {
            showApiInfo(sender);
            return true;
        }

        // /bbc debug island
        if (args[1].equalsIgnoreCase("island")) {
            showIslandInfo(sender);
            return true;
        }

        // /bbc debug flags
        if (args[1].equalsIgnoreCase("flags")) {
            showFlagsInfo(sender);
            return true;
        }

        // /bbc debug version
        if (args[1].equalsIgnoreCase("version")) {
            showVersionInfo(sender);
            return true;
        }

        // 尚未實作的子指令
        send(sender, text(sender, "debug_command.not-implemented",
                "§e[BBC] 此 Debug 功能尚未實作。"));
        return true;
    }

    private void showCommandList(CommandSender sender) {

        send(sender, text(sender, "debug_command.command-list-title",
                "§6===== BBC Debug ====="));
        send(sender, text(sender, "debug_command.available-subcommands",
                "§e可用子指令："));
        send(sender, "");

        send(sender, "§a/bbc debug plugins");
        send(sender, text(sender, "debug_command.plugins-description", "§7查看插件載入狀態"));
        send(sender, "");

        send(sender, "§a/bbc debug methods");
        send(sender, text(sender, "debug_command.methods-description", "§7Reflection 方法探索"));
        send(sender, text(sender, "debug_command.aliases", "§7Alias：flag、user"));
        send(sender, "");

        send(sender, "§a/bbc debug methods declared");
        send(sender, text(sender, "debug_command.declared-description",
                "§7只顯示類別自行宣告的 Public 方法"));
        send(sender, "");

        send(sender, "§a/bbc debug api");
        send(sender, text(sender, "debug_command.api-description",
                "§7Public API 探索：BentoBox 版本、已啟用玩法、附加模組清單"));
        send(sender, "");

        send(sender, "§a/bbc debug island");
        send(sender, text(sender, "debug_command.island-description",
                "§7Island API 探索：查看自己目前所在島嶼的詳細資料"));
        send(sender, "");

        send(sender, "§a/bbc debug flags");
        send(sender, text(sender, "debug_command.flags-description",
                "§7Protection Flag 探索：列出所有已註冊的保護旗標"));
        send(sender, "");

        send(sender, "§a/bbc debug version");
        send(sender, text(sender, "debug_command.version-description", "§7BBC 版本資訊"));

        send(sender, "§6=====================");
    }

    /**
     * 顯示插件載入狀態，DebugMenuForm「📦 插件資訊」按鈕也是呼叫這個方法，
     * 兩邊共用同一套邏輯，不重複寫。
     */
    public void showPluginStatus(CommandSender sender) {

        send(sender, text(sender, "debug_command.plugin-status-title",
                "§6===== BBC 插件狀態 ====="));

        send(sender, status(sender, "BBC", plugin));

        send(sender, status(sender, "BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        send(sender, status(sender, "Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        send(sender, status(sender, "Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        send(sender, status(sender, "PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        send(sender, "§6=============================");
    }

    private String status(CommandSender sender, String name, Plugin plugin) {

        if (plugin == null) {
            return "§c✘ " + name + ": "
                    + text(sender, "debug_command.not-installed", "未安裝");
        }

        return "§a✔ " + name + ": "
                + plugin.getPluginMeta().getVersion();
    }

    /**
     * /bbc debug api 與 DebugMenuForm「📚 BentoBox API」按鈕共用這個方法。
     *
     * 顯示 BentoBox 核心版本、目前所有已啟用的玩法（GameModeAddon）、
     * 以及所有已啟用的附加模組（Addon）清單，方便快速掌握伺服器上
     * BentoBox 生態系目前的狀態，不用一個一個 /plugins 慢慢找。
     */
    public void showApiInfo(CommandSender sender) {

        var bentoBox = plugin.getBentoBoxService().getBentoBox();

        send(sender, text(sender, "debug_command.api-title", "§6===== BentoBox API ====="));

        send(sender, text(sender, "debug_command.core-version-label", "§e核心版本：")
                + "§f" + bentoBox.getPluginMeta().getVersion());

        send(sender, "");
        send(sender, text(sender, "debug_command.enabled-game-modes", "§e已啟用玩法："));

        plugin.getBentoBoxService().getAvailableGameModes().forEach(choice ->
                send(sender, "§a  - " + choice.name()
                        + "§7 (/" + choice.label() + ")"));

        send(sender, "");
        send(sender, text(sender, "debug_command.enabled-addons", "§e已啟用附加模組："));

        AddonsManager addonsManager = bentoBox.getAddonsManager();
        List<Addon> enabledAddons = addonsManager.getEnabledAddons();

        enabledAddons.stream()
                .sorted(Comparator.comparing(addon -> addon.getDescription().getName()))
                .forEach(addon -> send(sender, "§a  - "
                        + addon.getDescription().getName()
                        + "§7 v" + addon.getDescription().getVersion()));

        send(sender, "§6=========================");
    }

    /**
     * /bbc debug island。
     *
     * 只顯示「發送指令的玩家」目前所在世界的島嶼資料，主控台（非玩家）執行時
     * 沒有「目前所在世界」的概念，直接提示要用玩家身分執行。
     */
    public void showIslandInfo(CommandSender sender) {

        if (!(sender instanceof Player player)) {
            send(sender, text(sender, "debug_command.player-only",
                    "§c此指令只能由玩家執行（需要知道你目前所在的世界）。"));
            return;
        }

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        send(sender, text(sender, "debug_command.island-title", "§6===== Island API ====="));

        if (island == null) {
            send(sender, text(sender, "debug_command.no-island",
                    "§c你目前所在的世界沒有島嶼資料。"));
            send(sender, "§6=======================");
            return;
        }

        UUID ownerUuid = island.getOwner();
        String ownerName = ownerUuid == null
                ? text(sender, "placeholder.unavailable", "（無）")
                : plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);

        send(sender, text(sender, "debug_command.owner-label", "§e島主：") + "§f" + ownerName);
        send(sender, text(sender, "debug_command.member-count-label", "§e成員數：") + "§f" + island.getMemberSet().size());
        send(sender, text(sender, "debug_command.world-label", "§e世界：") + "§f" + island.getWorld().getName());
        send(sender, text(sender, "debug_command.center-label", "§e中心座標：") + "§f"
                + island.getCenter().getBlockX() + ", "
                + island.getCenter().getBlockY() + ", "
                + island.getCenter().getBlockZ());
        String blocksSuffix = text(sender, "debug_command.blocks-suffix", " 格");
        send(sender, text(sender, "debug_command.protection-range-label", "§e保護範圍：") + "§f"
                + island.getProtectionRange() + blocksSuffix);
        send(sender, text(sender, "debug_command.island-range-label", "§e島嶼範圍：") + "§f"
                + island.getRange() + blocksSuffix);
        send(sender, text(sender, "debug_command.has-team-label", "§e是否有隊伍：") + "§f"
                + (island.hasTeam()
                ? text(sender, "debug_command.yes-value", "是")
                : text(sender, "debug_command.no-value", "否")));

        send(sender, "§6=======================");
    }

    /**
     * /bbc debug flags。
     *
     * 列出 BentoBox 目前所有已註冊的保護旗標（含官方核心跟各附加模組
     * 自行註冊的），依類型分組顯示，方便確認某個旗標是否真的有註冊成功、
     * 或某個附加模組裝了之後有沒有多出新的旗標。
     */
    public void showFlagsInfo(CommandSender sender) {

        FlagsManager flagsManager = plugin.getBentoBoxService().getBentoBox().getFlagsManager();
        List<Flag> flags = flagsManager.getFlags();

        send(sender, text(sender, "debug_command.flags-title",
                "§6===== Protection Flags ====="));
        send(sender, text(sender, "debug_command.registered-flags",
                "§e共 {count} 個已註冊旗標").replace("{count}", String.valueOf(flags.size())));
        send(sender, "");

        flags.stream()
                .sorted(Comparator.comparing(Flag::getID))
                .forEach(flag -> send(sender, "§a  - "
                        + flag.getID() + "§7 (" + flag.getType() + ")"));

        send(sender, "§6============================");
    }

    /**
     * /bbc debug version。
     */
    public void showVersionInfo(CommandSender sender) {

        send(sender, text(sender, "debug_command.version-title", "§6===== BBC Version ====="));
        send(sender, text(sender, "debug_command.bbc-version-label", "§eBentoBox Bedrock Companion：")
                + "§f" + plugin.getPluginMeta().getVersion());
        send(sender, text(sender, "debug_command.bentobox-version-label", "§eBentoBox：")
                + "§f" + plugin.getBentoBoxService().getBentoBox().getPluginMeta().getVersion());
        send(sender, text(sender, "debug_command.server-label", "§e伺服器：")
                + "§f" + Bukkit.getName() + " " + Bukkit.getVersion());
        send(sender, text(sender, "debug_command.minecraft-label", "§eMinecraft：")
                + "§f" + Bukkit.getMinecraftVersion());
        send(sender, "§6========================");
    }

    private String text(CommandSender sender, String key, String fallback) {
        return plugin.getLocaleService().get(sender, key, fallback);
    }
}
