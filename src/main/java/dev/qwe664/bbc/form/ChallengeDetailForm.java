package dev.qwe664.bbc.form;

import dev.qwe664.bbc.BentoBoxBedrockCompanion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.challenges.ChallengesAddon;
import world.bentobox.challenges.database.object.Challenge;
import world.bentobox.challenges.database.object.ChallengeLevel;
import world.bentobox.challenges.database.object.requirements.InventoryRequirements;
import world.bentobox.challenges.database.object.requirements.IslandRequirements;
import world.bentobox.challenges.database.object.requirements.OtherRequirements;
import world.bentobox.challenges.database.object.requirements.Requirements;
import world.bentobox.challenges.database.object.requirements.StatisticRequirements;
import world.bentobox.challenges.managers.ChallengesManager;
import world.bentobox.challenges.tasks.TryToComplete;

import java.util.List;
import java.util.Map;

/**
 * 單一挑戰的詳情頁：顯示需求內容，並提供「嘗試完成」按鈕。
 *
 * 需求詳情文字依 Challenge.getChallengeType() 判斷要把 getRequirements()
 * 轉型成哪一種子類別（InventoryRequirements / IslandRequirements /
 * StatisticRequirements / OtherRequirements），四種需求的欄位完全不同，
 * 型別轉型前一定要先判斷 ChallengeType，這是反編譯 Challenges-1.7.0.jar
 * 確認過的實際資料結構，不是憑印象猜測 API。
 *
 * 點擊「嘗試完成」呼叫 TryToComplete.complete(...)，這個 static 方法
 * 自己會驗證需求（檢查背包物品、統計數據等）並發放獎勵，
 * 不需要在這裡自己重算一次是否達成。
 */
public class ChallengeDetailForm {

    private final BentoBoxBedrockCompanion plugin;
    private final ChallengeLevel level;
    private final Challenge challenge;

    public ChallengeDetailForm(BentoBoxBedrockCompanion plugin, ChallengeLevel level, Challenge challenge) {
        this.plugin = plugin;
        this.level = level;
        this.challenge = challenge;
    }

    public void open(Player player) {

        FloodgateApi api = FloodgateApi.getInstance();

        if (api == null) {
            player.sendMessage("§cFloodgate API 尚未初始化。");
            return;
        }

        if (!api.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage("§e目前只有基岩版玩家可以使用 Bedrock UI。");
            return;
        }

        if (!plugin.getChallengesHook().isAvailable()) {
            player.sendMessage("§c挑戰功能目前無法使用（伺服器未安裝 Challenges 附加模組）。");
            return;
        }

        ChallengesManager manager = plugin.getChallengesHook().getChallengesManager();
        ChallengesAddon addon = plugin.getChallengesHook().getChallengesAddon();
        World world = player.getWorld();
        User user = User.getInstance(player);

        boolean complete = manager.isChallengeComplete(user, world, challenge);

        String name = challenge.getFriendlyName() == null || challenge.getFriendlyName().isBlank()
                ? challenge.getUniqueId()
                : challenge.getFriendlyName();

        StringBuilder content = new StringBuilder();

        if (challenge.getDescription() != null && !challenge.getDescription().isEmpty()) {
            content.append(String.join("\n", challenge.getDescription())).append("\n\n");
        }

        content.append(complete ? "§a✅ 已完成\n\n" : "§7⏳ 尚未完成\n\n");
        content.append("§e需求詳情：\n").append(buildRequirementDetail(player));

        var builder = SimpleForm.builder()
                .title("🏆 " + name)
                .content(content.toString());

        boolean canTry = !complete || challenge.isRepeatable();

        if (canTry) {
            builder.button("✅ 嘗試完成");
        }

        builder.button("⬅ 返回挑戰清單");

        int backButtonId = canTry ? 1 : 0;

        builder.validResultHandler(response -> {

            int clickedId = response.clickedButtonId();

            if (clickedId == backButtonId) {
                new ChallengeLevelForm(plugin, level).open(player);
                return;
            }

            // 只剩下「嘗試完成」這個按鈕會走到這裡
            boolean success = TryToComplete.complete(
                    addon,
                    user,
                    challenge,
                    world,
                    "island",
                    addon.getPermissionPrefix()
            );

            if (success) {
                player.sendMessage("§a✅ 挑戰「" + name + "」完成！");
            } else {
                player.sendMessage("§c尚未達成挑戰條件，請確認需求後再試一次。");
            }

            // 重新整理，讓完成狀態立即反映在畫面上
            open(player);
        });

        api.sendForm(player.getUniqueId(), builder);
    }

