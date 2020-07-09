package io.github.mazleo.snap.controllers;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class IdItemDetails extends ItemDetailsLookup.ItemDetails {
    private long itemId;

    public IdItemDetails(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getPosition() {
        return (int) itemId;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return itemId;
    }
}
