package de.ktran.anno1404warenrechner.data;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import de.ktran.anno1404warenrechner.R;

public enum Goods {
    INTERMEDIARY_HEMP("intermediary_hemp", R.drawable.intm_hemp, "Hanf"),
    INTERMEDIARY_WHEAT("intermediary_wheat", R.drawable.intm_wheat, "Weizen"),
    INTERMEDIARY_FLOUR("intermediary_flour", R.drawable.intm_flour, "Mehl"),
    INTERMEDIARY_HERBS("intermediary_herbs", R.drawable.intm_herbs, "Kräuter"),
    INTERMEDIARY_ALMOND("intermediary_almond", R.drawable.intm_almond, "Mandeln"),
    INTERMEDIARY_GRAPES("intermediary_grapes", R.drawable.intm_grapes, "Trauben"),
    INTERMEDIARY_BEESWAX("intermediary_beeswax", R.drawable.intm_beeswax, "Bienenwachs"),
    INTERMEDIARY_COFFEE_BEAN("intermediary_coffee_bean", R.drawable.intm_coffeebean, "Kaffeebohnen"),
    INTERMEDIARY_ROSE("intermediary_rose", R.drawable.intm_rose, "Rosen"),
    INTERMEDIARY_SUGAR_CANE("intermediary_sugar_cane", R.drawable.intm_sugarcane, "Zuckerrohr"),
    INTERMEDIARY_SUGAR("intermediary_sugar", R.drawable.intm_sugar, "Zucker"),
    INTERMEDIARY_INDIGO("intermediary_indigo", R.drawable.intm_indigo, "Indigo"),
    INTERMEDIARY_SILK("intermediary_silk", R.drawable.intm_silk, "Seide"),
    INTERMEDIARY_COAL("intermediary_coal", R.drawable.intm_coal, "Kohle"),
    INTERMEDIARY_BRINE("intermediary_brine", R.drawable.intm_brine, "Saline"),
    INTERMEDIARY_SALT("intermediary_salt", R.drawable.intm_salt, "Salz"),
    INTERMEDIARY_ANIMAL_HIDES("intermediary_animal_hides", R.drawable.intm_animal_hides, "Tierhäute"),
    INTERMEDIARY_PAPER("intermediary_paper", R.drawable.intm_paper, "Papier"),
    INTERMEDIARY_CANDLE("intermediary_candle", R.drawable.intm_candle, "Kerzen"),
    INTERMEDIARY_BRASS("intermediary_brass", R.drawable.intm_brass, "Kupfer"),
    INTERMEDIARY_COPPER_ORE("intermediary_copper_ore", R.drawable.intm_copper, "Kupfererz"),
    INTERMEDIARY_CATTLE("intermediary_cattle", R.drawable.intm_cattle, "Rind"),
    INTERMEDIARY_BARREL("intermediary_barrel", R.drawable.intm_barrel, "Fässer"),
    INTERMEDIARY_IRON_ORE("intermediary_iron_ore", R.drawable.intm_iron_ore, "Eisenerz"),
    INTERMEDIARY_IRON("intermediary_iron", R.drawable.intm_iron, "Eisen"),
    INTERMEDIARY_QUARTZ("intermediary_quartz", R.drawable.intm_quartz, "Quarz"),
    INTERMEDIARY_FUR("intermediary_fur", R.drawable.intm_fur, "Felle"),
    INTERMEDIARY_GOLD_ORE("intermediary_gold_ore", R.drawable.intm_goldore, "Golderz"),
    INTERMEDIARY_GOLD("intermediary_gold", R.drawable.intm_gold, "Gold"),
    INTERMEDIARY_PEARL("intermediary_pearl", R.drawable.intm_pearl, "Perlen"),


    BUILDING_WOOD("building_wood", R.drawable.intm_wood, "Holz"),

    FOOD_DATE("food_date", R.drawable.chain_date, "Datteln"),
    DRINK_MILK("drink_milk", R.drawable.chain_milk, "Milch"),
    POSSESSION_CARPET("possession_carpet", R.drawable.chain_carpet, "Teppich"),
    DRINK_COFFEE("drink_coffee", R.drawable.chain_coffee, "Kaffee"),
    POSSESSION_PEARL_NECKLACES("possession_pearl_workshop", R.drawable.chain_pearl_necklace, "Perlenketten"),
    POSSESSION_PERFUME("possession_perfume", R.drawable.chain_perfume, "Parfüm"),
    FOOD_MARZIPAN("food_marzipan", R.drawable.chain_marzipan, "Marzipan"),


    FOOD_FISH("food_fish", R.drawable.chain_fish, "Fisch"),
    DRINK_CIDER("drink_cider", R.drawable.chain_cider, "Most"),
    CLOTHING_LINEN("clothing_linen", R.drawable.chain_linen, "Leinenkutten"),
    FOOD_SPICES("food_spices", R.drawable.chain_spices, "Gewürze"),
    FOOD_BREAD("food_bread", R.drawable.chain_bread, "Brot"),
    DRINK_BEER("drink_beer", R.drawable.chain_beer, "Bier"),
    CLOTHING_LEATHER_JERKINS("clothing_leather", R.drawable.chain_leather, "Lederwämse"),
    POSSESSION_BOOKS("pos_books", R.drawable.chain_book, "Bücher"),
    POSSESSION_CANDLESTICKS("pos_candlesticks", R.drawable.chain_candlesticks, "Kerzenleuchter"),
    FOOD_MEAT("food_meat", R.drawable.chain_meat, "Fleisch"),
    DRINK_WINE("drink_wine", R.drawable.chain_wine, "Wein"),
    CLOTHING_FUR_COATS("clothing_fur_coats", R.drawable.chain_fur_coat, "Pelzmäntel"),
    CLOTHING_BROCADE_ROBE("clothing_brocade_robe", R.drawable.chain_brocade, "Brokatgewänder"),
    POSSESSION_GLASSES("pos_glasses", R.drawable.chain_glasses, "Brillengläser");

    public final String id;
    private final int drawableId;
    private final String name;

    Goods(String id, int drawableId, String name) {
        this.id = id;
        this.drawableId = drawableId;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
