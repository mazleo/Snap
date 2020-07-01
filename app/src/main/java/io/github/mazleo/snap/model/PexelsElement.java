package io.github.mazleo.snap.model;

import androidx.annotation.NonNull;

public class PexelsElement {
    private int id;
    private int width;
    private int height;

    public PexelsElement(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
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
