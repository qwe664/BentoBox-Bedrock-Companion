package dev.qwe664.bbc.menu;

import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.lists.Flags;

import java.util.List;

/**
 * 島嶼保護旗標（Flag.Type.PROTECTION，需要排名的那類）分類註冊表。
 * 依照用途分成 9 個分類，總共涵蓋 BentoBox 3.20.0 的全部 91 個保護旗標。
 */
public final class ProtectionCategories {

    private ProtectionCategories() {
    }

    private static final Flag[] BUILDING_FLAGS = {
            Flags.BREAK_BLOCKS,
            Flags.BREAK_SPAWNERS,
            Flags.BREAK_HOPPERS,
            Flags.PLACE_BLOCKS,
            Flags.CROP_TRAMPLE,
            Flags.DYE,
            Flags.TURTLE_EGGS,
    };

    private static final String[] BUILDING_LABELS = {
            "破壞方塊",
            "破壞刷怪磚",
            "破壞漏斗",
            "放置方塊",
            "踩壞作物",
            "使用染料",
            "踩壞海龜蛋",
    };

    private static final Flag[] STORAGE_FLAGS = {
            Flags.CHEST,
            Flags.TRAPPED_CHEST,
            Flags.BARREL,
            Flags.SHULKER_BOX,
            Flags.HOPPER,
            Flags.DISPENSER,
            Flags.DROPPER,
            Flags.FURNACE,
            Flags.BREWING,
            Flags.COMPOSTER,
            Flags.FLOWER_POT,
            Flags.CONTAINER,
            Flags.LECTERN,
    };

    private static final String[] STORAGE_LABELS = {
            "箱子/礦車箱",
            "陷阱箱",
            "木桶",
            "潛影盒",
            "漏斗",
            "發射器",
            "投擲器",
            "熔爐",
            "釀造台",
            "堆肥桶",
            "花盆",
            "所有容器（總開關）",
            "講台",
    };

    private static final Flag[] WORKSTATION_FLAGS = {
            Flags.CRAFTING,
            Flags.CRAFTER,
            Flags.ENCHANTING,
            Flags.ANVIL,
            Flags.BEACON,
            Flags.CAKE,
            Flags.JUKEBOX,
            Flags.CARTOGRAPHY,
            Flags.GRINDSTONE,
            Flags.SMITHING,
            Flags.STONECUTTING,
            Flags.LOOM,
            Flags.BOOKSHELF,
    };

    private static final String[] WORKSTATION_LABELS = {
            "工作台",
            "合成器",
            "附魔台",
            "鐵砧",
            "烽火台",
            "蛋糕",
            "唱片機",
            "製圖台",
            "砂輪",
            "鍛造台",
            "切石機",
            "織布機",
            "書架",
    };

    private static final Flag[] MECHANISM_FLAGS = {
            Flags.REDSTONE,
            Flags.LEVER,
            Flags.BUTTON,
            Flags.PRESSURE_PLATE,
            Flags.NOTE_BLOCK,
            Flags.TRAPDOOR,
            Flags.DOOR,
            Flags.GATE,
            Flags.TNT_PRIMING,
            Flags.SCULK_SENSOR,
            Flags.SCULK_SHRIEKER,
    };

    private static final String[] MECHANISM_LABELS = {
            "紅石元件",
            "拉桿",
            "按鈕",
            "壓力板",
            "音階盒",
            "活板門",
            "門",
            "柵欄門",
            "點燃 TNT",
            "幽匿感測體",
            "幽匿尖鳴體",
    };

    private static final Flag[] MOB_FLAGS = {
            Flags.FROST_WALKER,
            Flags.BED,
            Flags.RIDING,
            Flags.ARMOR_STAND,
            Flags.TRADING,
            Flags.NAME_TAG,
            Flags.ALLAY,
            Flags.BREEDING,
            Flags.MILKING,
            Flags.FISH_SCOOPING,
            Flags.AXOLOTL_SCOOPING,
            Flags.HURT_ANIMALS,
            Flags.HURT_MONSTERS,
            Flags.HURT_VILLAGERS,
            Flags.HURT_TAMED_ANIMALS,
            Flags.LEASH,
            Flags.SHEARING,
            Flags.SPAWN_EGGS,
            Flags.HIVE,
            Flags.PAUSE_MOB_GROWTH,
    };

