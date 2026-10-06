package dev.qwe664.bbc.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyTextTest {

    @Test
    void nullInputReturnsEmptyComponent() {
        assertEquals(Component.empty(), LegacyText.component(null));
    }

    @Test
    void plainTextBecomesPlainComponent() {
        assertEquals(Component.text("hello world"), LegacyText.component("hello world"));
    }

    @Test
    void sectionColorBecomesAdventureColor() {
        assertEquals(
                Component.text("Error", NamedTextColor.RED),
                LegacyText.component("§cError")
        );
    }
}
