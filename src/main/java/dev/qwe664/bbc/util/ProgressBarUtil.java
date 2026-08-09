package dev.qwe664.bbc.util;

/**
 * 把「完成數 / 總數」轉成文字進度條，例如 "■■■□□□□□ 3/8"。
 *
 * 基岩版表單（Cumulus SimpleForm）沒有原生的進度條元件，
 * 用色塊字元組合文字是最常見的替代做法。
 */
public final class ProgressBarUtil {

    private static final int DEFAULT_LENGTH = 10;
    private static final char FILLED_CHAR = '■';
    private static final char EMPTY_CHAR = '□';

    private ProgressBarUtil() {
    }

    /**
     * 產生一段文字進度條，例如 "§a■■■§7□□□□□□□ 3/8"。
     *
     * @param completed 已完成數量
     * @param total     總數量（0 時視為 0/0，不會出錯，全部顯示為空格子）
     */
    public static String build(int completed, int total) {
        return build(completed, total, DEFAULT_LENGTH);
    }

    public static String build(int completed, int total, int barLength) {

        if (total <= 0) {
            return "§7" + EMPTY_CHAR_STRING(barLength) + " 0/0";
        }

        int clampedCompleted = Math.max(0, Math.min(completed, total));
        int filledCount = (int) Math.round((double) clampedCompleted / total * barLength);
        filledCount = Math.max(0, Math.min(filledCount, barLength));

        StringBuilder bar = new StringBuilder();
        bar.append("§a");

        for (int i = 0; i < barLength; i++) {
            if (i == filledCount) {
                bar.append("§7");
            }
            bar.append(i < filledCount ? FILLED_CHAR : EMPTY_CHAR);
        }

        bar.append("§f ").append(clampedCompleted).append("/").append(total);

        return bar.toString();
    }

    private static String EMPTY_CHAR_STRING(int length) {
        return String.valueOf(EMPTY_CHAR).repeat(Math.max(0, length));
    }
}
