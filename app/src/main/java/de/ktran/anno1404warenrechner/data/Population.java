package de.ktran.anno1404warenrechner.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.common.collect.ImmutableMap;

import de.ktran.anno1404warenrechner.R;

public enum Population {
    BEGGARS(500, com.github.mcginty.R.color.material_bluegrey700,
            R.drawable.ic_beggars,
            R.string.pop_beggars,
            ImmutableMap.of(
                    Goods.FOOD_FISH, 0.7f,
                    Goods.DRINK_CIDER, 0.3f
            )
    ),

    PEASANTS(8, com.github.mcginty.R.color.material_lime700,
            R.drawable.ic_peasants,
            R.string.pop_peasants,
            ImmutableMap.of(
                    Goods.FOOD_FISH, 1f,
                    Goods.DRINK_CIDER, 0.44f
            )
    ),
    CITIZENS(15, com.github.mcginty.R.color.material_lightgreen700,
            R.drawable.ic_citizens,
            R.string.pop_citizens,
            ImmutableMap.of(
                    Goods.FOOD_FISH, 0.4f,
                    Goods.FOOD_SPICES, 0.4f,
                    Goods.DRINK_CIDER, 0.44f,
                    Goods.CLOTHING_LINEN, 0.42f
            )
    ),
    PATRICIANS(25, com.github.mcginty.R.color.material_lightblue700,
            R.drawable.ic_patricians,
            R.string.pop_patrician,
            new ImmutableMap.Builder<Goods, Float>()
                    .put(Goods.FOOD_FISH, 0.22f)
                    .put(Goods.FOOD_SPICES, 0.22f)
                    .put(Goods.FOOD_BREAD, 0.55f)
                    .put(Goods.DRINK_CIDER, 0.23f)
                    .put(Goods.DRINK_BEER, 0.24f)
                    .put(Goods.CLOTHING_LINEN, 0.19f)
                    .put(Goods.CLOTHING_LEATHER_JERKINS, 0.28f)
                    .put(Goods.POSSESSION_BOOKS, 0.16f)
                    .put(Goods.POSSESSION_CANDLESTICKS, 0.08f).build()
    ),
    NOBLEMEN(40, com.github.mcginty.R.color.material_purple700,
            R.drawable.ic_noblemen,
            R.string.pop_noblemen,
            new ImmutableMap.Builder<Goods, Float>()
                    .put(Goods.FOOD_FISH, 0.16f)
                    .put(Goods.FOOD_SPICES, 0.16f)
                    .put(Goods.FOOD_BREAD, 0.39f)
                    .put(Goods.FOOD_MEAT, 0.22f)
                    .put(Goods.DRINK_CIDER, 0.13f)
                    .put(Goods.DRINK_BEER, 0.14f)
                    .put(Goods.DRINK_WINE, 0.2f)
                    .put(Goods.CLOTHING_LINEN, 0.08f)
                    .put(Goods.CLOTHING_LEATHER_JERKINS, 0.16f)
                    .put(Goods.CLOTHING_BROCADE_ROBE, 0.16f)
                    .put(Goods.CLOTHING_FUR_COATS, 0.142f)
                    .put(Goods.POSSESSION_BOOKS, 0.09f)
                    .put(Goods.POSSESSION_CANDLESTICKS, 0.06f)
                    .put(Goods.POSSESSION_GLASSES, 0.117f)
                    .build()
    ),


    NOMADS(15, com.github.mcginty.R.color.material_yellow700,
            R.drawable.ic_nomads,
            R.string.pop_nomads,
            ImmutableMap.of(
                    Goods.FOOD_DATE, 0.666f,
                    Goods.DRINK_MILK, 0.344f,
                    Goods.POSSESSION_CARPET, 0.166f
            )
    ),
    ENVOYS(25, com.github.mcginty.R.color.material_deeporange700,
            R.drawable.ic_envoys,
            R.string.pop_envoys,
            new ImmutableMap.Builder<Goods, Float>()
                    .put(Goods.FOOD_DATE, 0.5f)
                    .put(Goods.FOOD_MARZIPAN, 0.163f)
                    .put(Goods.DRINK_MILK, 0.225f)
                    .put(Goods.DRINK_COFFEE, 0.1f)
                    .put(Goods.POSSESSION_CARPET, 0.1f)
                    .put(Goods.POSSESSION_PEARL_NECKLACES, 0.133f)
                    .put(Goods.POSSESSION_PERFUME, 0.08f).build()

    );

    // Needs of Pop. per 100 inhabitants per minute
    public final ImmutableMap<Goods, Float> needsPerMinute;

    private final int colorId;
    private final int iconId;
    private final int stringId;
    public final int houseSize;

    Population(int houseSize, int color, int icon, int string, ImmutableMap<Goods, Float> needsPerMinute) {
        this.needsPerMinute = needsPerMinute;
        this.colorId = color;
        this.iconId = icon;
        this.stringId = string;
        this.houseSize = houseSize;
    }

    public int getColor(Context context) {
        return ContextCompat.getColor(context, colorId);
    }

    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, iconId);
    }

    public String getString(Context context) {
        return context.getString(stringId);
    }

    public int getHouseCountByPopSize(int popSize) {
        return (int) Math.ceil(popSize / (float) houseSize);
    }
}
