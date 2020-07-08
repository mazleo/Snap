package io.github.mazleo.snap.controllers;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class ImageItemDetailsLookup extends ItemDetailsLookup {
    private final RecyclerView recyclerView;

    public ImageItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View viewItem = this.recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (viewItem != null) {
            RecyclerView.ViewHolder viewHolderItem = this.recyclerView.findContainingViewHolder(viewItem);
            if (viewHolderItem instanceof ImageGridAdapter.ImageGridViewHolder) {
                return ((ImageGridAdapter.ImageGridViewHolder) viewHolderItem).getItemDetails();
            }
        }

        return null;
    }
}
