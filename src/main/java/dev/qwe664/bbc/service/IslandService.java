package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;

public class IslandService {

    private final BentoBoxBedrockCompanion plugin;
    private final BentoBoxService bentoBoxService;

    public IslandService(BentoBoxBedrockCompanion plugin) {
        this.plugin = plugin;
        this.bentoBoxService = new BentoBoxService(plugin);
    }

    public BentoBoxService getBentoBoxService() {
        return bentoBoxService;
    }
}
