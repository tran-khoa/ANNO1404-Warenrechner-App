package de.ktran.anno1404warenrechner.data;

import com.google.common.collect.ImmutableMap;

public enum ProductionBuilding {

    // PEASANT -> CITIZEN
    FISHING_LODGE("fishing_lodge", null, Goods.FOOD_FISH, 2.0f),
    CIDER_FARM("cider_farm", null, Goods.DRINK_CIDER, 1.5f),

    // CITIZEN -> PATRICIAN
    SPICE_FARM("spice_farm", null, Goods.FOOD_SPICES, 2.0f),
    // ---> LINEN GARMENT
    HEMP_PLANTATION("hemp_plantation", null, Goods.INTERMEDIARY_HEMP, 1.0f),
    WEAVERS_HUT("weavers_hut", ImmutableMap.of(Goods.INTERMEDIARY_HEMP, 2.0f), Goods.CLOTHING_LINEN, 2.0f),

    // PATRICIAN -> NOBLEMEN
    // ---> BREAD
    CROP_FARM("crop_farm", null, Goods.INTERMEDIARY_WHEAT, 2.0f),
    MILL("mill", ImmutableMap.of(Goods.INTERMEDIARY_WHEAT, 4.0f), Goods.INTERMEDIARY_FLOUR, 4.0f),
    BAKERY("bakery", ImmutableMap.of(Goods.INTERMEDIARY_FLOUR, 4.0f), Goods.FOOD_BREAD, 4.0f),

    // ---> BEER
    MONESTARY_GARDEN("monestary_garden", null, Goods.INTERMEDIARY_HERBS, 2.0f),
    BREWERY("monestary_brewery", ImmutableMap.of(Goods.INTERMEDIARY_HERBS, 2f, Goods.INTERMEDIARY_WHEAT, 2f), Goods.DRINK_BEER, 1.5f),

    // ---> TANNERY
    CHARCOAL_BURNER("charcoal_burner", null, Goods.INTERMEDIARY_COAL, 2.0f),
    COAL_MINE("coal_mine", null, Goods.INTERMEDIARY_COAL, 4.0f),
    SALT_MINE("salt_mine", null, Goods.INTERMEDIARY_BRINE, 4.0f),
    SALTWORKS("salt_works",
            ImmutableMap.of(Goods.INTERMEDIARY_BRINE, 4.0f, Goods.INTERMEDIARY_COAL, 4.0f), Goods.INTERMEDIARY_SALT, 4.0f),
    PIG_FARM("pig_farm", null, Goods.INTERMEDIARY_ANIMAL_HIDES, 2.0f),
    TANNERY("tannery", ImmutableMap.of(
            Goods.INTERMEDIARY_ANIMAL_HIDES, 4.0f,
            Goods.INTERMEDIARY_SALT, 2f
    ), Goods.CLOTHING_LEATHER_JERKINS, 4.0f),

    // --> BOOKS
    LUMBERJACKS_HUT("lumberjacks_hut", null, Goods.BUILDING_WOOD, 1.5f),
    INDIGO_FARM("indigo_farm", null, Goods.INTERMEDIARY_INDIGO, 1.5f),
    PAPER_MILL("paper_mill", ImmutableMap.of(
            Goods.BUILDING_WOOD, 3.0f
    ), Goods.INTERMEDIARY_PAPER, 3.0f),
    PRINTING_HOUSE("printing_house", ImmutableMap.of(
            Goods.INTERMEDIARY_INDIGO, 3f,
            Goods.INTERMEDIARY_PAPER, 1.5f
    ), Goods.POSSESSION_BOOKS, 3f),

