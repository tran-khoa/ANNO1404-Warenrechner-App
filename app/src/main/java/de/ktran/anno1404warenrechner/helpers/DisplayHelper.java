package de.ktran.anno1404warenrechner.helpers;


import android.content.Context;
import android.util.TypedValue;

public class DisplayHelper {
    public static int dpToPx(Context context, int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, context.getResources().getDisplayMetrics());
    }
}
