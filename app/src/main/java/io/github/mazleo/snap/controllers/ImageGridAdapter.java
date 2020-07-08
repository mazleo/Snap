package io.github.mazleo.snap.controllers;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.model.PexelsElement;
import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.model.SearchResult;

public class ImageGridAdapter extends RecyclerView.Adapter {
    public static class ImageGridViewHolder extends RecyclerView.ViewHolder {
        View container;
        ImageView imageView;
        List<PexelsElement> pexelsElementList;

        public ImageGridViewHolder(View view, List<PexelsElement> pexelsElementList) {
            super(view);
            this.imageView = view.findViewById(R.id.image_grid_imageview);
            this.pexelsElementList = pexelsElementList;
        }

        public ItemDetailsLookup.ItemDetails getItemDetails() {
            int adapterPosition = getAdapterPosition();
            PexelsElement selectionKey = pexelsElementList.get(adapterPosition);

            return new ImageItemDetails(adapterPosition, selectionKey);
        }
    }

    private MutableLiveData<SearchResult> searchResultMutableLiveData;

    public ImageGridAdapter(MutableLiveData<SearchResult> searchResultMutableLiveData) {
        this.searchResultMutableLiveData = searchResultMutableLiveData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_grid_item, parent, false);
        ImageGridViewHolder imageGridViewHolder = new ImageGridViewHolder(container, searchResultMutableLiveData.getValue().getListPexelsElement());
        return imageGridViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.searchResultMutableLiveData.getValue() != null) {
            List<PexelsElement> pexelsElementList = this.searchResultMutableLiveData.getValue().getListPexelsElement();
            PexelsImage pexelsImage = (PexelsImage) pexelsElementList.get(position);
            Bitmap image = pexelsImage.getThumbnailBitmap();
            ((ImageGridViewHolder) holder).imageView.setImageBitmap(image);
        }
    }

    @Override
    public int getItemCount() {
        return this.searchResultMutableLiveData.getValue() != null ?
                (this.searchResultMutableLiveData
                .getValue()
                .getListPexelsElement()
                .size())
                : 0;
    }
}
