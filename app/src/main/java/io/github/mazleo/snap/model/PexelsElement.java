package io.github.mazleo.snap.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PexelsElement implements Parcelable {
    private int id;
    private int width;
    private int height;

    public PexelsElement(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public PexelsElement(Parcel in) {
        this.id = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<PexelsElement> CREATOR = new Parcelable.Creator<PexelsElement>() {
        @Override
        public PexelsElement createFromParcel(Parcel parcel) {
            return new PexelsElement(parcel);
        }

        @Override
        public PexelsElement[] newArray(int i) {
            return new PexelsElement[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + this.id;
    }
}
