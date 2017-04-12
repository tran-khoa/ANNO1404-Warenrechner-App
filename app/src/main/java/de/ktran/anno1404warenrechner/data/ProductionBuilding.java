package de.ktran.anno1404warenrechner.data;

import com.google.common.collect.ImmutableMap;

public enum ProductionBuilding {

    // PEASANT -> CITIZEN
    FISHING_LODGE(null, Goods.FOOD_FISH, 2.0f),
    CIDER_FARM(null, Goods.DRINK_CIDER, 1.5f),

    // CITIZEN -> PATRICIAN
    SPICE_FARM(null, Goods.FOOD_SPICES, 2.0f),
    // ---> LINEN GARMENT
    HEMP_PLANTATION(null, Goods.INTERMEDIARY_HEMP, 1.0f),
    WEAVERS_HUT(ImmutableMap.of(Goods.INTERMEDIARY_HEMP, 2.0f), Goods.CLOTHING_LINEN, 2.0f),

    // PATRICIAN -> NOBLEMEN
    // ---> BREAD
    CROP_FARM(null, Goods.INTERMEDIARY_WHEAT, 2.0f),
    MILL(ImmutableMap.of(Goods.INTERMEDIARY_WHEAT, 4.0f), Goods.INTERMEDIARY_FLOUR, 4.0f),
    BAKERY(ImmutableMap.of(Goods.INTERMEDIARY_FLOUR, 4.0f), Goods.FOOD_BREAD, 4.0f),

    // ---> BEER
    MONESTARY_GARDEN(null, Goods.INTERMEDIARY_HERBS, 2.0f),
    BREWERY(ImmutableMap.of(Goods.INTERMEDIARY_HERBS, 2f, Goods.INTERMEDIARY_WHEAT, 2f), Goods.DRINK_BEER, 1.5f),

    // ---> TANNERY
    CHARCOAL_BURNER(null, Goods.INTERMEDIARY_COAL, 2.0f),
    COAL_MINE(null, Goods.INTERMEDIARY_COAL, 4.0f),
    SALT_MINE(null, Goods.INTERMEDIARY_BRINE, 4.0f),
    SALTWORKS(
            ImmutableMap.of(Goods.INTERMEDIARY_BRINE, 4.0f, Goods.INTERMEDIARY_COAL, 4.0f), Goods.INTERMEDIARY_SALT, 4.0f),
    PIG_FARM(null, Goods.INTERMEDIARY_ANIMAL_HIDES, 2.0f),
    TANNERY(ImmutableMap.of(
            Goods.INTERMEDIARY_ANIMAL_HIDES, 4.0f,
            Goods.INTERMEDIARY_SALT, 2f
    ), Goods.CLOTHING_LEATHER_JERKINS, 4.0f),

    // --> BOOKS
    LUMBERJACKS_HUT(null, Goods.BUILDING_WOOD, 1.5f),
    INDIGO_FARM(null, Goods.INTERMEDIARY_INDIGO, 1.5f),
    PAPER_MILL(ImmutableMap.of(
            Goods.BUILDING_WOOD, 3.0f
    ), Goods.INTERMEDIARY_PAPER, 3.0f),
    PRINTING_HOUSE(ImmutableMap.of(
            Goods.INTERMEDIARY_INDIGO, 3f,
            Goods.INTERMEDIARY_PAPER, 1.5f
    ), Goods.POSSESSION_BOOKS, 3f),

