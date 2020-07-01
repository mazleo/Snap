package io.github.mazleo.snap.utils;

import android.app.Activity;

public class ImageUtility {
    public static final int IMAGE_TYPE_THUMBNAIL = 0;
    public static final int IMAGE_TYPE_SRC = 1;

    public static int calcThumbnailImageSize(Activity activity) {
        int screenOrientation = DisplayUtility.getScreenOrientation(activity);
        int screenWidth = WindowUtility.getWidthPX(activity);
        int thumbnailSize = -1;

        switch (screenOrientation) {
            case DisplayUtility.ORIENTATION_PORTRAIT:
                thumbnailSize = (screenWidth - 2) / 3;
                break;
            case DisplayUtility.ORIENTATION_LANDSCAPE:
                thumbnailSize = (screenWidth - 5) / 6;
                break;
        }

        return thumbnailSize;
    }
    public static int calcSrcImageWidth(Activity activity) {
        int screenWidth = WindowUtility.getWidthPX(activity);
        return screenWidth * 2;
    }
    public static int calcSrcImageHeight(int srcWidth, int originalWidth, int originalHeight) {
        return (originalHeight / originalWidth) * srcWidth;
    }
}
