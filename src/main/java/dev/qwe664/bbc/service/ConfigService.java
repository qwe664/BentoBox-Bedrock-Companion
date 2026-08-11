package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;

/**
 * 讀取 plugins/BentoBoxBedrockCompanion/config.yml。
 *
 * 涵蓋兩類設定：
 * - messages.*：文字訊息（歡迎詞、職級稱呼備用文字、大廳名稱等），
 *   缺值或空字串時一律 fallback 回程式碼裡寫死的中文，不會讓玩家
 *   看到 null 或空白。
 * - features.*：功能開關，只控制「BBC 自己這層的按鈕/子選單要不要
 *   顯示」，不影響底層附加模組本身是否安裝或運作——即使某個開關
 *   關閉，Warps/Challenges/Visit 這些附加模組還是正常運作，玩家只是
 *   看不到 BBC 表單裡對應的入口。缺值時預設全部視為開啟（true），
 *   這樣既有安裝、還沒手動編輯過 config.yml 的伺服器行為不會被改變。
 */
public class ConfigService {

    private final BentoBoxBedrockCompanion plugin;

    public ConfigService(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    /**
     * 重新從硬碟讀取 config.yml。呼叫端負責先把訊息告知玩家。
     */
    public void reload() {
        plugin.reloadConfig();
    }

    /**
     * 讀取 messages 區塊下的一個文字設定，key 不含 "messages." 前綴，
     * 例如 getMessage("ranks.owner", "隊長")。
     */
    public String getMessage(String key, String fallback) {
        String value = plugin.getConfig().getString("messages." + key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    /**
     * 讀取 features 區塊下的一個功能開關，key 不含 "features." 前綴，
     * 缺值預設 true（開啟）。
     */
    public boolean isFeatureEnabled(String key) {
        return plugin.getConfig().getBoolean("features." + key, true);
    }
}
