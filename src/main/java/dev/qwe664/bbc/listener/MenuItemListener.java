package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import dev.qwe664.bbc.util.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * 監聽玩家右鍵選單物品，開啟主選單。
 *
 * Java 版跟基岩版玩家都走同一套邏輯：呼叫 FormManager.openMainMenu()，
 * 該方法內部已經會依 Floodgate 判斷開 Bedrock Form 還是箱子選單，這裡不用重複判斷。
 */
public class MenuItemListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public MenuItemListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        // 副手觸發的事件跟主手是成對觸發的，這裡只處理主手那次，避免選單開兩次。
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (!MenuItem.isMenuItem(plugin, item)) {
            return;
        }

        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        Player player = event.getPlayer();
        plugin.getFormManager().openMainMenu(player);
    }
}
