package dev.qwe664.bbc.listener;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.events.BentoBoxReadyEvent;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

/** 在 BentoBox 與所有附加元件完成啟用後記錄最終整合狀態。 */
public class BentoBoxReadyListener implements Listener {

    private final BentoBoxBedrockCompanion plugin;

    public BentoBoxReadyListener(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBentoBoxReady(BentoBoxReadyEvent event) {
        auditBentoBoxAddons();
        reportOptionalAddonFeatures();

        var bankHook = plugin.getBankHook();

        if (!bankHook.isAddonEnabled()) {
            plugin.getLogger().info("未偵測到 Bank 附加模組，%bbc_island_money% 變數固定回傳 0（不影響其他功能）。");
            return;
        }

        if (bankHook.getBankManager() == null) {
            plugin.getLogger().warning("已偵測到 Bank 附加模組，但 BankManager 在 BentoBox 完成載入後仍未初始化；請檢查 Bank 的啟用錯誤與 Vault 經濟服務。");
            return;
        }

        plugin.getLogger().info("已偵測到 Bank 附加模組且 BankManager 已初始化，島嶼銀行功能已啟用。");
    }

    /** 等 BentoBox 完成啟用流程後，才報告選用 addon 對應的功能狀態。 */
    private void reportOptionalAddonFeatures() {
        if (plugin.getWarpsHook().isAvailable()) {
            plugin.getLogger().info("已偵測到 Warps 附加模組，傳送點功能已啟用。");
        } else {
            plugin.getLogger().info("未偵測到 Warps 附加模組，傳送點功能將不會顯示（不影響其他功能）。");
        }

        if (plugin.getChallengesHook().isAvailable()) {
            plugin.getLogger().info("已偵測到 Challenges 附加模組，挑戰功能已啟用。");
        } else {
            plugin.getLogger().info("未偵測到 Challenges 附加模組，挑戰功能將不會顯示（不影響其他功能）。");
        }

        if (plugin.getVisitHook().isAvailable()) {
            plugin.getLogger().info("已偵測到 Visit 附加模組，拜訪島嶼功能已啟用。");
        } else {
            plugin.getLogger().info("未偵測到 Visit 附加模組，拜訪島嶼功能將不會顯示（不影響其他功能）。");
        }
    }

    /** 檢查 BentoBox 主插件與每一個由 AddonsManager 管理的附加元件。 */
    private void auditBentoBoxAddons() {
        var bentoBox = plugin.getBentoBoxService().getBentoBox();
        boolean mainPluginEnabled = bentoBox.isEnabled();
        List<Addon> addons = bentoBox.getAddonsManager().getAddons().stream()
                .sorted(Comparator.comparing(this::addonName, String.CASE_INSENSITIVE_ORDER))
                .toList();
        long enabledCount = addons.stream().filter(Addon::isEnabled).count();

        plugin.getLogger().log(mainPluginEnabled ? Level.INFO : Level.WARNING,
                "BentoBox 主插件狀態：" + (mainPluginEnabled ? "已啟用" : "未啟用"));

        for (Addon addon : addons) {
            String version = addon.getDescription() == null ? "未知版本" : addon.getDescription().getVersion();
            plugin.getLogger().log(addon.isEnabled() ? Level.INFO : Level.WARNING,
                    "BentoBox 附加元件：" + addonName(addon) + " " + version + "，狀態：" + addon.getState());
        }

        if (mainPluginEnabled && enabledCount == addons.size()) {
            plugin.getLogger().info("BentoBox 完整載入檢查通過：主插件已啟用，附加元件 "
                    + enabledCount + "/" + addons.size() + " 已全部啟用。");
        } else {
            plugin.getLogger().warning("BentoBox 完整載入檢查未通過：主插件="
                    + (mainPluginEnabled ? "已啟用" : "未啟用") + "，附加元件="
                    + enabledCount + "/" + addons.size() + " 已啟用；請查看上方非 ENABLED 項目。");
        }
    }

    private String addonName(Addon addon) {
        return addon.getDescription() == null ? addon.getClass().getSimpleName() : addon.getDescription().getName();
    }
}
