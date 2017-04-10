package de.ktran.anno1404warenrechner.helpers;


import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayHelper {
    public static int dpToPx(DisplayMetrics displayMetrics, int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, displayMetrics);
    }
}
