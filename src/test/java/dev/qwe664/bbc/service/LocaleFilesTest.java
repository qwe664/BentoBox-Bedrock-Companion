package dev.qwe664.bbc.service;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocaleFilesTest {

    private static final Pattern CJK = Pattern.compile("[\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}\\p{IsHangul}]");
    private static final Pattern TOKEN = Pattern.compile("\\{[^{}]+}");

    private final Map<String, String> english = load("en-US");
    private final Map<String, String> traditionalChinese = load("zh-TW");

    @Test
    void localeFilesHaveIdenticalLeafKeys() {
        assertEquals(traditionalChinese.keySet(), english.keySet());
    }

    @Test
    void localeValuesAreNotBlank() {
        english.forEach((key, value) -> assertFalse(value.isBlank(), "Blank en-US value: " + key));
        traditionalChinese.forEach((key, value) -> assertFalse(value.isBlank(), "Blank zh-TW value: " + key));
    }

    @Test
    void englishLocaleDoesNotContainCjkText() {
        english.forEach((key, value) ->
                assertFalse(CJK.matcher(value).find(), "CJK text in en-US value: " + key));
    }

    @Test
    void replacementTokensMatchBetweenLocales() {
        english.forEach((key, value) -> assertEquals(
                tokens(value),
                tokens(traditionalChinese.get(key)),
                "Replacement-token mismatch: " + key));
    }

    private static Map<String, String> load(String locale) {
        String path = "locales/" + locale + ".yml";
        InputStream stream = LocaleFilesTest.class.getClassLoader().getResourceAsStream(path);
        assertNotNull(stream, "Missing locale resource: " + path);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        Map<String, String> values = new TreeMap<>();
        for (String key : yaml.getKeys(true)) {
            if (!yaml.isConfigurationSection(key)) {
                Object value = yaml.get(key);
                assertNotNull(value, "Null locale value: " + key);
                values.put(key, String.valueOf(value));
            }
        }
        return values;
    }

    private static Set<String> tokens(String value) {
        Set<String> tokens = new java.util.TreeSet<>();
        Matcher matcher = TOKEN.matcher(value);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
}
