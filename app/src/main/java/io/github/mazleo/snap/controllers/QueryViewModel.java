package io.github.mazleo.snap.controllers;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.model.SearchState;
import io.github.mazleo.snap.utils.SearchInfo;

public class QueryViewModel {
    private MutableLiveData<SearchResult> searchResult;
    private QueryRepository queryRepository;
    private MutableLiveData<Integer> searchProgress;

    public QueryViewModel() {
        searchResult = null;
        queryRepository = null;
        searchProgress = null;
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
        //this.queryRepository = new QueryRepository(this);
    }
}
