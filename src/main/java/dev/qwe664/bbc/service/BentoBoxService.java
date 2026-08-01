package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.PlayersManager;

public class BentoBoxService {

    private final BentoBoxBedrockCompanion plugin;

    public BentoBoxService(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
    }

    public BentoBox getBentoBox() {
        return BentoBox.getInstance();
    }

    public IslandsManager getIslandsManager() {
        return getBentoBox().getIslands();
    }

    public PlayersManager getPlayersManager() {
        return getBentoBox().getPlayers();
    }
}
