package io.github.mazleo.snap.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.mazleo.snap.controllers.QueryRepository;
import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.SearchInfo;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchSearchResultWebService implements Observer {
    private QueryRepository queryRepository;
    private Activity activity;
    private Disposable searchDisposable;
    private FetchImagesWebService imageWebService;
    private int searchType;

    public FetchSearchResultWebService(QueryRepository queryRepository, Activity activity, int searchType) {
        this.queryRepository = queryRepository;
        this.activity = activity;
        this.searchType = searchType;
    }

    public void retrieveSearchResult(int pageNumber, int resultsPerPage, String query) {
        Observable<SearchResult> searchResultObservable = null;
        Gson gson = new GsonBuilder().registerTypeAdapter(SearchResult.class, new ImageSearchResultDeserializer()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SearchInfo.PEXELS_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        SearchResultService searchResultService = retrofit.create(SearchResultService.class);

        if (this.searchType == SearchInfo.SEARCH_TYPE_IMAGE) {
            searchResultObservable = searchResultService.fetchSearchResult("v1", pageNumber, resultsPerPage, query);
        }
        // If searching for videos
        // else if (this.searchType == SearchInfo.SEARCH_TYPE_VIDEO)

        searchResultObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onComplete() {
    }
    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onNext(Object o) {
        SearchResult searchResult = (SearchResult) o;
        this.imageWebService = new FetchImagesWebService(this.queryRepository, searchResult);
        this.imageWebService.retrieveImages(this.activity);
    }
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.searchDisposable = d;
    }
}
