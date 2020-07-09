package io.github.mazleo.snap.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.TimeUnit;

import io.github.mazleo.snap.controllers.ImageRepository;
import io.github.mazleo.snap.model.PexelsImage;
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

public class FetchImageWebService implements Observer {
    private Disposable disposable;
    private ImageRepository imageRepository;
    private PexelsImage pexelsImage;

    public FetchImageWebService(ImageRepository imageRepository) {
        disposable = null;
        this.imageRepository = imageRepository;
    }

    public void retrieveImage(PexelsImage pexelsImage, Activity activity) {
        this.pexelsImage = pexelsImage;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SearchInfo.PEXELS_PHOTOS_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);

        Observable<ResponseBody> observable = imageService.fetchMediaBytes(
                pexelsImage.getImageUrl(),
                ImageUtility.calcImageWidth(activity, pexelsImage),
                ImageUtility.calcImageHeight(activity, pexelsImage),
                "crop"
        );

        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void returnImage(PexelsImage pexelsImage) {
        this.imageRepository.returnImage(pexelsImage);
    }

    private void passError() {
        this.imageRepository.passError();
    }

    public void cleanUp() {
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
        ResponseBody responseBody = (ResponseBody) o;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(
                    responseBody.bytes(),
                    0,
                    (int) responseBody.contentLength()
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            passError();
        }

        this.pexelsImage.setImageBitmap(bitmap);

        returnImage(this.pexelsImage);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        passError();
    }

    @Override
    public void onComplete() {
    }
}
