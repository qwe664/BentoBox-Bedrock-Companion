package dev.qwe664.bbc.command;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.qwe664.bbc.util.LegacyText.send;

public class HelpCommand implements BaseCommand {

    private final BentoBoxBedrockCompanion plugin;

    public HelpCommand(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        showHelp(sender);
        return true;
    }

    private void showHelp(CommandSender sender) {

        send(sender, "§6========== BBC ==========");
        send(sender, "");

        send(sender, text(sender, "help.general", "§e一般指令"));
        send(sender, "§a/bbc");
        send(sender, text(sender, "help.bedrock-description", "§7基岩版：開啟 BBC 表單主選單"));
        send(sender, text(sender, "help.java-description", "§7Java 版：直接轉發到 /is"));
        send(sender, "");

        send(sender, "§a/bbc help");
        send(sender, text(sender, "help.help-description", "§7顯示此說明"));
        send(sender, "");

        send(sender, "§a/bbc item");
        send(sender, text(sender, "help.item-description", "§7補領選單物品（右鍵可開啟主選單）"));
        send(sender, "");

        // 只有具備開發者權限的人才看得到後面這段，
        // 一般玩家看到一堆 Debug 指令反而只會覺得困惑。
        if (sender instanceof Player player
                && plugin.getPermissionService().hasDeveloperPermission(player)) {

            send(sender, text(sender, "help.developer", "§e開發者指令"));
            send(sender, "§a/bbc debug");
            send(sender, text(sender, "help.debug-description", "§7顯示 Debug 子指令清單"));
            send(sender, "");

            send(sender, "§a/bbc debug plugins");
            send(sender, text(sender, "help.plugins-description", "§7查看插件載入狀態"));
            send(sender, "");

            send(sender, text(sender, "help.methods-command",
                    "§a/bbc debug methods <alias|類別名稱>"));
            send(sender, text(sender, "help.methods-description",
                    "§7Reflection 方法探索（可加 declared 只看自身宣告的方法）"));
            send(sender, "");
        }

        send(sender, text(sender, "help.more-features", "§8更多功能將於後續版本加入。"));
        send(sender, "§6=========================");
    }

    private String text(CommandSender sender, String key, String fallback) {
        return plugin.getLocaleService().get(sender, key, fallback);
    }
}
