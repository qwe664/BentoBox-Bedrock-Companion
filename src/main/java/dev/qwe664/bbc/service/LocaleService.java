package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.user.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * BBC 自己的雙語系統，目前支援 zh-TW（預設）跟 en-US。
 *
 * 語言判斷直接沿用 BentoBox 玩家自己選的語言（User.getLocale()，
 * 對應玩家用 /is language 或 /ob language 等指令設定的偏好），
 * 不另外做一套語言選單，玩家不用學第二套設定方式。
 * 反編譯 BentoBox 3.22.2 User.java 確認 getLocale() 回傳
 * java.util.Locale（第 1047 行），非猜測。
 *
 * 語言檔放在 plugins/BentoBoxBedrockCompanion/locales/ 底下，
 * 首次啟動會從 jar 內建的 resources/locales/ 複製出來，伺服器
 * 管理員可以直接編輯那兩個檔案調整翻譯，不用重新編譯。之後每次
 * 插件更新新增翻譯內容時，會自動把硬碟上缺少的 key 從 jar 內建
 * 版本補進去（不覆蓋管理員已經自訂過的既有翻譯），伺服器管理員
 * 不用手動刪檔重生就能拿到新版的翻譯內容。
 *
 * 查詢優先序：玩家語言檔 → zh-TW.yml（永遠當最終備援）→ 呼叫端
 * 自己傳入的 fallback 字串（理論上不會用到，除非兩個語言檔都
 * 意外遺失，純粹求安全不讓玩家看到空白或例外）。
 */
public class LocaleService {

    private static final String DEFAULT_LOCALE = "zh-TW";
    private static final String[] SUPPORTED_LOCALES = {"zh-TW", "en-US"};

    private final BentoBoxBedrockCompanion plugin;
    private final Map<String, YamlConfiguration> locales = new HashMap<>();

    public LocaleService(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * 從硬碟重新載入語言檔，供「重載插件設定」一併呼叫。
     */
    public void load() {
        locales.clear();
        for (String code : SUPPORTED_LOCALES) {
            loadOne(code);
        }
    }

    private void loadOne(String code) {

        File file = new File(plugin.getDataFolder(), "locales/" + code + ".yml");

        if (!file.exists()) {
            plugin.saveResource("locales/" + code + ".yml", false);
        }

        YamlConfiguration onDisk = YamlConfiguration.loadConfiguration(file);

        // 補漏：伺服器上已經存在的語言檔（從舊版就有）不會因為新版 jar
        // 更新而自動拿到新增的翻譯 key——saveResource 只在檔案「完全不存在」
        // 時才會複製，檔案已存在就完全不動它。這裡把 jar 內建版本裡「硬碟上
        // 沒有的 key」逐一補進去（不覆蓋伺服器管理員已經自訂過的既有翻譯），
        // 這樣升級插件版本、新增翻譯內容時，既有安裝也能自動跟上，不用手動
        // 刪檔重生或每次都提醒管理員去對照補檔案。
        boolean updated = mergeMissingKeys(onDisk, code);

        if (updated) {
            try {
                onDisk.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("無法把補齊的翻譯寫回 " + code + ".yml：" + e.getMessage());
            }
        }

        locales.put(code, onDisk);
    }

    /**
     * 把 jar 內建的語言檔（resources/locales/xx.yml）裡，硬碟上那份缺少的
     * key 逐一補上去。回傳是否有實際補到東西（true 才需要存檔）。
     */
    private boolean mergeMissingKeys(YamlConfiguration onDisk, String code) {

        try (InputStream stream = plugin.getResource("locales/" + code + ".yml")) {

            if (stream == null) {
                return false;
            }

            YamlConfiguration bundled = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));

            boolean changed = false;

            for (String key : bundled.getKeys(true)) {

                // 只補「葉節點」（實際有翻譯值的 key），跳過中繼的區塊節點
                // （例如 "team_menu" 這種底下還有子 key 的節點本身）。
                if (bundled.isConfigurationSection(key)) {
                    continue;
                }

                if (!onDisk.contains(key)) {
                    onDisk.set(key, bundled.get(key));
                    changed = true;
                }
            }

            return changed;

        } catch (IOException e) {
            plugin.getLogger().warning("無法讀取 jar 內建的 " + code + ".yml 進行翻譯補齊：" + e.getMessage());
            return false;
        }
    }

    /**
     * 把 BentoBox 的 java.util.Locale 對應到 BBC 支援的語言檔代碼。
     * 目前只細分「中文系」跟「其他」兩類，中文（不分繁簡）一律給
     * zh-TW，其餘語言一律給 en-US；BBC 沒有另外做簡體轉換。
     */
    private String resolveLocaleCode(Player player) {

        Locale bentoboxLocale = User.getInstance(player).getLocale();

        if (bentoboxLocale != null && bentoboxLocale.getLanguage().equalsIgnoreCase("zh")) {
            return "zh-TW";
        }

        return "en-US";
    }

    /**
     * 查一句翻譯。key 是語言檔裡的路徑（例如 "menu.island"）。
     * fallback 只在 zh-TW.yml 本身也查不到這個 key 時才會用到。
     */
    public String get(Player player, String key, String fallback) {

        String code = resolveLocaleCode(player);

        YamlConfiguration primary = locales.get(code);
        String value = primary == null ? null : primary.getString(key);

        if (value == null || value.isBlank()) {
            YamlConfiguration defaultLocale = locales.get(DEFAULT_LOCALE);
            value = defaultLocale == null ? null : defaultLocale.getString(key);
        }

        return (value == null || value.isBlank()) ? fallback : value;
    }
}
