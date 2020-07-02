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

public class FetchThumbnailsWebService implements Observer {
    private SearchResult searchResult;
    private int currentPexelElement;
    private QueryRepository queryRepository;
    private Disposable disposable;


    public FetchThumbnailsWebService(QueryRepository queryRepository, SearchResult searchResult) {
        this.queryRepository = queryRepository;
        this.searchResult = searchResult;
        this.currentPexelElement = 0;
        this.disposable = null;
    }

    public void retrieveImages(Activity activity) {
        if (this.searchResult.getListPexelsElement().size() == 0) {
            returnNoResult();
            return;
        }

        List<Observable> observableList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SearchInfo.PEXELS_PHOTOS_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        ImageService imageService = retrofit.create(ImageService.class);

        List<PexelsElement> pexelsElementList = searchResult.getListPexelsElement();
        for (PexelsElement elementItem : pexelsElementList) {
            PexelsImage imageItem = (PexelsImage) elementItem;
            int thumbnailSize = ImageUtility.calcThumbnailImageSize(activity);

            Observable<ResponseBody> thumbnailObservable = imageService.fetchMediaBytes(
                    imageItem.getImageUrl(),
                    thumbnailSize,
                    thumbnailSize,
                    "crop"
            );

            observableList.add(thumbnailObservable);
        }

        Observable finalObservable = mergeObservablesInList(observableList);
        if (finalObservable != null) {
            finalObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        }
    }

    private Observable mergeObservablesInList(List<Observable> observableList) {
        if (observableList.size() > 0) {
            Observable<ResponseBody> srcObservable = observableList.get(0);

            for (int o = 1; o < observableList.size(); o++) {
                srcObservable = srcObservable.concatWith(observableList.get(o));
            }

            return srcObservable;
        }
        else {
            return null;
        }
    }
    private void returnSearchResult() {
        this.queryRepository.passSearchResult(this.searchResult);
        cleanUp();
    }
    public void returnNoResult() {
        this.queryRepository.passNoResult();
        cleanUp();
    }
    public void cleanUp() {
        this.searchResult = null;
        this.queryRepository = null;
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
            this.disposable = null;
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
    }
    @Override
    public void onNext(Object o) {
        Log.i("APPDEBUG", "ON NEXT " + this.currentPexelElement);
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
        imageObject.setThumbnailBitmap(bitmap);
        this.currentPexelElement++;
    }
    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {
        returnSearchResult();
    }
}
