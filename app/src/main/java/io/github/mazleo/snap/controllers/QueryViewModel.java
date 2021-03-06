package io.github.mazleo.snap.controllers;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.SearchInfo;

public class QueryViewModel extends ViewModel {
    private MutableLiveData<SearchResult> searchResult;
    private QueryRepository queryRepository;
    private MutableLiveData<Integer> searchProgress;
    private MutableLiveData<Boolean> noResult;
    private MutableLiveData<Boolean> error;

    public QueryViewModel() {
        searchResult = new MutableLiveData<>();
        searchResult.setValue(null);
        queryRepository = null;
        searchProgress = new MutableLiveData<>();
        searchProgress.setValue(SearchInfo.SEARCH_NO_PROGRESS);
        noResult = new MutableLiveData<>();
        noResult.setValue(false);
        error = new MutableLiveData<>();
        error.setValue(false);
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
            appendNewSearchResultsToHeldSearchResults(newSearchResult);
        }
        cleanUp();
        this.searchProgress.setValue(SearchInfo.SEARCH_NO_PROGRESS);
    }

    private void appendNewSearchResultsToHeldSearchResults(SearchResult newSearchResult) {
        SearchResult currentSearchResult = this.searchResult.getValue();
        currentSearchResult.setSearchState(newSearchResult.getSearchState());
        currentSearchResult.getListPexelsElement().addAll(newSearchResult.getListPexelsElement());
        this.searchResult.setValue(currentSearchResult);
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

    public void onError() {
        setError(true);
        setSearchResult(null);
        setSearchProgress(SearchInfo.SEARCH_NO_PROGRESS);
        cleanUp();
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

    public MutableLiveData<Boolean> getNoResultLiveData() {
        return this.noResult;
    }
    public boolean getError() {
        return this.error.getValue();
    }

    public MutableLiveData<Boolean> getErrorLiveData() {
        return this.error;
    }

    public void setError(boolean error) {
        this.error.setValue(error);
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
}
