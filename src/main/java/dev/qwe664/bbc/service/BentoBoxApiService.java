package dev.qwe664.bbc.service;

import org.bukkit.plugin.Plugin;

import world.bentobox.bentobox.BentoBox;

public class BentoBoxApiService {

    private final Plugin plugin;

    public BentoBoxApiService(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * BentoBox 是否存在且已啟用
     */
    public boolean hasBentoBox() {
        return plugin.getServer().getPluginManager().isPluginEnabled("BentoBox");
    }

    /**
     * 取得 BentoBox Plugin Instance
     */
    public BentoBox getBentoBox() {
        return BentoBox.getInstance();
    }
}
