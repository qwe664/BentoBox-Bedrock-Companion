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
                sender.sendMessage(ChatColor.RED + "用法：");
                sender.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods <Alias 或完整類別名稱>");
                sender.sendMessage(ChatColor.YELLOW
                        + "/bbc debug methods declared <Alias 或完整類別名稱>");
                sender.sendMessage(ChatColor.GRAY + "例如：");
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
                    sender.sendMessage(ChatColor.RED + "請輸入 Alias 或完整類別名稱。");
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

            sender.sendMessage(ChatColor.GREEN
                    + "[BBC] 已將反射資訊輸出至主控台。");

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
        sender.sendMessage(ChatColor.YELLOW + "[BBC] 此 Debug 功能尚未實作。");
        return true;
    }

    private void showCommandList(CommandSender sender) {

        sender.sendMessage(ChatColor.GOLD + "===== BBC Debug =====");
        sender.sendMessage(ChatColor.YELLOW + "可用子指令：");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug plugins");
        sender.sendMessage(ChatColor.GRAY + "查看插件載入狀態");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods");
        sender.sendMessage(ChatColor.GRAY + "Reflection 方法探索");
        sender.sendMessage(ChatColor.GRAY + "Alias：flag、user");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug methods declared");
        sender.sendMessage(ChatColor.GRAY + "只顯示類別自行宣告的 Public 方法");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug api");
        sender.sendMessage(ChatColor.GRAY + "Public API 探索：BentoBox 版本、已啟用玩法、附加模組清單");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug island");
        sender.sendMessage(ChatColor.GRAY + "Island API 探索：查看自己目前所在島嶼的詳細資料");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug flags");
        sender.sendMessage(ChatColor.GRAY + "Protection Flag 探索：列出所有已註冊的保護旗標");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.GREEN + "/bbc debug version");
        sender.sendMessage(ChatColor.GRAY + "BBC 版本資訊");

        sender.sendMessage(ChatColor.GOLD + "=====================");
    }

    /**
     * 顯示插件載入狀態，DebugMenuForm「📦 插件資訊」按鈕也是呼叫這個方法，
     * 兩邊共用同一套邏輯，不重複寫。
     */
    public void showPluginStatus(CommandSender sender) {

        sender.sendMessage(ChatColor.GOLD + "===== BBC Plugin Status =====");

        sender.sendMessage(status("BBC", plugin));

        sender.sendMessage(status("BentoBox",
                Bukkit.getPluginManager().getPlugin("BentoBox")));

        sender.sendMessage(status("Floodgate",
                Bukkit.getPluginManager().getPlugin("floodgate")));

        sender.sendMessage(status("Geyser",
                Bukkit.getPluginManager().getPlugin("Geyser-Spigot")));

        sender.sendMessage(status("PlaceholderAPI",
                Bukkit.getPluginManager().getPlugin("PlaceholderAPI")));

        sender.sendMessage(ChatColor.GOLD + "=============================");
    }

    private String status(String name, Plugin plugin) {

        if (plugin == null) {
            return ChatColor.RED + "✘ " + name + ": Not Installed";
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

        sender.sendMessage(ChatColor.GOLD + "===== BentoBox API =====");

        sender.sendMessage(ChatColor.YELLOW + "核心版本："
                + ChatColor.WHITE + bentoBox.getDescription().getVersion());

        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "已啟用玩法：");

        plugin.getBentoBoxService().getAvailableGameModes().forEach(choice ->
                sender.sendMessage(ChatColor.GREEN + "  - " + choice.name()
                        + ChatColor.GRAY + " (/" + choice.label() + ")"));

        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "已啟用附加模組：");

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
            sender.sendMessage(ChatColor.RED + "此指令只能由玩家執行（需要知道你目前所在的世界）。");
            return;
        }

        Island island = plugin.getBentoBoxService().getIslandsManager()
                .getIsland(player.getWorld(), player.getUniqueId());

        sender.sendMessage(ChatColor.GOLD + "===== Island API =====");

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "你目前所在的世界沒有島嶼資料。");
            sender.sendMessage(ChatColor.GOLD + "=======================");
            return;
        }

        UUID ownerUuid = island.getOwner();
        String ownerName = ownerUuid == null
                ? "（無）"
                : plugin.getBentoBoxService().getPlayersManager().getName(ownerUuid);

        sender.sendMessage(ChatColor.YELLOW + "島主：" + ChatColor.WHITE + ownerName);
        sender.sendMessage(ChatColor.YELLOW + "成員數：" + ChatColor.WHITE + island.getMemberSet().size());
        sender.sendMessage(ChatColor.YELLOW + "世界：" + ChatColor.WHITE + island.getWorld().getName());
        sender.sendMessage(ChatColor.YELLOW + "中心座標：" + ChatColor.WHITE
                + island.getCenter().getBlockX() + ", "
                + island.getCenter().getBlockY() + ", "
                + island.getCenter().getBlockZ());
        sender.sendMessage(ChatColor.YELLOW + "保護範圍：" + ChatColor.WHITE
                + island.getProtectionRange() + " 格");
        sender.sendMessage(ChatColor.YELLOW + "島嶼範圍：" + ChatColor.WHITE
                + island.getRange() + " 格");
        sender.sendMessage(ChatColor.YELLOW + "是否有隊伍：" + ChatColor.WHITE
                + (island.hasTeam() ? "是" : "否"));

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

        sender.sendMessage(ChatColor.GOLD + "===== Protection Flags =====");
        sender.sendMessage(ChatColor.YELLOW + "共 " + flags.size() + " 個已註冊旗標");
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

        sender.sendMessage(ChatColor.GOLD + "===== BBC Version =====");
        sender.sendMessage(ChatColor.YELLOW + "BentoBox Bedrock Companion："
                + ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "BentoBox："
                + ChatColor.WHITE + plugin.getBentoBoxService().getBentoBox().getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "伺服器："
                + ChatColor.WHITE + Bukkit.getName() + " " + Bukkit.getVersion());
        sender.sendMessage(ChatColor.YELLOW + "Minecraft："
                + ChatColor.WHITE + Bukkit.getMinecraftVersion());
        sender.sendMessage(ChatColor.GOLD + "========================");
    }
}
