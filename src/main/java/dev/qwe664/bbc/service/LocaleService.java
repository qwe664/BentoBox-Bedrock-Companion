package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.user.User;

import java.io.File;
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
 * 管理員可以直接編輯那兩個檔案調整翻譯，不用重新編譯。
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

        locales.put(code, YamlConfiguration.loadConfiguration(file));
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
