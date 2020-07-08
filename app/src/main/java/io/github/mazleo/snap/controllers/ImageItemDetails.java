package io.github.mazleo.snap.controllers;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

import io.github.mazleo.snap.model.PexelsElement;

public class ImageItemDetails extends ItemDetailsLookup.ItemDetails {
    private final int adapterPosition;
    private final PexelsElement selectionKey;

    public ImageItemDetails(int adapterPosition, PexelsElement selectionKey) {
        this.adapterPosition = adapterPosition;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return this.adapterPosition;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return this.selectionKey;
    }
}
