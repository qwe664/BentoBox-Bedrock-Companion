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
        String message = event.getMessage().toLowerCase();

        // 檢查玩家是否輸入了島嶼設定相關指令
        if (message.startsWith("/is settings") || message.startsWith("/island settings")) {
            
            // 透過妳現有的 FloodgateHook 檢查是否為基岩版玩家
            if (plugin.getFloodgateHook().isBedrock(player)) {
                // 1. 取消原本會跳出 Java 箱子 GUI 的事件
                event.setCancelled(true);
                
                // 2. 這裡之後可以接妳寫好的 Geyser 表單開啟邏輯
                player.sendMessage("§a[BBC] 偵測到基岩版玩家，即將為妳開啟專屬設定介面！");
                
                // 例如：BedrockSettingsForm.open(player);
            }
        }
    }
}
