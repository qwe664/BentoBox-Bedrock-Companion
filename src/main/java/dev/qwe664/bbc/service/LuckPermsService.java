package dev.qwe664.bbc.service;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * 串接伺服器實際使用的權限系統（LuckPerms）。
 *
 * 注意：PermissionService 裡的 hasPermission() 系列方法本來就是透過
 * Bukkit 的 player.hasPermission()，這個機制本身已經會正確反映 LuckPerms
 * 的設定（因為 LuckPerms 是掛鉤 Bukkit 權限系統運作的），不需要另外處理。
 *
 * 這個 service 要做的，是 hasPermission() 做不到的事：
 * 查詢玩家目前的「權限組名稱」「前綴」這類 LuckPerms 專屬的中繼資料，
 * 用來顯示在表單上（例如主選單歡迎詞、島嶼資訊）。
 *
 * LuckPerms 在 plugin.yml 裡設為 softdepend（非必要），
 * 沒裝 LuckPerms 時，這裡的方法一律回傳 null，呼叫端要自己處理 null 的狀況。
 */
public class LuckPermsService {

    private final boolean enabled;

    public LuckPermsService() {
        this.enabled = Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    /**
     * LuckPerms 是否有安裝且已啟用
     */
    public boolean isEnabled() {
        return enabled;
    }

    private LuckPerms getApi() {
        return LuckPermsProvider.get();
    }

    /**
     * 取得玩家目前的主要權限組名稱（LuckPerms 內部的群組 ID，例如 "vip"、"default"）。
     * LuckPerms 未安裝、或查詢失敗時回傳 null。
     */
    public String getPrimaryGroup(Player player) {

        if (!enabled) {
            return null;
        }

        User user = getApi().getUserManager().getUser(player.getUniqueId());

        return user == null ? null : user.getPrimaryGroup();
    }

    /**
     * 取得玩家目前的聊天前綴（例如 "&6[VIP] "），已經過其他插件（如權限組繼承）
     * 運算後的最終結果，不是單純讀取單一權限組設定。
     * LuckPerms 未安裝、查無前綴、或查詢失敗時回傳 null。
     */
    public String getPrefix(Player player) {

        if (!enabled) {
            return null;
        }

        User user = getApi().getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            return null;
        }

        CachedMetaData metaData = user.getCachedData().getMetaData();

        return metaData.getPrefix();
    }
}
