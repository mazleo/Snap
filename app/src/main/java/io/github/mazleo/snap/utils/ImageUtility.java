package io.github.mazleo.snap.utils;

import android.app.Activity;

import io.github.mazleo.snap.model.PexelsImage;

public class ImageUtility {
    public static final int IMAGE_TYPE_THUMBNAIL = 0;
    public static final int IMAGE_TYPE_SRC = 1;
    public static final int IMAGE_RETRIEVAL_IN_PROGRESS = 0;
    public static final int IMAGE_RETRIEVAL_NO_PROGRESS = 1;
    public static final int IMAGE_WIDTH_BASE = 3;
    public static final int IMAGE_HEIGHT_BASE = 4;

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

    public static int calcImageWidth(Activity activity, PexelsImage pexelsImage) {
        int sizeBase = calcImageBase(activity, pexelsImage);
        int imageWidth = pexelsImage.getWidth();
        int imageHeight = pexelsImage.getHeight();
        int screenWidth = WindowUtility.getWidthPX(activity);
        int screenHeight = WindowUtility.getHeightPX(activity);

        switch (sizeBase) {
            case IMAGE_WIDTH_BASE:
                return Math.min(imageWidth, screenWidth);
            case IMAGE_HEIGHT_BASE:
                if (imageHeight <= screenHeight) {
                    return imageWidth;
                }
                else {
                    return (int) (((float) imageWidth / (float) imageHeight) * (float) screenHeight);
                }
        }

        return 0;
    }

    public static int calcImageHeight(Activity activity, PexelsImage pexelsImage) {
        int sizeBase = calcImageBase(activity, pexelsImage);
        int imageWidth = pexelsImage.getWidth();
        int imageHeight = pexelsImage.getHeight();
        int screenWidth = WindowUtility.getWidthPX(activity);
        int screenHeight = WindowUtility.getHeightPX(activity);

        switch (sizeBase) {
            case IMAGE_HEIGHT_BASE:
                return Math.min(imageHeight, screenHeight);
            case IMAGE_WIDTH_BASE:
                if (imageWidth <= screenWidth) {
                    return imageHeight;
                }
                else {
                    return (int) (((float) imageHeight / (float) imageWidth) * (float) screenWidth);
                }
        }

        return 0;
    }

    public static int calcImageBase(Activity activity, PexelsImage pexelsImage) {
        float imageAspectRatio = calcImageAspectRatio(pexelsImage.getWidth(), pexelsImage.getHeight());
        float screenAspectRatio = WindowUtility.calcScreenAspectRatio(activity);

        if (imageAspectRatio == screenAspectRatio) {
            return IMAGE_WIDTH_BASE;
        }
        else if (imageAspectRatio > screenAspectRatio) {
            return IMAGE_WIDTH_BASE;
        }
        else {
            return IMAGE_HEIGHT_BASE;
        }
    }

    public static float calcImageAspectRatio(float width, float height) {
        return width / height;
    }
}
