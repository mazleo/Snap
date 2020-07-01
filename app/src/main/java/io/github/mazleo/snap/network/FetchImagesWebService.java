package io.github.mazleo.snap.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.mazleo.snap.controllers.QueryRepository;
import io.github.mazleo.snap.model.PexelsElement;
import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.DisplayUtility;
import io.github.mazleo.snap.utils.ImageUtility;
import io.github.mazleo.snap.utils.SearchInfo;
import io.github.mazleo.snap.utils.WindowUtility;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class FetchImagesWebService implements Observer {
    private SearchResult searchResult;
    private int currentPexelElement;
    private int currentImageType;
    private QueryRepository queryRepository;
    private Disposable imageDisposable;


    public FetchImagesWebService(QueryRepository queryRepository, SearchResult searchResult) {
        this.queryRepository = queryRepository;
        this.searchResult = searchResult;
        this.currentPexelElement = 0;
        this.currentImageType = ImageUtility.IMAGE_TYPE_THUMBNAIL;
        this.imageDisposable = null;
    }

    public void retrieveImages(Activity activity) {
        List<Observable> observableList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SearchInfo.PEXELS_PHOTOS_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        ImageService imageService = retrofit.create(ImageService.class);

        List<PexelsElement> pexelsElementList = searchResult.getListPexelsElement();
        // Download both thumbnail and src image for image search type
        if (searchResult.getSearchState().getSearchType() == SearchInfo.SEARCH_TYPE_IMAGE) {
            for (PexelsElement elementItem : pexelsElementList) {
                PexelsImage imageItem = (PexelsImage) elementItem;
                int originalWidth = imageItem.getWidth();
                int originalHeight = imageItem.getHeight();
                int thumbnailSize = ImageUtility.calcThumbnailImageSize(activity);
                int srcWidth = ImageUtility.calcSrcImageWidth(activity);
                int srcHeight = ImageUtility.calcSrcImageHeight(srcWidth, originalWidth, originalHeight);

                Observable<ResponseBody> thumbnailObservable = imageService.fetchMediaBytes(
                        imageItem.getImageUrl(),
                        thumbnailSize,
                        thumbnailSize,
                        "crop"
                );
                Observable<ResponseBody> srcObservable = imageService.fetchMediaBytes(
                        imageItem.getImageUrl(),
                        Math.min(srcWidth, originalWidth),
                        Math.min(srcHeight, originalHeight),
                        "crop"
                );

                observableList.add(thumbnailObservable);
                observableList.add(srcObservable);
            }
        }
        // Download only thumbnail for video search type
        // else {}

        Observable finalObservable = mergeObservablesInList(observableList);
        finalObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private Observable mergeObservablesInList(List<Observable> observableList) {
        Observable<ResponseBody> srcObservable = observableList.get(0);

        for (int o = 1; o < observableList.size(); o++) {
            srcObservable = srcObservable.concatWith(observableList.get(o));
        }

        return srcObservable;
    }
    private void returnSearchResult() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.imageDisposable = d;
    }
    @Override
    public void onNext(Object o) {
        ResponseBody imageResponse = (ResponseBody) o;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(
                    imageResponse.bytes(),
                    0,
                    (int) imageResponse.contentLength()
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        PexelsImage imageObject = (PexelsImage) this.searchResult.getListPexelsElement().get(this.currentPexelElement);
        switch (this.searchResult.getSearchState().getSearchType()) {
            case SearchInfo.SEARCH_TYPE_IMAGE:
                if (this.currentImageType == ImageUtility.IMAGE_TYPE_THUMBNAIL) {
                    imageObject.setThumbnailBitmap(bitmap);
                    this.currentImageType = ImageUtility.IMAGE_TYPE_SRC;
                }
                else if (this.currentImageType == ImageUtility.IMAGE_TYPE_SRC) {
                    imageObject.setSrcBitmap(bitmap);
                    this.currentImageType = ImageUtility.IMAGE_TYPE_THUMBNAIL;
                    this.currentPexelElement++;
                }
                break;
            /*
            case SearchInfo.SEARCH_TYPE_VIDEO:
                break;
             */
        }
    }
    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {
        Log.i("APPDEBUG", this.searchResult.toString());
        returnSearchResult();
    }
}
