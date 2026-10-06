package dev.qwe664.bbc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorUtilTest {

    @Test
    void nullInputReturnsEmptyString() {
        assertEquals("", ColorUtil.translate(null));
    }

    @Test
    void plainTextIsUnchanged() {
        assertEquals("hello world", ColorUtil.translate("hello world"));
    }

    @Test
    void ampersandCodeIsTranslatedToSectionSign() {
        assertEquals("§2Green Text", ColorUtil.translate("&2Green Text"));
    }

    @Test
    void multipleAndFormattingCodesAreTranslated() {
        assertEquals("§f§lBold White", ColorUtil.translate("&f&lBold White"));
    }

    @Test
    void alreadyTranslatedSectionSignIsLeftAsIs() {
        assertEquals("§aAlready translated", ColorUtil.translate("§aAlready translated"));
    }

    @Test
    void invalidAmpersandCodeIsLeftAsIs() {
        assertEquals("&zNot a color", ColorUtil.translate("&zNot a color"));
    }

    @Test
    void uppercaseCodeIsNormalizedLikeBukkitDid() {
        assertEquals("§aGreen Text", ColorUtil.translate("&AGreen Text"));
    }
}
