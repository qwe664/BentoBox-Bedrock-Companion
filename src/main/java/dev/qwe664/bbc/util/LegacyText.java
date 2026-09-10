package dev.qwe664.bbc.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

/**
 * Bridges BBC's existing locale strings to Paper's Adventure message API.
 *
 * <p>Locale files and Cumulus forms intentionally keep using legacy section-sign
 * strings because Bedrock forms accept strings rather than Adventure components.
 * Chat and console output pass through this class at the final send boundary.</p>
 */
public final class LegacyText {

    private static final LegacyComponentSerializer SECTION_SERIALIZER =
            LegacyComponentSerializer.legacySection();

    private LegacyText() {
    }

    /**
     * Converts a legacy section-sign string into an Adventure component.
     */
    public static Component component(String text) {
        return SECTION_SERIALIZER.deserialize(text == null ? "" : text);
    }

    /**
     * Sends a legacy locale string through Paper's Adventure audience API.
     */
    public static void send(CommandSender sender, String text) {
        sender.sendMessage(component(text));
    }
}
