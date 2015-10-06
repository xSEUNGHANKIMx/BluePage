package com.example.bluepage.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtils {
    private final static DisplayMetrics xDisplayMetrics = Resources.getSystem().getDisplayMetrics();
    private final static float density = xDisplayMetrics.density;

    public static int px2dp(int px) {
        return (int) (px / density);
    }

    public static int dp2px(int dp) {
        return (int) (dp * density);
    }

    public static int px2dp(float px) {
        return (int) (px / density);
    }

    public static int dp2px(float dp) {
        return (int) (dp * density);
    }

    public static float getDensity() {
        return density;
    }

    public static float getDPI() {
        return xDisplayMetrics.densityDpi;
    }

    public static int getScreenWidth() {
        return xDisplayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        return xDisplayMetrics.heightPixels;
    }
}

