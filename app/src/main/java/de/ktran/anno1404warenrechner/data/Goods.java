package de.ktran.anno1404warenrechner.data;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import de.ktran.anno1404warenrechner.R;

public enum Goods {
    INTERMEDIARY_HEMP(Type.INTERMEDIARY, R.drawable.intm_hemp, R.string.intermediary_hemp),
    INTERMEDIARY_WHEAT(Type.INTERMEDIARY, R.drawable.intm_wheat, R.string.intermediary_wheat),
    INTERMEDIARY_FLOUR(Type.INTERMEDIARY, R.drawable.intm_flour, R.string.intermediary_flour),
    INTERMEDIARY_HERBS(Type.INTERMEDIARY, R.drawable.intm_herbs, R.string.intermediary_herbs),
    INTERMEDIARY_ALMOND(Type.INTERMEDIARY, R.drawable.intm_almond, R.string.intermediary_almond),
    INTERMEDIARY_GRAPES(Type.INTERMEDIARY, R.drawable.intm_grapes, R.string.intermediary_grapes),
    INTERMEDIARY_BEESWAX(Type.INTERMEDIARY, R.drawable.intm_beeswax, R.string.intermediary_beeswax),
    INTERMEDIARY_COFFEE_BEAN(Type.INTERMEDIARY, R.drawable.intm_coffeebean, R.string.intermediary_coffee_bean),
    INTERMEDIARY_ROSE(Type.INTERMEDIARY, R.drawable.intm_rose, R.string.intermediary_rose),
    INTERMEDIARY_SUGAR_CANE(Type.INTERMEDIARY, R.drawable.intm_sugarcane, R.string.intermediary_sugar_cane),
    INTERMEDIARY_SUGAR(Type.INTERMEDIARY, R.drawable.intm_sugar, R.string.intermediary_sugar),
    INTERMEDIARY_INDIGO(Type.INTERMEDIARY, R.drawable.intm_indigo, R.string.intermediary_indigo),
    INTERMEDIARY_SILK(Type.INTERMEDIARY, R.drawable.intm_silk, R.string.intermediary_silk),
    INTERMEDIARY_COAL(Type.INTERMEDIARY, R.drawable.intm_coal, R.string.intermediary_coal),
    INTERMEDIARY_BRINE(Type.INTERMEDIARY, R.drawable.intm_brine, R.string.intermediary_brine),
    INTERMEDIARY_SALT(Type.INTERMEDIARY, R.drawable.intm_salt, R.string.intermediary_salt),
    INTERMEDIARY_ANIMAL_HIDES(Type.INTERMEDIARY, R.drawable.intm_animal_hides, R.string.intermediary_animal_hides),
    INTERMEDIARY_PAPER(Type.INTERMEDIARY, R.drawable.intm_paper, R.string.intermediary_paper),
    INTERMEDIARY_CANDLE(Type.INTERMEDIARY, R.drawable.intm_candle, R.string.intermediary_candle),
    INTERMEDIARY_BRASS(Type.INTERMEDIARY, R.drawable.intm_brass, R.string.intermediary_brass),
    INTERMEDIARY_COPPER_ORE(Type.INTERMEDIARY, R.drawable.intm_copper, R.string.intermediary_copper_ore),
    INTERMEDIARY_CATTLE(Type.INTERMEDIARY, R.drawable.intm_cattle, R.string.intermediary_cattle),
    INTERMEDIARY_BARREL(Type.INTERMEDIARY, R.drawable.intm_barrel, R.string.intermediary_barrel),
    INTERMEDIARY_IRON_ORE(Type.INTERMEDIARY, R.drawable.intm_iron_ore, R.string.intermediary_iron_ore),
    INTERMEDIARY_IRON(Type.INTERMEDIARY, R.drawable.intm_iron, R.string.intermediary_iron),
    INTERMEDIARY_QUARTZ(Type.INTERMEDIARY, R.drawable.intm_quartz, R.string.intermediary_quartz),
    INTERMEDIARY_FUR(Type.INTERMEDIARY, R.drawable.intm_fur, R.string.intermediary_fur),
    INTERMEDIARY_GOLD_ORE(Type.INTERMEDIARY, R.drawable.intm_goldore, R.string.intermediary_gold_ore),
    INTERMEDIARY_GOLD(Type.INTERMEDIARY, R.drawable.intm_gold, R.string.intermediary_gold),
    INTERMEDIARY_PEARL(Type.INTERMEDIARY, R.drawable.intm_pearl, R.string.intermediary_pearl),


    BUILDING_WOOD(Type.MATERIAL, R.drawable.intm_wood, R.string.building_wood),

