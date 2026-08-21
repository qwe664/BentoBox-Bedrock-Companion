package dev.qwe664.bbc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressBarUtilTest {

    @Test
    void zeroTotalRendersAsZeroOverZero() {
        assertEquals("§7□□□□□□□□□□ 0/0", ProgressBarUtil.build(5, 0));
    }

    @Test
    void negativeTotalRendersAsZeroOverZero() {
        assertEquals("§7□□□□□□□□□□ 0/0", ProgressBarUtil.build(1, -3));
    }

    @Test
    void fullyCompletedFillsEveryBlock() {
        String bar = ProgressBarUtil.build(8, 8);
        assertEquals("§a■■■■■■■■■■§f 8/8", bar);
    }

    @Test
    void noProgressLeavesBarEmpty() {
        String bar = ProgressBarUtil.build(0, 8);
        assertEquals("§a§7□□□□□□□□□□§f 0/8", bar);
    }

    @Test
    void partialProgressRoundsToNearestBlock() {
        // 3/8 * 10 = 3.75 -> rounds to 4 filled blocks
        String bar = ProgressBarUtil.build(3, 8);
        assertEquals("§a■■■■§7□□□□□□§f 3/8", bar);
    }

    @Test
    void completedAboveTotalIsClampedToTotal() {
        String bar = ProgressBarUtil.build(999, 8);
        assertTrue(bar.endsWith(" 8/8"));
        assertEquals("§a■■■■■■■■■■§f 8/8", bar);
    }

    @Test
    void completedBelowZeroIsClampedToZero() {
        String bar = ProgressBarUtil.build(-5, 8);
        assertTrue(bar.endsWith(" 0/8"));
    }

    @Test
    void customBarLengthIsRespected() {
        String bar = ProgressBarUtil.build(1, 2, 4);
        // 1/2 * 4 = 2 filled blocks out of 4
        assertEquals("§a■■§7□□§f 1/2", bar);
    }
}
