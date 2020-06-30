package io.github.mazleo.snap.util;

import android.app.Activity;

public class WindowUtility {
    public static int getWidthPX(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }
    public static int getHeightPX(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }
    public static float getWidthDP(Activity activity) {
        int widthPX = getWidthPX(activity);
        int dpi = DisplayUtility.getScreenDensityDPI(activity);
        return convertPXToDP(widthPX, dpi);
    }
    public static float getHeightDP(Activity activity) {
        int heightPX = getHeightPX(activity);
        int dpi = DisplayUtility.getScreenDensityDPI(activity);
        return convertPXToDP(heightPX, dpi);
    }
    private static float convertPXToDP(int px, int dpi) {
        return ((float) px / ((float) dpi / 160));
    }
}