    FOOD_FISH(Type.NEEDS, R.drawable.chain_fish, R.string.food_fish),
    DRINK_CIDER(Type.NEEDS, R.drawable.chain_cider, R.string.drink_cider),
    FOOD_SPICES(Type.NEEDS, R.drawable.chain_spices, R.string.food_spices),
    CLOTHING_LINEN(Type.NEEDS, R.drawable.chain_linen, R.string.clothing_linen),
    FOOD_BREAD(Type.NEEDS, R.drawable.chain_bread, R.string.food_bread),
    DRINK_BEER(Type.NEEDS, R.drawable.chain_beer, R.string.drink_beer),
    CLOTHING_LEATHER_JERKINS(Type.NEEDS, R.drawable.chain_leather, R.string.clothing_leather_jerkins),
    POSSESSION_BOOKS(Type.NEEDS, R.drawable.chain_book, R.string.pos_books),
    FOOD_MEAT(Type.NEEDS, R.drawable.chain_meat, R.string.food_meat),
    CLOTHING_FUR_COATS(Type.NEEDS, R.drawable.chain_fur_coat, R.string.clothing_fur_coats),
    DRINK_WINE(Type.NEEDS, R.drawable.chain_wine, R.string.drink_wine),
    POSSESSION_GLASSES(Type.NEEDS, R.drawable.chain_glasses, R.string.pos_glasses),
    POSSESSION_CANDLESTICKS(Type.NEEDS, R.drawable.chain_candlesticks, R.string.pos_candlesticks),
    CLOTHING_BROCADE_ROBE(Type.NEEDS, R.drawable.chain_brocade, R.string.clothing_brocade_robe),

    FOOD_DATE(Type.NEEDS, R.drawable.chain_date, R.string.food_date),
    DRINK_MILK(Type.NEEDS, R.drawable.chain_milk, R.string.drink_milk),
    POSSESSION_CARPET(Type.NEEDS, R.drawable.chain_carpet, R.string.possession_carpet),
    DRINK_COFFEE(Type.NEEDS, R.drawable.chain_coffee, R.string.drink_coffee),
    POSSESSION_PEARL_NECKLACES(Type.NEEDS, R.drawable.chain_pearl_necklace, R.string.possession_pearl_necklace),
    POSSESSION_PERFUME(Type.NEEDS, R.drawable.chain_perfume, R.string.intermediary_perfume),
    FOOD_MARZIPAN(Type.NEEDS, R.drawable.chain_marzipan, R.string.food_marzipan),

    /**
     * Other non-vital goods
     */
    ROPES(Type.MATERIAL, R.drawable.res_ropes, R.string.res_ropes),
    TOOLS(Type.MATERIAL, R.drawable.res_tools, R.string.res_tools),
    CLAY(Type.INTERMEDIARY, R.drawable.intm_clay, R.string.intm_clay),
    MOSAICS(Type.MATERIAL, R.drawable.res_mosaic, R.string.res_mosaic),
    POTASH(Type.INTERMEDIARY, R.drawable.intm_potash, R.string.intm_potash),
    GLASS(Type.MATERIAL, R.drawable.res_glass, R.string.res_glass),
    WEAPONS(Type.MATERIAL, R.drawable.res_weapons, R.string.res_weapons),
    WAR_MACHINES(Type.MATERIAL, R.drawable.res_war_machines, R.string.res_war_machines),
    CANNONS(Type.MATERIAL, R.drawable.res_cannons, R.string.res_cannons),
    STONE(Type.MATERIAL, R.drawable.res_stone, R.string.res_stone);

    public enum Type {
        MATERIAL, NEEDS, INTERMEDIARY
    }

    private static final List<Goods> NEEDS = new ArrayList<>();
    private static final List<Goods> MATERIALS = new ArrayList<>();

    static {
        for (Goods g : Goods.values()) {
            if (g.type == Type.NEEDS) NEEDS.add(g);
            if (g.type == Type.MATERIAL) MATERIALS.add(g);
        }
    }

    private final int drawableId;
    private final int stringId;
    private final Type type;

    Goods(Type type, int drawableId, int stringId) {
        this.type = type;
        this.drawableId = drawableId;
        this.stringId = stringId;
    }

    public static List<Goods> getNeeds() {
        return NEEDS;
    }

    public static List<Goods> getMaterials() {
        return MATERIALS;
    }

    public static boolean isMaterial(Goods goods) {
        for (Goods g : MATERIALS) if (g.equals(goods)) return true;

        return false;
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

    public Type getType() {
        return type;
    }
}
