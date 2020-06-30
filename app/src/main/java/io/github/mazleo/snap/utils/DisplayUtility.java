package io.github.mazleo.snap.utils;

import android.app.Activity;
import android.view.Surface;

public class DisplayUtility {
    public static final int ORIENTATION_PORTRAIT = 0;
    public static final int ORIENTATION_LANDSCAPE = 1;

    public static int getScreenDensityDPI(Activity activity) {
        return activity.getResources().getConfiguration().densityDpi;
    }
    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        return (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) ? ORIENTATION_PORTRAIT : ORIENTATION_LANDSCAPE;
    }
}