    // --> CANDLES
    APIARY("apiary", null, Goods.INTERMEDIARY_BEESWAX, 2/3f),
    CANDLEMAKERS_WORKSHOP("candlemakers_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_HEMP, 0.75f,
            Goods.INTERMEDIARY_BEESWAX, 2f
    ), Goods.INTERMEDIARY_CANDLE, 4/3),
    COPPER_MINE("copper_mine", null, Goods.INTERMEDIARY_COPPER_ORE, 4/3f),
    COPPER_SMELTER("copper_smelter", ImmutableMap.of(
            Goods.INTERMEDIARY_COPPER_ORE, 1f,
            Goods.INTERMEDIARY_COAL, 2/3f
    ), Goods.INTERMEDIARY_BRASS, 4/3f),
    REDSMITHS_WORKSHOP("redsmiths_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_CANDLE, 1.5f,
            Goods.INTERMEDIARY_BRASS, 1f
    ), Goods.POSSESSION_CANDLESTICKS, 2.0f),

    // NOBLEMEN
    // --> MEAT
    CATTLE_FARM("cattle_farm", null, Goods.INTERMEDIARY_CATTLE, 1.25f),
    BUTCHERS_SHOP("butchers_shop", ImmutableMap.of(
            Goods.INTERMEDIARY_CATTLE, 2.5f,
            Goods.INTERMEDIARY_SALT, 1.6f //@todo inaccurate
    ), Goods.FOOD_MEAT, 2.5f),

    // --> WINE
    VINEYARD("vineyard", null, Goods.INTERMEDIARY_GRAPES, 2/3f),
    IRON_ORE_MINE("iron_ore_mine", null, Goods.INTERMEDIARY_IRON_ORE, 2f),
    IRON_SMELTER("iron_smelter", ImmutableMap.of(
            Goods.INTERMEDIARY_IRON_ORE, IRON_ORE_MINE.getTonsPerMin(),
            Goods.INTERMEDIARY_COAL, CHARCOAL_BURNER.getTonsPerMin()
    ), Goods.INTERMEDIARY_IRON, 2f),
    BARREL_COOPERAGE("barrel_cooperage", ImmutableMap.of(
            Goods.BUILDING_WOOD, 1f,
            Goods.INTERMEDIARY_IRON, 1f
    ), Goods.INTERMEDIARY_BARREL, 2f),
    WINE_PRESS("wine_press", ImmutableMap.of(
            Goods.INTERMEDIARY_GRAPES, 2f,
            Goods.INTERMEDIARY_BARREL, 2f
    ), Goods.DRINK_WINE, 2f),

    // --> GLASSES
    QUARTZ_QUARRY("quartz_quarry", null, Goods.INTERMEDIARY_QUARTZ, 4/3f),
    OPTICIANS_WORKSHOP("opticians_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_BRASS, 0.5f,
            Goods.INTERMEDIARY_QUARTZ, 0.5f
    ), Goods.POSSESSION_GLASSES, 2f),

    // --> FUR COATS
    TRAPPERS_LODGE("trappers_lodge", null, Goods.INTERMEDIARY_FUR, 2.5f),
    FURRIERS_WORKSHOP("furriers_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_FUR, TRAPPERS_LODGE.getTonsPerMin(),
            Goods.INTERMEDIARY_SALT, 4/3f
    ), Goods.CLOTHING_FUR_COATS, 2.5f),

    // -> BROCADE COATS
    GOLD_MINE("gold_mine", null, Goods.INTERMEDIARY_GOLD_ORE, 1.5f),
    GOLD_SMELTER("gold_smelter", ImmutableMap.of(
            Goods.INTERMEDIARY_GOLD_ORE, GOLD_MINE.getTonsPerMin(),
            Goods.INTERMEDIARY_COAL, 1.5f
    ), Goods.INTERMEDIARY_GOLD, 1.5f),
    SILK_PLANTATION("silk_plantation", null, Goods.INTERMEDIARY_SILK, 1.5f),
    SILK_WEAVING_MILL("silk_weaving_mill", ImmutableMap.of(
        Goods.INTERMEDIARY_SILK, 2 * SILK_PLANTATION.getTonsPerMin(),
         Goods.INTERMEDIARY_GOLD, GOLD_SMELTER.getTonsPerMin()
    ), Goods.CLOTHING_BROCADE_ROBE, 3f),

    DATE_PLANTATION("date_plantation", null, Goods.FOOD_DATE, 3f),
    GOAT_FARM("goat_farm", null, Goods.DRINK_MILK, 1.5f),
    CARPET_WORKSHOP("carpet_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_INDIGO, INDIGO_FARM.getTonsPerMin(),
            Goods.INTERMEDIARY_SILK, SILK_PLANTATION.getTonsPerMin()
    ), Goods.POSSESSION_CARPET, 1.5f),
    COFFEE_PLANTATION("coffee_plantation", null, Goods.INTERMEDIARY_COFFEE_BEAN, 1f),
    ROASTING_HOUSE("roasting_house", ImmutableMap.of(
            Goods.INTERMEDIARY_COFFEE_BEAN, 2 * COFFEE_PLANTATION.getTonsPerMin()
    ), Goods.DRINK_COFFEE, 1f),


    PEARL_FISHERS_HUT("pearl_fishers_hut", null, Goods.INTERMEDIARY_PEARL, 1f),
    PEARL_WORKSHOP("pearl_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_PEARL, PEARL_FISHERS_HUT.getTonsPerMin()
    ), Goods.POSSESSION_PEARL_NECKLACES, 1f),

    ROSE_NURSERY("rose_nursery", null, Goods.INTERMEDIARY_ROSE, 0.5f),
    PERFUMERY("perfumery", ImmutableMap.of(
            Goods.INTERMEDIARY_ROSE, 3 * ROSE_NURSERY.getTonsPerMin()
    ), Goods.POSSESSION_PERFUME, 1f),

    ALMOND_PLANTATION("almond_plantation", null, Goods.INTERMEDIARY_ALMOND, 2.0f),
    SUGAR_CANE_PLANTATION("sugar_cane_plantation", null, Goods.INTERMEDIARY_SUGAR_CANE, 2f),
    SUGAR_MILL("sugar_mill", ImmutableMap.of(
            Goods.INTERMEDIARY_SUGAR_CANE, 2 * SUGAR_CANE_PLANTATION.getTonsPerMin()
    ), Goods.INTERMEDIARY_SUGAR, 4f),
    CONFECTIONERS_WORKSHOP("confectioners_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_ALMOND, 2 * ALMOND_PLANTATION.getTonsPerMin(),
            Goods.INTERMEDIARY_SUGAR, SUGAR_MILL.getTonsPerMin()
    ),Goods.FOOD_MARZIPAN, 4f),

    /**
     * Other non-vital goods
     */
    ROPEYARD("rope_yard", ImmutableMap.of(
            Goods.INTERMEDIARY_HEMP, HEMP_PLANTATION.getTonsPerMin()
    ), Goods.ROPES, 2f),
    TOOLMAKERS_WORKSHOP("toolmaker", ImmutableMap.of(
            Goods.INTERMEDIARY_IRON, 1f
    ), Goods.TOOLS, 2f),
    CLAY_PIT("clay_pit", null, Goods.CLAY, 1.2f),

    MOSAIC_WORKSHOP("mosaic_workshop", ImmutableMap.of(
            Goods.INTERMEDIARY_QUARTZ, QUARTZ_QUARRY.getTonsPerMin() * 0.9f,
            Goods.CLAY, CLAY_PIT.getTonsPerMin() * 2
    ), Goods.MOSAICS, 2.4f),

    FOREST_GLASSWORKS("forest_glassworks", null, Goods.POTASH, 2f),
    GLASS_SMELTER("glass_smelter", ImmutableMap.of(
            Goods.POTASH, 1f,
            Goods.INTERMEDIARY_QUARTZ, 0.5f
    ), Goods.GLASS, 1f),
    WEAPONSMITH("weapon_smith", ImmutableMap.of(
            Goods.INTERMEDIARY_IRON, IRON_SMELTER.getTonsPerMin()
    ), Goods.WEAPONS, 2f),
    WAR_MACHINES_WORKSHOP("war_machines_workshop", ImmutableMap.of(
            Goods.BUILDING_WOOD, LUMBERJACKS_HUT.getTonsPerMin() * 2,
            Goods.ROPES, 1.5f
    ), Goods.WAR_MACHINES, 1.5f),
    CANNON_FOUNDRY("cannon_foundry", ImmutableMap.of(
            Goods.BUILDING_WOOD, LUMBERJACKS_HUT.getTonsPerMin() * 2,
            Goods.INTERMEDIARY_IRON, 1.5f
    ), Goods.CANNONS, 1f),
    STONEMASONS_HUT("stonemasons_hut", null, Goods.STONE, 2f)
    ;


    public final String id;
    private final ImmutableMap<Goods, Float> requires;
    private final Goods produces;
    private final float tonsPerMin;

    ProductionBuilding(String id, ImmutableMap<Goods, Float> requires, Goods produces, float tonsPerMin) {
        this.id = "production_ " + id;
        this.requires = requires;
        this.produces = produces;
        this.tonsPerMin = tonsPerMin;
    }

    public ImmutableMap<Goods, Float> getRequires() {
        return requires;
    }

    public Goods getProduces() {
        return produces;
    }

    public float getTonsPerMin() {
        return tonsPerMin;
    }

    public float getTonsPerMin(int bonus) {
        return (0.25f * bonus * tonsPerMin) + tonsPerMin;
    }

    public float getTonsPerMin(int chains, int bonus) {
        return getTonsPerMin(bonus) * chains;
    }
}
