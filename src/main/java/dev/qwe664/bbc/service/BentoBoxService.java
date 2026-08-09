package dev.qwe664.bbc.service;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.PlayersManager;

import java.util.List;
import java.util.Optional;

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

    /**
     * 依玩家「目前所在世界」動態取得該玩法（GameModeAddon）註冊的玩家指令別名。
     *
     * 不同玩法的指令別名不一樣，例如 AOneBlock 是 "ob"、ChunkBlock 是 "ch"，
     * 伺服器同時開多種玩法時絕對不能寫死。
     *
     * 注意：這裡查不到對應玩法時回傳空值（Optional.empty），
     * 不會再保底猜一個 "is"——因為玩家可能站在主城、大廳這類
     * 不屬於任何玩法的中立世界，這種情況下光看「目前世界」根本猜不出
     * 玩家想去哪個玩法的島嶼，硬猜一個預設值只會變成靜默失敗、
     * 玩家點按鈕完全沒反應。呼叫端要處理空值的情況，
     * 通常是改成跳出玩法選擇表單（見 getAvailableGameModes()）。
     */
    public Optional<String> getPlayerCommandLabel(Player player) {

        return getBentoBox().getIWM().getAddon(player.getWorld())
                .flatMap(GameModeAddon::getPlayerCommand)
                .map(CompositeCommand::getTopLabel);
    }

    /**
     * 列出伺服器目前所有已啟用的玩法（GameModeAddon），
     * 給「玩家不在任何玩法世界裡、需要跳出選單讓他自己選」的情境使用。
     *
     * 只回傳「有註冊玩家指令」的玩法（過濾掉沒有 getPlayerCommand() 的異常情況），
     * 每一項含玩法的顯示名稱（來自 addon.yml 的 name，例如 "AOneBlock"）
     * 跟它的指令別名（例如 "ob"）。
     */
    public List<GameModeChoice> getAvailableGameModes() {

        return getBentoBox().getAddonsManager().getGameModeAddons().stream()
                .map(addon -> new GameModeChoice(
                        addon.getDescription().getName(),
                        addon.getPlayerCommand().map(CompositeCommand::getTopLabel).orElse(null)
                ))
                .filter(choice -> choice.label() != null)
                .toList();
    }

    /**
     * 一個玩法選項：顯示名稱 + 該玩法的玩家指令別名。
     */
    public record GameModeChoice(String name, String label) {
    }

    /**
     * 依玩家「目前所在世界」取得該玩法的顯示名稱（例如 "AOneBlock"、"ChunkBlock"），
     * 用來在選單上告訴玩家「你現在在哪個玩法裡」。
     *
     * 查不到就回傳空值（玩家站在主城、大廳這類不屬於任何玩法的中立世界）。
     */
    public Optional<String> getCurrentGameModeName(Player player) {

        return getBentoBox().getIWM().getAddon(player.getWorld())
                .map(addon -> addon.getDescription().getName());
    }
}
