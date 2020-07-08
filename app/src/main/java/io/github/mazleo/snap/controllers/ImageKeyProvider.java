package io.github.mazleo.snap.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

import io.github.mazleo.snap.model.PexelsElement;

public class ImageKeyProvider extends ItemKeyProvider {
    private List<PexelsElement> pexelsElementList;

    public ImageKeyProvider(int scope, List<PexelsElement> pexelsElementList) {
        super(scope);
        this.pexelsElementList = pexelsElementList;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return this.pexelsElementList.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return pexelsElementList.indexOf((PexelsElement) key);
    }
}
