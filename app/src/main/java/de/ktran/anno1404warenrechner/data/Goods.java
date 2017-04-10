package de.ktran.anno1404warenrechner.data;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import de.ktran.anno1404warenrechner.R;

public enum Goods {
    INTERMEDIARY_HEMP(R.drawable.intm_hemp, R.string.intermediary_hemp),
    INTERMEDIARY_WHEAT(R.drawable.intm_wheat, R.string.intermediary_wheat),
    INTERMEDIARY_FLOUR(R.drawable.intm_flour, R.string.intermediary_flour),
    INTERMEDIARY_HERBS(R.drawable.intm_herbs, R.string.intermediary_herbs),
    INTERMEDIARY_ALMOND(R.drawable.intm_almond, R.string.intermediary_almond),
    INTERMEDIARY_GRAPES(R.drawable.intm_grapes, R.string.intermediary_grapes),
    INTERMEDIARY_BEESWAX(R.drawable.intm_beeswax, R.string.intermediary_beeswax),
    INTERMEDIARY_COFFEE_BEAN(R.drawable.intm_coffeebean, R.string.intermediary_coffee_bean),
    INTERMEDIARY_ROSE(R.drawable.intm_rose, R.string.intermediary_rose),
    INTERMEDIARY_SUGAR_CANE(R.drawable.intm_sugarcane, R.string.intermediary_sugar_cane),
    INTERMEDIARY_SUGAR(R.drawable.intm_sugar, R.string.intermediary_sugar),
    INTERMEDIARY_INDIGO(R.drawable.intm_indigo, R.string.intermediary_indigo),
    INTERMEDIARY_SILK(R.drawable.intm_silk, R.string.intermediary_silk),
    INTERMEDIARY_COAL(R.drawable.intm_coal, R.string.intermediary_coal),
    INTERMEDIARY_BRINE(R.drawable.intm_brine, R.string.intermediary_brine),
    INTERMEDIARY_SALT(R.drawable.intm_salt, R.string.intermediary_salt),
    INTERMEDIARY_ANIMAL_HIDES(R.drawable.intm_animal_hides, R.string.intermediary_animal_hides),
    INTERMEDIARY_PAPER(R.drawable.intm_paper, R.string.intermediary_paper),
    INTERMEDIARY_CANDLE(R.drawable.intm_candle, R.string.intermediary_candle),
    INTERMEDIARY_BRASS(R.drawable.intm_brass, R.string.intermediary_brass),
    INTERMEDIARY_COPPER_ORE(R.drawable.intm_copper, R.string.intermediary_copper_ore),
    INTERMEDIARY_CATTLE(R.drawable.intm_cattle, R.string.intermediary_cattle),
    INTERMEDIARY_BARREL(R.drawable.intm_barrel, R.string.intermediary_barrel),
    INTERMEDIARY_IRON_ORE(R.drawable.intm_iron_ore, R.string.intermediary_iron_ore),
    INTERMEDIARY_IRON(R.drawable.intm_iron, R.string.intermediary_iron),
    INTERMEDIARY_QUARTZ(R.drawable.intm_quartz, R.string.intermediary_quartz),
    INTERMEDIARY_FUR(R.drawable.intm_fur, R.string.intermediary_fur),
    INTERMEDIARY_GOLD_ORE(R.drawable.intm_goldore, R.string.intermediary_gold_ore),
    INTERMEDIARY_GOLD(R.drawable.intm_gold, R.string.intermediary_gold),
    INTERMEDIARY_PEARL(R.drawable.intm_pearl, R.string.intermediary_pearl),


    BUILDING_WOOD(R.drawable.intm_wood, R.string.building_wood),

    FOOD_DATE(R.drawable.chain_date, R.string.food_date),
    DRINK_MILK(R.drawable.chain_milk, R.string.drink_milk),
    POSSESSION_CARPET(R.drawable.chain_carpet, R.string.possession_carpet),
    DRINK_COFFEE(R.drawable.chain_coffee, R.string.drink_coffee),
    POSSESSION_PEARL_NECKLACES(R.drawable.chain_pearl_necklace, R.string.possession_pearl_necklace),
    POSSESSION_PERFUME(R.drawable.chain_perfume, R.string.intermediary_perfume),
    FOOD_MARZIPAN(R.drawable.chain_marzipan, R.string.food_marzipan),


    FOOD_FISH(R.drawable.chain_fish, R.string.food_fish),
    DRINK_CIDER(R.drawable.chain_cider, R.string.drink_cider),
    CLOTHING_LINEN(R.drawable.chain_linen, R.string.clothing_linen),
    FOOD_SPICES(R.drawable.chain_spices, R.string.food_spices),
    FOOD_BREAD(R.drawable.chain_bread, R.string.food_bread),
    DRINK_BEER(R.drawable.chain_beer, R.string.drink_beer),
    CLOTHING_LEATHER_JERKINS(R.drawable.chain_leather, R.string.clothing_leather_jerkins),
    POSSESSION_BOOKS(R.drawable.chain_book, R.string.pos_books),
    POSSESSION_CANDLESTICKS(R.drawable.chain_candlesticks, R.string.pos_candlesticks),
    FOOD_MEAT(R.drawable.chain_meat, R.string.food_meat),
    DRINK_WINE(R.drawable.chain_wine, R.string.drink_wine),
    CLOTHING_FUR_COATS(R.drawable.chain_fur_coat, R.string.clothing_fur_coats),
    CLOTHING_BROCADE_ROBE(R.drawable.chain_brocade, R.string.clothing_brocade_robe),
    POSSESSION_GLASSES(R.drawable.chain_glasses, R.string.pos_glasses);

    private final int drawableId;
    private final int stringId;

    Goods(int drawableId, int stringId) {
        this.drawableId = drawableId;
        this.stringId = stringId;
    }

    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    @NonNull
    public ProductionBuilding getProductionBuilding() {
        for (ProductionBuilding p : ProductionBuilding.values()) {
            if (p.getProduces().equals(this)) {
                return p;
            }
        }

        throw new UnsupportedOperationException();
    }

    public String getName(Context context) {
        return context.getString(stringId);
    }
}
