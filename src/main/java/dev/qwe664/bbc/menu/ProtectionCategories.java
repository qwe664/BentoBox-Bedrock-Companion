package dev.qwe664.bbc.menu;

import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.lists.Flags;

import java.util.List;

/**
 * 島嶼保護旗標（Flag.Type.PROTECTION，需要排名的那類）分類註冊表。
 * 依照用途分成 9 個分類，總共涵蓋 BentoBox 3.20.0 的 96 個保護旗標。
 * 每個旗標附上簡短的中文說明，顯示在下拉選單標籤裡。
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
            Flags.HARVEST,
            Flags.CROP_PLANTING,
    };

    private static final String[] BUILDING_LABELS = {
            "破壞方塊",
            "破壞刷怪磚",
            "破壞漏斗",
            "放置方塊",
            "踩壞作物",
            "使用染料",
            "踩壞海龜蛋",
            "收成作物",
            "種植作物",
    };

    private static final String[] BUILDING_DESCRIPTIONS = {
            "設定誰可以破壞一般方塊",
            "覆蓋「破壞方塊」，單獨控制刷怪磚",
            "覆蓋「破壞方塊」，單獨控制漏斗",
            "設定誰可以放置方塊",
            "設定誰可以踩壞耕地上的作物",
            "設定誰可以使用染料",
            "設定誰可以踩壞海龜蛋",
            "設定誰可以收成作物，別忘了也要允許拾取物品",
            "設定誰可以種植作物",
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

    private static final String[] STORAGE_DESCRIPTIONS = {
            "設定誰可以開啟箱子與礦車箱（不含陷阱箱）",
            "設定誰可以開啟陷阱箱",
            "設定誰可以開啟木桶",
            "設定誰可以開啟潛影盒",
            "設定誰可以操作漏斗",
            "設定誰可以操作發射器",
            "設定誰可以操作投擲器",
            "設定誰可以使用熔爐",
            "設定誰可以使用釀造台",
            "設定誰可以使用堆肥桶",
            "設定誰可以操作花盆",
            "同時覆蓋以上所有容器類；個別設定會優先於此項",
            "設定誰可以放書到講台或從講台拿書（不影響閱讀）",
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

    private static final String[] WORKSTATION_DESCRIPTIONS = {
            "設定誰可以使用工作台",
            "設定誰可以使用合成器",
            "設定誰可以使用附魔台",
            "設定誰可以使用鐵砧",
            "設定誰可以操作烽火台",
            "設定誰可以吃蛋糕",
            "設定誰可以使用唱片機",
            "設定誰可以使用製圖台",
            "設定誰可以使用砂輪",
            "設定誰可以使用鍛造台",
            "設定誰可以使用切石機",
            "設定誰可以使用織布機",
            "設定誰可以與書架互動",
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
            Flags.SIGN_EDITING,
            Flags.BELL_RINGING,
            Flags.CANDLES,
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
            "編輯告示牌",
            "敲鐘",
            "蠟燭互動",
    };

    private static final String[] MECHANISM_DESCRIPTIONS = {
            "設定誰可以操作紅石元件",
            "設定誰可以操作拉桿",
            "設定誰可以使用按鈕",
            "設定誰可以觸發壓力板",
            "設定誰可以使用音階盒",
            "設定誰可以操作活板門",
            "設定誰可以開關門",
            "設定誰可以開關柵欄門",
            "設定誰可以點燃 TNT（不影響打火石本身的保護）",
            "設定誰可以觸發幽匿感測體",
            "設定誰可以觸發幽匿尖鳴體",
            "設定誰可以編輯告示牌內容",
            "設定誰可以敲響村莊之鐘",
            "設定誰可以點燃或熄滅蠟燭",
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

    private static final String[] MOB_DESCRIPTIONS = {
            "設定誰可以使用冰霜行者附魔在島上行走",
            "設定誰可以使用床",
            "設定誰可以騎乘動物",
            "設定誰可以操作盔甲座",
            "設定誰可以跟村民交易",
            "設定誰可以使用命名牌",
            "設定誰可以給予/拿取悅靈身上的物品",
            "設定誰可以繁殖動物",
            "設定誰可以擠牛奶",
            "設定誰可以用水桶撈魚",
            "設定誰可以用水桶撈美西螈",
            "設定誰可以傷害動物",
            "設定誰可以傷害怪物",
            "設定誰可以傷害村民",
            "設定誰可以傷害已被馴服的動物",
            "設定誰可以使用拴繩",
            "設定誰可以剪羊毛或剪蘑菇牛",
            "設定誰可以使用生怪蛋",
            "設定誰可以採集蜂巢/蜂箱裡的蜂蜜",
            "設定誰的操作會暫停生物的成長（例如幼體變成體）",
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

    private static final String[] MOVEMENT_DESCRIPTIONS = {
            "設定誰可以放置、破壞、進出礦車",
            "設定誰可以放置、破壞、進出船",
            "設定誰可以使用鞘翅飛行",
            "設定誰可以丟擲終界珍珠",
            "設定誰可以用紫頌果傳送",
            "設定誰可以使用地獄門",
            "設定誰可以使用終界傳送門",
            "設定誰可以開啟坐騎（如驢、駱馬）的物品欄",
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

    private static final String[] ITEMS_DESCRIPTIONS = {
            "設定誰可以操作物品展示框，會覆蓋放置/破壞方塊的設定",
            "阻止與龍蛋互動，不影響龍蛋被放置或破壞",
            "設定誰可以丟棄物品",
            "設定誰可以拾取物品",
            "設定誰可以拾取經驗球",
            "設定誰可以丟擲經驗瓶",
            "設定誰可以丟擲潑濺藥水或滯留藥水",
            "設定誰可以丟雞蛋",
            "設定誰可以使用風彈",
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

    private static final String[] COLLECTING_DESCRIPTIONS = {
            "設定誰可以使用水桶類物品",
            "覆蓋「水桶類互動」，單獨控制收集熔岩",
            "覆蓋「水桶類互動」，單獨控制收集水",
            "覆蓋「水桶類互動」，單獨控制收集粉雪",
            "設定誰可以用釣竿釣魚（防止從島外釣進保護區）",
            "設定誰可以用打火石或火焰彈點火",
            "設定誰可以撲滅火焰",
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

    private static final String[] MISC_DESCRIPTIONS = {
            "設定各項指令需要的最低排名",
            "設定誰可以鎖定/解鎖島嶼，阻擋其他玩家進入",
            "設定誰可以變更島嶼的保護與設定選項",
    };

    public static final List<ProtectionCategory> ALL = List.of(
            new ProtectionCategory("BUILDING", "建築與破壞", BUILDING_FLAGS, BUILDING_LABELS, BUILDING_DESCRIPTIONS),
            new ProtectionCategory("STORAGE", "容器與儲存", STORAGE_FLAGS, STORAGE_LABELS, STORAGE_DESCRIPTIONS),
            new ProtectionCategory("WORKSTATION", "工作站與製作", WORKSTATION_FLAGS, WORKSTATION_LABELS, WORKSTATION_DESCRIPTIONS),
            new ProtectionCategory("MECHANISM", "紅石與機關", MECHANISM_FLAGS, MECHANISM_LABELS, MECHANISM_DESCRIPTIONS),
            new ProtectionCategory("MOB", "生物互動", MOB_FLAGS, MOB_LABELS, MOB_DESCRIPTIONS),
            new ProtectionCategory("MOVEMENT", "移動與傳送", MOVEMENT_FLAGS, MOVEMENT_LABELS, MOVEMENT_DESCRIPTIONS),
            new ProtectionCategory("ITEMS", "物品拾取與丟棄", ITEMS_FLAGS, ITEMS_LABELS, ITEMS_DESCRIPTIONS),
            new ProtectionCategory("COLLECTING", "收集資源", COLLECTING_FLAGS, COLLECTING_LABELS, COLLECTING_DESCRIPTIONS),
            new ProtectionCategory("MISC", "其他與管理", MISC_FLAGS, MISC_LABELS, MISC_DESCRIPTIONS)
    );
}
