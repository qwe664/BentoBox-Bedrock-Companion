package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;

public abstract class BaseForm {

    protected final BentoBoxBedrockCompanion plugin;

    protected BaseForm(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    /**
     * 開啟 Form。
     * 所有繼承 BaseForm 的類別都必須實作此方法。
     */
    public abstract void open(Player player);
}
