package io.github.mazleo.snap.model;

import android.graphics.Bitmap;

public class PexelsImage extends PexelsElement {
    private String imageUrl;
    private int photographerId;
    private String photographerName;
    private Bitmap thumbnailBitmap;

    public PexelsImage(int id, int width, int height, String imageUrl) {
        super(id, width, height);
        this.imageUrl = imageUrl;
        this.photographerId = -1;
        this.photographerName = null;
    }
    public PexelsImage(int id, int width, int height, String imageUrl, int photographerId, String photographerName) {
        this(id, width, height, imageUrl);
        this.photographerId = photographerId;
        this.photographerName = photographerName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(int photographerId) {
        this.photographerId = photographerId;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public Bitmap getThumbnailBitmap() {
        return thumbnailBitmap;
    }

    public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
        this.thumbnailBitmap = thumbnailBitmap;
    }
}
