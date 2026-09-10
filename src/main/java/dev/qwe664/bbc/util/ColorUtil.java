package dev.qwe664.bbc.util;

/**
 * 把 Bukkit 傳統的 '&' 顏色代碼（如 &2、&f&l）轉成真正生效的
 * 顏色控制字元（§），供 Bedrock 表單（Cumulus）顯示用。
 *
 * 官方附加模組（Challenges 等）的設定檔裡存的名稱/描述字串
 * 幾乎都是用 '&' 寫死的，直接塞進表單不會被解析，
 * 一律先過這個方法再顯示。
 *
 * 之後如果要在程式碼裡自己組顏色文字，也統一用 '&' 寫，
 * 呼叫 translate() 轉換，不要直接寫 § —— 兩種色碼混用容易漏轉、難排查。
 */
public final class ColorUtil {

    private static final String LEGACY_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";

    private ColorUtil() {
    }

    /**
     * 轉換文字裡的 '&' 顏色代碼；null 直接回傳空字串，避免呼叫端還要另外判空。
     */
    public static String translate(String text) {
        if (text == null) {
            return "";
        }
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length - 1; i++) {
            if (characters[i] == '&' && LEGACY_CODES.indexOf(characters[i + 1]) >= 0) {
                characters[i] = '\u00a7';
                characters[i + 1] = Character.toLowerCase(characters[i + 1]);
            }
        }
        return new String(characters);
    }
}
