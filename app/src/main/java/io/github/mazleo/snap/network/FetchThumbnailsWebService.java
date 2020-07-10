package io.github.mazleo.snap.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.mazleo.snap.controllers.QueryRepository;
import io.github.mazleo.snap.model.PexelsElement;
import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.ImageUtility;
import io.github.mazleo.snap.utils.SearchInfo;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
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
        if (isListPexelsElementEmpty()) {
            returnNoResult();
            return;
        }

        OkHttpClient okHttpClient = buildOkHttpClientWithCustomTimeout();
        Retrofit retrofit = buildRetrofitWithRxJava(okHttpClient);
        ImageService imageService = retrofit.create(ImageService.class);
        List<Observable> observableList = populateObservableList(activity, imageService);

        Observable finalObservable = mergeObservablesInList(observableList);
        if (finalObservable != null) {
            finalObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        }
    }

    private boolean isListPexelsElementEmpty() {
        return this.searchResult.getListPexelsElement().size() == 0;
    }

    private OkHttpClient buildOkHttpClientWithCustomTimeout() {
        return new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private Retrofit buildRetrofitWithRxJava(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(SearchInfo.PEXELS_PHOTOS_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private List<Observable> populateObservableList(Activity activity, ImageService imageService) {
        List<Observable> observableList = new ArrayList<>();
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

        return observableList;
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
    }

    public void returnNoResult() {
        this.queryRepository.passNoResult();
    }

    public void cleanUp() {
        this.searchResult = null;
        this.queryRepository = null;
        cleanUpDisposable();
    }

    private void cleanUpDisposable() {
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
            this.disposable = null;
        }
    }

    public void passError() {
        this.queryRepository.passError();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
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
            passError();
        }
        PexelsImage imageObject = (PexelsImage) this.searchResult.getListPexelsElement().get(this.currentPexelElement);
        imageObject.setThumbnailBitmap(bitmap);
        this.currentPexelElement++;
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        passError();
    }

    @Override
    public void onComplete() {
        returnSearchResult();
    }
}
