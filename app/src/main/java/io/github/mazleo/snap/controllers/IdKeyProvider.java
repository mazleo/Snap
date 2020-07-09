package io.github.mazleo.snap.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class IdKeyProvider extends ItemKeyProvider {
    private RecyclerView recyclerView;

    public IdKeyProvider(int scope, RecyclerView recyclerView) {
        super(scope);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return recyclerView != null ? recyclerView.getAdapter().getItemId(position) : null;
    }

    @Override
    public int getPosition(@NonNull Object key) {
        long longkey = (long) key;
        if (recyclerView != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(Math.toIntExact(longkey));
            if (viewHolder != null) {
                return viewHolder.getAdapterPosition();
            }
        }
        return -1;
    }
}