    private static final String[] MOB_LABELS = {
            "冰霜行者附魔",
            "床",
            "騎乘動物",
            "盔甲座",
            "與村民交易",
            "命名牌",
            "悅靈互動",
            "繁殖動物",
            "擠牛奶",
            "撈魚",
            "撈美西螈",
            "傷害動物",
            "傷害怪物",
            "傷害村民",
            "傷害已馴服動物",
            "拴繩",
            "剪羊毛",
            "使用生怪蛋",
            "採集蜂巢",
            "暫停生物成長",
    };

    private static final Flag[] MOVEMENT_FLAGS = {
            Flags.MINECART,
            Flags.BOAT,
            Flags.ELYTRA,
            Flags.ENDER_PEARL,
            Flags.CHORUS_FRUIT,
            Flags.NETHER_PORTAL,
            Flags.END_PORTAL,
            Flags.MOUNT_INVENTORY,
    };

    private static final String[] MOVEMENT_LABELS = {
            "礦車",
            "船",
            "鞘翅",
            "終界珍珠",
            "紫頌果",
            "地獄門",
            "終界傳送門",
            "坐騎物品欄",
    };

    private static final Flag[] ITEMS_FLAGS = {
            Flags.ITEM_FRAME,
            Flags.DRAGON_EGG,
            Flags.ITEM_DROP,
            Flags.ITEM_PICKUP,
            Flags.EXPERIENCE_PICKUP,
            Flags.EXPERIENCE_BOTTLE_THROWING,
            Flags.POTION_THROWING,
            Flags.EGGS,
            Flags.WIND_CHARGE,
    };

    private static final String[] ITEMS_LABELS = {
            "物品展示框",
            "龍蛋",
            "丟棄物品",
            "拾取物品",
            "拾取經驗",
            "丟擲經驗瓶",
            "丟擲藥水",
            "丟雞蛋",
            "風彈",
    };

    private static final Flag[] COLLECTING_FLAGS = {
            Flags.BUCKET,
            Flags.COLLECT_LAVA,
            Flags.COLLECT_WATER,
            Flags.COLLECT_POWDERED_SNOW,
            Flags.FISHING,
            Flags.FLINT_AND_STEEL,
            Flags.FIRE_EXTINGUISH,
    };

    private static final String[] COLLECTING_LABELS = {
            "水桶類互動",
            "收集熔岩",
            "收集水",
            "收集粉雪",
            "釣魚",
            "打火石點火",
            "撲滅火焰",
    };

    private static final Flag[] MISC_FLAGS = {
            Flags.COMMAND_RANKS,
            Flags.LOCK,
            Flags.CHANGE_SETTINGS,
    };

    private static final String[] MISC_LABELS = {
            "指令權限設定",
            "鎖定島嶼",
            "變更島嶼設定的權限",
    };

    public static final List<ProtectionCategory> ALL = List.of(
            new ProtectionCategory("BUILDING", "建築與破壞", BUILDING_FLAGS, BUILDING_LABELS),
            new ProtectionCategory("STORAGE", "容器與儲存", STORAGE_FLAGS, STORAGE_LABELS),
            new ProtectionCategory("WORKSTATION", "工作站與製作", WORKSTATION_FLAGS, WORKSTATION_LABELS),
            new ProtectionCategory("MECHANISM", "紅石與機關", MECHANISM_FLAGS, MECHANISM_LABELS),
            new ProtectionCategory("MOB", "生物互動", MOB_FLAGS, MOB_LABELS),
            new ProtectionCategory("MOVEMENT", "移動與傳送", MOVEMENT_FLAGS, MOVEMENT_LABELS),
            new ProtectionCategory("ITEMS", "物品拾取與丟棄", ITEMS_FLAGS, ITEMS_LABELS),
            new ProtectionCategory("COLLECTING", "收集資源", COLLECTING_FLAGS, COLLECTING_LABELS),
            new ProtectionCategory("MISC", "其他與管理", MISC_FLAGS, MISC_LABELS)
    );
}
