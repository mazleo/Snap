package io.github.mazleo.snap.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PexelsImage extends PexelsElement implements Parcelable {
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
    public PexelsImage(Parcel in) {
        super(in);
        this.imageUrl = in.readString();
        if (in.dataAvail() > 0) {
            this.photographerId = in.readInt();
            this.photographerName = in.readString();
        }
        else {
            this.photographerId = -1;
            this.photographerName = null;
        }
    }

    public static final Parcelable.Creator<PexelsImage> CREATOR = new Parcelable.Creator<PexelsImage>() {
        @Override
        public PexelsImage createFromParcel(Parcel parcel) {
            return new PexelsImage(parcel);
        }

        @Override
        public PexelsImage[] newArray(int i) {
            return new PexelsImage[i];
        }
    };

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

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.imageUrl);
        parcel.writeInt(this.photographerId);
        parcel.writeString(this.photographerName);
    }
}