    /**
     * 依挑戰類型組出需求詳情文字。
     */
    private String buildRequirementDetail(Player player) {

        Requirements requirements = challenge.getRequirements();

        if (requirements == null) {
            return "§7（無特殊需求）";
        }

        StringBuilder detail = new StringBuilder();

        switch (challenge.getChallengeType()) {

            case INVENTORY_TYPE -> {
                if (requirements instanceof InventoryRequirements inv) {
                    detail.append(buildInventoryRequirementDetail(player, inv));
                }
            }

            case ISLAND_TYPE -> {
                if (requirements instanceof IslandRequirements island) {
                    detail.append(buildIslandRequirementDetail(island));
                }
            }

            case STATISTIC_TYPE -> {
                if (requirements instanceof StatisticRequirements stat) {
                    detail.append(buildStatisticRequirementDetail(stat));
                }
            }

            case OTHER_TYPE -> {
                if (requirements instanceof OtherRequirements other) {
                    detail.append(buildOtherRequirementDetail(other));
                }
            }
        }

        if (detail.isEmpty()) {
            detail.append("§7（無特殊需求）");
        }

        return detail.toString();
    }

    private String buildInventoryRequirementDetail(Player player, InventoryRequirements inv) {

        List<ItemStack> requiredItems = inv.getRequiredItems();

        if (requiredItems == null || requiredItems.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (ItemStack required : requiredItems) {

            int have = countInInventory(player, required.getType());
            int need = required.getAmount();
            boolean satisfied = have >= need;

            sb.append(satisfied ? "§a✔ " : "§c✘ ")
                    .append("§f").append(formatMaterialName(required.getType()))
                    .append(" §7(").append(have).append("/").append(need).append(")\n");
        }

        return sb.toString();
    }

    private String buildIslandRequirementDetail(IslandRequirements island) {

        StringBuilder sb = new StringBuilder();

        Map<Material, Integer> requiredBlocks = island.getRequiredBlocks();
        if (requiredBlocks != null && !requiredBlocks.isEmpty()) {
            sb.append("§7島嶼範圍內需要有：\n");
            requiredBlocks.forEach((material, amount) ->
                    sb.append("§f- ").append(formatMaterialName(material))
                            .append(" §7x").append(amount).append("\n"));
        }

        if (!sb.isEmpty()) {
            sb.append("§7（搜尋半徑：").append(island.getSearchRadius()).append(" 方塊）\n");
        }

        return sb.toString();
    }

    private String buildStatisticRequirementDetail(StatisticRequirements stat) {

        var requiredStatistics = stat.getRequiredStatistics();

        if (requiredStatistics == null || requiredStatistics.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        requiredStatistics.forEach(rec ->
                sb.append("§f- ").append(rec.statistic().name())
                        .append(" §7x").append(rec.amount()).append("\n"));

        return sb.toString();
    }

    private String buildOtherRequirementDetail(OtherRequirements other) {

        StringBuilder sb = new StringBuilder();

        if (other.getRequiredExperience() > 0) {
            sb.append("§f- 經驗值 §7x").append(other.getRequiredExperience()).append("\n");
        }

        if (other.getRequiredMoney() > 0) {
            sb.append("§f- 金錢 §7$").append(other.getRequiredMoney()).append("\n");
        }

        if (other.getRequiredIslandLevel() > 0) {
            sb.append("§f- 島嶼等級 §7需達 ").append(other.getRequiredIslandLevel()).append("\n");
        }

        return sb.toString();
    }

    private int countInInventory(Player player, Material material) {

        int count = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }

        return count;
    }

    private String formatMaterialName(Material material) {

        String[] words = material.name().toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }

        return sb.toString().trim();
    }
}
