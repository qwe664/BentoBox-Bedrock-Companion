package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                sender.sendMessage(text(sender, "debug_command.usage", ChatColor.RED + "用法："));
                sender.sendMessage(text(sender, "debug_command.methods-usage",
                        ChatColor.YELLOW + "/bbc debug methods <Alias 或完整類別名稱>"));
                sender.sendMessage(text(sender, "debug_command.declared-usage",
                        ChatColor.YELLOW + "/bbc debug methods declared <Alias 或完整類別名稱>"));
                sender.sendMessage(text(sender, "debug_command.examples", ChatColor.GRAY + "例如："));
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods flag");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods declared flag");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods user");
                sender.sendMessage(ChatColor.GRAY + "/bbc debug methods declared user");
                return true;
            }

            boolean declaredOnly = false;
            String target;

            if (args[2].equalsIgnoreCase("declared")) {

                declaredOnly = true;

                if (args.length < 4) {
                    sender.sendMessage(text(sender, "debug_command.alias-required",
                            ChatColor.RED + "請輸入 Alias 或完整類別名稱。"));
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

            sender.sendMessage(text(sender, "debug_command.reflection-output",
                    ChatColor.GREEN + "[BBC] 已將反射資訊輸出至主控台。"));

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
        sender.sendMessage(text(sender, "debug_command.not-implemented",
                ChatColor.YELLOW + "[BBC] 此 Debug 功能尚未實作。"));
        return true;
    }

    private void showCommandList(CommandSender sender) {

        sender.sendMessage(text(sender, "debug_command.command-list-title",
                ChatColor.GOLD + "===== BBC Debug ====="));
        sender.sendMessage(text(sender, "debug_command.available-subcommands",
                ChatColor.YELLOW + "可用子指令："));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug plugins");
        sender.sendMessage(text(sender, "debug_command.plugins-description", ChatColor.GRAY + "查看插件載入狀態"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods");
        sender.sendMessage(text(sender, "debug_command.methods-description", ChatColor.GRAY + "Reflection 方法探索"));
        sender.sendMessage(text(sender, "debug_command.aliases", ChatColor.GRAY + "Alias：flag、user"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods declared");
        sender.sendMessage(text(sender, "debug_command.declared-description",
                ChatColor.GRAY + "只顯示類別自行宣告的 Public 方法"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug api");
        sender.sendMessage(text(sender, "debug_command.api-description",
                ChatColor.GRAY + "Public API 探索：BentoBox 版本、已啟用玩法、附加模組清單"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug island");
        sender.sendMessage(text(sender, "debug_command.island-description",
                ChatColor.GRAY + "Island API 探索：查看自己目前所在島嶼的詳細資料"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug flags");
        sender.sendMessage(text(sender, "debug_command.flags-description",
                ChatColor.GRAY + "Protection Flag 探索：列出所有已註冊的保護旗標"));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug version");
        sender.sendMessage(text(sender, "debug_command.version-description", ChatColor.GRAY + "BBC 版本資訊"));

        sender.sendMessage(ChatColor.GOLD + "=====================");
    }

    /**
     * 顯示插件載入狀態，DebugMenuForm「📦 插件資訊」按鈕也是呼叫這個方法，
     * 兩邊共用同一套邏輯，不重複寫。
     */
    public void showPluginStatus(CommandSender sender) {

        sender.sendMessage(text(sender, "debug_command.plugin-status-title",
                ChatColor.GOLD + "===== BBC 插件狀態 ====="));

        sender.sendMessage(status(sender, "BBC", plugin));

        sender.sendMessage(status(sender, "BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        sender.sendMessage(status(sender, "Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        sender.sendMessage(status(sender, "Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        sender.sendMessage(status(sender, "PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        sender.sendMessage(ChatColor.GOLD + "=============================");
    }

    private String status(CommandSender sender, String name, Plugin plugin) {

        if (plugin == null) {
            return ChatColor.RED + "✘ " + name + ": "
                    + text(sender, "debug_command.not-installed", "未安裝");
        }

        return ChatColor.GREEN + "✔ " + name + ": "
                + plugin.getDescription().getVersion();
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

        sender.sendMessage(text(sender, "debug_command.api-title", ChatColor.GOLD + "===== BentoBox API ====="));

        sender.sendMessage(text(sender, "debug_command.core-version-label", ChatColor.YELLOW + "核心版本：")
                + ChatColor.WHITE + bentoBox.getDescription().getVersion());

        sender.sendMessage("");
        sender.sendMessage(text(sender, "debug_command.enabled-game-modes", ChatColor.YELLOW + "已啟用玩法："));

        plugin.getBentoBoxService().getAvailableGameModes().forEach(choice ->
                sender.sendMessage(ChatColor.GREEN + "  - " + choice.name()
                        + ChatColor.GRAY + " (/" + choice.label() + ")"));

        sender.sendMessage("");
        sender.sendMessage(text(sender, "debug_command.enabled-addons", ChatColor.YELLOW + "已啟用附加模組："));

        AddonsManager addonsManager = bentoBox.getAddonsManager();
        List<Addon> enabledAddons = addonsManager.getEnabledAddons();

        enabledAddons.stream()
                .sorted(Comparator.comparing(addon -> addon.getDescription().getName()))
                .forEach(addon -> sender.sendMessage(ChatColor.GREEN + "  - "
                        + addon.getDescription().getName()
                        + ChatColor.GRAY + " v" + addon.getDescription().getVersion()));

        sender.sendMessage(ChatColor.GOLD + "=========================");
    }

    /**
     * /bbc debug island。
     *
     * 只顯示「發送指令的玩家」目前所在世界的島嶼資料，主控台（非玩家）執行時
     * 沒有「目前所在世界」的概念，直接提示要用玩家身分執行。
     */
    public void showIslandInfo(CommandSender sender) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(text(sender, "debug_command.player-only",
                    ChatColor.RED + "此指令只能由玩家執行（需要知道你目前所在的世界）。"));
            return;
        }

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        sender.sendMessage(text(sender, "debug_command.island-title", ChatColor.GOLD + "===== Island API ====="));

        if (island == null) {
            sender.sendMessage(text(sender, "debug_command.no-island",
                    ChatColor.RED + "你目前所在的世界沒有島嶼資料。"));
            sender.sendMessage(ChatColor.GOLD + "=======================");
            return;
        }

        UUID ownerUuid = island.getOwner();
        String ownerName = ownerUuid == null
                ? text(sender, "placeholder.unavailable", "（無）")
                : plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);

        sender.sendMessage(text(sender, "debug_command.owner-label", ChatColor.YELLOW + "島主：") + ChatColor.WHITE + ownerName);
        sender.sendMessage(text(sender, "debug_command.member-count-label", ChatColor.YELLOW + "成員數：") + ChatColor.WHITE + island.getMemberSet().size());
        sender.sendMessage(text(sender, "debug_command.world-label", ChatColor.YELLOW + "世界：") + ChatColor.WHITE + island.getWorld().getName());
        sender.sendMessage(text(sender, "debug_command.center-label", ChatColor.YELLOW + "中心座標：") + ChatColor.WHITE
                + island.getCenter().getBlockX() + ", "
                + island.getCenter().getBlockY() + ", "
                + island.getCenter().getBlockZ());
        String blocksSuffix = text(sender, "debug_command.blocks-suffix", " 格");
        sender.sendMessage(text(sender, "debug_command.protection-range-label", ChatColor.YELLOW + "保護範圍：") + ChatColor.WHITE
                + island.getProtectionRange() + blocksSuffix);
        sender.sendMessage(text(sender, "debug_command.island-range-label", ChatColor.YELLOW + "島嶼範圍：") + ChatColor.WHITE
                + island.getRange() + blocksSuffix);
        sender.sendMessage(text(sender, "debug_command.has-team-label", ChatColor.YELLOW + "是否有隊伍：") + ChatColor.WHITE
                + (island.hasTeam()
                ? text(sender, "debug_command.yes-value", "是")
                : text(sender, "debug_command.no-value", "否")));

        sender.sendMessage(ChatColor.GOLD + "=======================");
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

        sender.sendMessage(text(sender, "debug_command.flags-title",
                ChatColor.GOLD + "===== Protection Flags ====="));
        sender.sendMessage(text(sender, "debug_command.registered-flags",
                ChatColor.YELLOW + "共 {count} 個已註冊旗標").replace("{count}", String.valueOf(flags.size())));
        sender.sendMessage("");

        flags.stream()
                .sorted(Comparator.comparing(Flag::getID))
                .forEach(flag -> sender.sendMessage(ChatColor.GREEN + "  - "
                        + flag.getID() + ChatColor.GRAY + " (" + flag.getType() + ")"));

        sender.sendMessage(ChatColor.GOLD + "============================");
    }

    /**
     * /bbc debug version。
     */
    public void showVersionInfo(CommandSender sender) {

        sender.sendMessage(text(sender, "debug_command.version-title", ChatColor.GOLD + "===== BBC Version ====="));
        sender.sendMessage(text(sender, "debug_command.bbc-version-label", ChatColor.YELLOW + "BentoBox Bedrock Companion：")
                + ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(text(sender, "debug_command.bentobox-version-label", ChatColor.YELLOW + "BentoBox：")
                + ChatColor.WHITE + plugin.getBentoBoxService().getBentoBox().getDescription().getVersion());
        sender.sendMessage(text(sender, "debug_command.server-label", ChatColor.YELLOW + "伺服器：")
                + ChatColor.WHITE + Bukkit.getName() + " " + Bukkit.getVersion());
        sender.sendMessage(text(sender, "debug_command.minecraft-label", ChatColor.YELLOW + "Minecraft：")
                + ChatColor.WHITE + Bukkit.getMinecraftVersion());
        sender.sendMessage(ChatColor.GOLD + "========================");
    }

    private String text(CommandSender sender, String key, String fallback) {
        return plugin.getLocaleService().get(sender, key, fallback);
    }
}