    // --> CANDLES
    APIARY(null, Goods.INTERMEDIARY_BEESWAX, 2/3f),
    CANDLEMAKERS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_HEMP, 0.75f,
            Goods.INTERMEDIARY_BEESWAX, 2f
    ), Goods.INTERMEDIARY_CANDLE, 4/3),
    COPPER_MINE(null, Goods.INTERMEDIARY_COPPER_ORE, 4/3f),
    COPPER_SMELTER(ImmutableMap.of(
            Goods.INTERMEDIARY_COPPER_ORE, 1f,
            Goods.INTERMEDIARY_COAL, 2/3f
    ), Goods.INTERMEDIARY_BRASS, 4/3f),
    REDSMITHS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_CANDLE, 1.5f,
            Goods.INTERMEDIARY_BRASS, 1f
    ), Goods.POSSESSION_CANDLESTICKS, 2.0f),

    // NOBLEMEN
    // --> MEAT
    CATTLE_FARM(null, Goods.INTERMEDIARY_CATTLE, 1.25f),
    BUTCHERS_SHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_CATTLE, 2.5f,
            Goods.INTERMEDIARY_SALT, 1.6f //@todo inaccurate
    ), Goods.FOOD_MEAT, 2.5f),

    // --> WINE
    VINEYARD(null, Goods.INTERMEDIARY_GRAPES, 2/3f),
    IRON_ORE_MINE(null, Goods.INTERMEDIARY_IRON_ORE, 2f),
    IRON_SMELTER(ImmutableMap.of(
            Goods.INTERMEDIARY_IRON_ORE, IRON_ORE_MINE.getTonsPerMin(),
            Goods.INTERMEDIARY_COAL, CHARCOAL_BURNER.getTonsPerMin()
    ), Goods.INTERMEDIARY_IRON, 2f),
    BARREL_COOPERAGE(ImmutableMap.of(
            Goods.BUILDING_WOOD, 1f,
            Goods.INTERMEDIARY_IRON, 1f
    ), Goods.INTERMEDIARY_BARREL, 2f),
    WINE_PRESS(ImmutableMap.of(
            Goods.INTERMEDIARY_GRAPES, 2f,
            Goods.INTERMEDIARY_BARREL, 2f
    ), Goods.DRINK_WINE, 2f),

    // --> GLASSES
    QUARTZ_QUARRY(null, Goods.INTERMEDIARY_QUARTZ, 4/3f),
    OPTICIANS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_BRASS, 0.5f,
            Goods.INTERMEDIARY_QUARTZ, 0.5f
    ), Goods.POSSESSION_GLASSES, 2f),

    // --> FUR COATS
    TRAPPERS_LODGE(null, Goods.INTERMEDIARY_FUR, 2.5f),
    FURRIERS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_FUR, TRAPPERS_LODGE.getTonsPerMin(),
            Goods.INTERMEDIARY_SALT, 4/3f
    ), Goods.CLOTHING_FUR_COATS, 2.5f),

    // -> BROCADE COATS
    GOLD_MINE(null, Goods.INTERMEDIARY_GOLD_ORE, 1.5f),
    GOLD_SMELTER(ImmutableMap.of(
            Goods.INTERMEDIARY_GOLD_ORE, GOLD_MINE.getTonsPerMin(),
            Goods.INTERMEDIARY_COAL, 1.5f
    ), Goods.INTERMEDIARY_GOLD, 1.5f),
    SILK_PLANTATION(null, Goods.INTERMEDIARY_SILK, 1.5f),
    SILK_WEAVING_MILL(ImmutableMap.of(
        Goods.INTERMEDIARY_SILK, 2 * SILK_PLANTATION.getTonsPerMin(),
         Goods.INTERMEDIARY_GOLD, GOLD_SMELTER.getTonsPerMin()
    ), Goods.CLOTHING_BROCADE_ROBE, 3f),

    DATE_PLANTATION(null, Goods.FOOD_DATE, 3f),
    GOAT_FARM(null, Goods.DRINK_MILK, 1.5f),
    CARPET_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_INDIGO, INDIGO_FARM.getTonsPerMin(),
            Goods.INTERMEDIARY_SILK, SILK_PLANTATION.getTonsPerMin()
    ), Goods.POSSESSION_CARPET, 1.5f),
    COFFEE_PLANTATION(null, Goods.INTERMEDIARY_COFFEE_BEAN, 1f),
    ROASTING_HOUSE(ImmutableMap.of(
            Goods.INTERMEDIARY_COFFEE_BEAN, 2 * COFFEE_PLANTATION.getTonsPerMin()
    ), Goods.DRINK_COFFEE, 1f),


    PEARL_FISHERS_HUT(null, Goods.INTERMEDIARY_PEARL, 1f),
    PEARL_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_PEARL, PEARL_FISHERS_HUT.getTonsPerMin()
    ), Goods.POSSESSION_PEARL_NECKLACES, 1f),

    ROSE_NURSERY(null, Goods.INTERMEDIARY_ROSE, 0.5f),
    PERFUMERY(ImmutableMap.of(
            Goods.INTERMEDIARY_ROSE, 3 * ROSE_NURSERY.getTonsPerMin()
    ), Goods.POSSESSION_PERFUME, 1f),

    ALMOND_PLANTATION(null, Goods.INTERMEDIARY_ALMOND, 2.0f),
    SUGAR_CANE_PLANTATION(null, Goods.INTERMEDIARY_SUGAR_CANE, 2f),
    SUGAR_MILL(ImmutableMap.of(
            Goods.INTERMEDIARY_SUGAR_CANE, 2 * SUGAR_CANE_PLANTATION.getTonsPerMin()
    ), Goods.INTERMEDIARY_SUGAR, 4f),
    CONFECTIONERS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_ALMOND, 2 * ALMOND_PLANTATION.getTonsPerMin(),
            Goods.INTERMEDIARY_SUGAR, SUGAR_MILL.getTonsPerMin()
    ),Goods.FOOD_MARZIPAN, 4f),

    /**
     * Other non-vital goods
     */
    ROPEYARD(ImmutableMap.of(
            Goods.INTERMEDIARY_HEMP, HEMP_PLANTATION.getTonsPerMin()
    ), Goods.ROPES, 2f),
    TOOLMAKERS_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_IRON, 1f
    ), Goods.TOOLS, 2f),
    CLAY_PIT(null, Goods.CLAY, 1.2f),

    MOSAIC_WORKSHOP(ImmutableMap.of(
            Goods.INTERMEDIARY_QUARTZ, QUARTZ_QUARRY.getTonsPerMin() * 0.9f,
            Goods.CLAY, CLAY_PIT.getTonsPerMin() * 2
    ), Goods.MOSAICS, 2.4f),

    FOREST_GLASSWORKS(null, Goods.POTASH, 2f),
    GLASS_SMELTER(ImmutableMap.of(
            Goods.POTASH, 1f,
            Goods.INTERMEDIARY_QUARTZ, 0.5f
    ), Goods.GLASS, 1f),
    WEAPONSMITH(ImmutableMap.of(
            Goods.INTERMEDIARY_IRON, IRON_SMELTER.getTonsPerMin()
    ), Goods.WEAPONS, 2f),
    WAR_MACHINES_WORKSHOP(ImmutableMap.of(
            Goods.BUILDING_WOOD, LUMBERJACKS_HUT.getTonsPerMin() * 2,
            Goods.ROPES, 1.5f
    ), Goods.WAR_MACHINES, 1.5f),
    CANNON_FOUNDRY(ImmutableMap.of(
            Goods.BUILDING_WOOD, LUMBERJACKS_HUT.getTonsPerMin() * 2,
            Goods.INTERMEDIARY_IRON, 1.5f
    ), Goods.CANNONS, 1f),
    STONEMASONS_HUT(null, Goods.STONE, 2f)
    ;


    private final ImmutableMap<Goods, Float> requires;
    private final Goods produces;
    private final float tonsPerMin;

    ProductionBuilding(ImmutableMap<Goods, Float> requires, Goods produces, float tonsPerMin) {
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
