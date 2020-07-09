package io.github.mazleo.snap.utils;

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
    public static float convertPXToDP(int px, int dpi) {
        return ((float) px / ((float) dpi / 160));
    }
    public static float convertDPToPX(int dp, int dpi) {
        return (int) ((float) dp * ((float) dpi / 160f));
    }
    public static float calcScreenAspectRatio(Activity activity) {
        float width = getWidthPX(activity);
        float height = getHeightPX(activity);

        return width / height;
    }
}
