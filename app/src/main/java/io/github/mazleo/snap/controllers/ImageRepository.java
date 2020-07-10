package io.github.mazleo.snap.controllers;

import android.app.Activity;

import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.network.FetchImageWebService;

public class ImageRepository {
    private FetchImageWebService fetchImageWebService;
    private ImageViewModel imageViewModel;

    public ImageRepository(ImageViewModel imageViewModel) {
        this.fetchImageWebService = new FetchImageWebService(this);
        this.imageViewModel = imageViewModel;
    }

    public void startRetrieveImageService(PexelsImage pexelsImage, Activity activity) {
        this.fetchImageWebService.retrieveImage(pexelsImage, activity);
    }

    public void returnImage(PexelsImage pexelsImage) {
        this.imageViewModel.setDownloadedImage(pexelsImage);
    }

    public void cleanUp() {
        if (this.fetchImageWebService != null) {
            this.fetchImageWebService.cleanUp();
            this.fetchImageWebService = null;
        }
    }

    public void passError() {
        this.imageViewModel.error();
    }
}
