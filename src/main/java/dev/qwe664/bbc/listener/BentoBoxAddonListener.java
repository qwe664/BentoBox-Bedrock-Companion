package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.events.addon.AddonDisableEvent;
import world.bentobox.bentobox.api.events.addon.AddonEnableEvent;

/** 追蹤 BentoBox 附加模組在啟動完成後的啟用與停用。 */
public class BentoBoxAddonListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public BentoBoxAddonListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAddonEnable(AddonEnableEvent event) {
        Addon addon = event.getAddon();
        plugin.getLogger().info("BentoBox 附加元件已啟用：" + describe(addon));

        if ("Bank".equalsIgnoreCase(addonName(addon)) && plugin.getBankHook().getBankManager() == null) {
            plugin.getLogger().warning("Bank 已啟用，但 BankManager 尚未初始化；島嶼銀行功能目前不可用。");
        }
    }

    @EventHandler
    public void onAddonDisable(AddonDisableEvent event) {
        Addon addon = event.getAddon();
        plugin.getLogger().warning("BentoBox 附加元件已停用：" + describe(addon));
    }

    private String describe(Addon addon) {
        String version = addon.getDescription() == null ? "未知版本" : addon.getDescription().getVersion();
        return addonName(addon) + " " + version;
    }

    private String addonName(Addon addon) {
        return addon.getDescription() == null ? addon.getClass().getSimpleName() : addon.getDescription().getName();
    }
}
