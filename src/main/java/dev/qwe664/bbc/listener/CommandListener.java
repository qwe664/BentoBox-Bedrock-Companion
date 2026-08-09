package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public CommandListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        String[] parts = event.getMessage().substring(1).split(" ");

        if (parts.length < 2 || !parts[1].equalsIgnoreCase("settings")) {
            return;
        }

        // BentoBox 的玩法指令（"ob"、"ch" 等）是全域指令，跟玩家「目前站在哪個世界」無關
        // （例如站在主城也能打 /ob settings），所以不能只看玩家目前所在世界反推該比對哪個別名，
        // 而是要跟伺服器上「所有已啟用玩法」的指令別名逐一比對，看玩家打的指令屬於哪一個玩法。
        boolean isSettingsCommand = plugin.getBentoBoxService().getAvailableGameModes().stream()
                .anyMatch(choice -> parts[0].equalsIgnoreCase(choice.label()));

        if (!isSettingsCommand) {
            return;
        }

        // 透過現有的 FloodgateHook 檢查是否為基岩版玩家
        if (plugin.getFloodgateHook().isBedrock(player)) {
            // 1. 取消原本會跳出 Java 箱子 GUI 的事件
            event.setCancelled(true);

            // 2. 改為開啟基岩版原生表單
            plugin.getFormManager().openSettingsMenu(player);
        }
    }
}
