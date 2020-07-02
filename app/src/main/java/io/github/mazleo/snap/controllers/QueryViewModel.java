package io.github.mazleo.snap.controllers;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.model.SearchState;
import io.github.mazleo.snap.utils.SearchInfo;

public class QueryViewModel {
    private MutableLiveData<SearchResult> searchResult;
    private QueryRepository queryRepository;
    private MutableLiveData<Integer> searchProgress;

    public QueryViewModel() {
        searchResult = new MutableLiveData<SearchResult>();
        searchResult.postValue(null);
        queryRepository = null;
        searchProgress = new MutableLiveData<Integer>();
        searchProgress.postValue(SearchInfo.SEARCH_NO_PROGRESS);
    }

    public MutableLiveData<SearchResult> getSearchResultLiveData() {
        return searchResult;
    }
    public SearchResult getSearchResultObject() {
        return searchResult.getValue();
    }
    public MutableLiveData<Integer> getSearchProgressLiveData() {
        return searchProgress;
    }
    public int getSearchProgress() {
        return searchProgress.getValue();
    }
    public void setSearchResult(SearchResult searchResult) {
        this.searchResult.postValue(searchResult);
    }
    public void setSearchProgress(int searchProgress) {
        this.searchProgress.postValue(searchProgress);
    }
    public void fetchSearchResult(int pageNumber, int resultsPerPage, int searchType, String query, Activity activity) {
        setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
        this.queryRepository = new QueryRepository(this);
        this.queryRepository.startServiceForSearchResult(pageNumber, resultsPerPage, searchType, query, activity);
    }
    public void appendSearchResult(SearchResult newSearchResult) {
        Log.i("APPDEBUG", "Appending...");
        if (this.searchResult.getValue() == null) {
            this.searchResult.postValue(newSearchResult);
        }
        else {
            SearchResult currentSearchResult = this.searchResult.getValue();
            currentSearchResult.setSearchState(newSearchResult.getSearchState());
            currentSearchResult.getListPexelsElement().addAll(newSearchResult.getListPexelsElement());
            this.searchResult.postValue(currentSearchResult);
        }
        Log.i("APPDEBUG", "Appended...");
    }
}
