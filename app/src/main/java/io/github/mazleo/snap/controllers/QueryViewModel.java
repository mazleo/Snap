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
    private MutableLiveData<Boolean> noResult;

    public QueryViewModel() {
        searchResult = new MutableLiveData<>();
        searchResult.setValue(null);
        queryRepository = null;
        searchProgress = new MutableLiveData<>();
        searchProgress.setValue(SearchInfo.SEARCH_NO_PROGRESS);
        noResult = new MutableLiveData<>();
        noResult.setValue(false);
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
    public boolean getNoResult() {
        return this.noResult.getValue();
    }
    public void setNoResult(boolean noResult) {
        this.noResult.setValue(noResult);
    }
    public void setSearchResult(SearchResult searchResult) {
        this.searchResult.setValue(searchResult);
    }
    public void setSearchProgress(int searchProgress) {
        this.searchProgress.setValue(searchProgress);
    }
    public void fetchSearchResult(int pageNumber, int resultsPerPage, int searchType, String query, Activity activity) {
        setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
        this.queryRepository = new QueryRepository(this);
        this.queryRepository.startServiceForSearchResult(pageNumber, resultsPerPage, searchType, query, activity);
    }
    public void appendSearchResult(SearchResult newSearchResult) {
        if (this.searchResult.getValue() == null) {
            this.searchResult.setValue(newSearchResult);
        }
        else {
            SearchResult currentSearchResult = this.searchResult.getValue();
            currentSearchResult.setSearchState(newSearchResult.getSearchState());
            currentSearchResult.getListPexelsElement().addAll(newSearchResult.getListPexelsElement());
            this.searchResult.setValue(currentSearchResult);
        }
        Log.i("APPDEBUG", this.searchResult.getValue().toString());
    }
    public void cancelSearchResultRetrieval() {
        cleanUp();
        this.setSearchProgress(SearchInfo.SEARCH_NO_PROGRESS);
        deleteSearchResult();
    }
    public void cleanUp() {
        if (this.queryRepository != null) {
            this.queryRepository.cleanUp();
            this.queryRepository = null;
        }
    }
    public void deleteSearchResult() {
        this.setSearchResult(null);
    }
    public void noResult() {
        setNoResult(true);
        setSearchResult(null);
        setSearchProgress(SearchInfo.SEARCH_NO_PROGRESS);
        cleanUp();
    }
}
