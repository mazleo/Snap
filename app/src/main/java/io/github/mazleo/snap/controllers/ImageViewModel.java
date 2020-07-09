package io.github.mazleo.snap.controllers;

import android.app.Activity;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.github.mazleo.snap.model.PexelsImage;

public class ImageViewModel extends ViewModel {
    private MutableLiveData<PexelsImage> imageLiveData;
    private MutableLiveData<Boolean> progressLiveData;
    private MutableLiveData<Boolean> errorLiveData;
    private Activity activity;
    private ImageRepository imageRepository;

    public ImageViewModel() {
        this.imageLiveData = new MutableLiveData<>();
        this.imageLiveData.setValue(null);
        this.progressLiveData = new MutableLiveData<>();
        this.progressLiveData.setValue(false);
        this.errorLiveData = new MutableLiveData<>();
        this.errorLiveData.setValue(false);
        this.activity = null;
        this.imageRepository = null;
    }

    public void downloadImage(PexelsImage pexelsImage, Activity activity) {
        this.activity = activity;

        this.progressLiveData.setValue(true);
        this.imageRepository = new ImageRepository(this);
        this.imageRepository.startRetrieveImageService(pexelsImage, this.activity);
    }

    public void setDownloadedImage(PexelsImage pexelsImage) {
        this.imageLiveData.setValue(pexelsImage);
        this.progressLiveData.setValue(false);
        this.cleanUpWebService();
    }

    public void error() {
        this.errorLiveData.setValue(true);
        this.progressLiveData.setValue(false);
        this.errorLiveData.setValue(false);
        cleanUpWebService();
    }

    public void cleanUpWebService() {
        if (this.imageRepository != null) {
            this.imageRepository.cleanUp();
            this.imageRepository = null;
        }
    }

    public MutableLiveData<PexelsImage> getImageLiveData() {
        return imageLiveData;
    }

    public PexelsImage getPexelsImage() {
        return this.imageLiveData.getValue();
    }

    public void setImageLiveData(MutableLiveData<PexelsImage> imageLiveData) {
        this.imageLiveData = imageLiveData;
    }

    public void setPexelsImage(PexelsImage pexelsImage) {
        this.imageLiveData.setValue(pexelsImage);
    }

    public MutableLiveData<Boolean> getProgressLiveData() {
        return progressLiveData;
    }

    public boolean getProgress() {
        return this.progressLiveData.getValue();
    }

    public void setProgressLiveData(MutableLiveData<Boolean> progressLiveData) {
        this.progressLiveData = progressLiveData;
    }

    public void setProgress(boolean progress) {
        this.progressLiveData.setValue(progress);
    }

    public MutableLiveData<Boolean> getErrorLiveData() {
        return errorLiveData;
    }

    public boolean getError() {
        return this.errorLiveData.getValue();
    }

    public void setErrorLiveData(MutableLiveData<Boolean> errorLiveData) {
        this.errorLiveData = errorLiveData;
    }

    public void setError(boolean error) {
        this.errorLiveData.setValue(error);
    }
}
