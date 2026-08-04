package dev.qwe664.bbc.menu;

import world.bentobox.bentobox.api.flags.Flag;

/**
 * 一組保護旗標分類：例如「容器與儲存」底下的所有 Flag.Type.PROTECTION 旗標。
 *
 * @param id     內部識別碼（例如 "STORAGE"）
 * @param title  顯示給玩家看的分類標題（例如 "容器與儲存"）
 * @param flags  此分類底下的旗標，順序對應 labels
 * @param labels 每個旗標的中文說明，順序對應 flags
 */
public record ProtectionCategory(String id, String title, Flag[] flags, String[] labels) {
}
