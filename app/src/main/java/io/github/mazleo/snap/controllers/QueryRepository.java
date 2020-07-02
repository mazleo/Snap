package io.github.mazleo.snap.controllers;

import android.app.Activity;

import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.network.FetchSearchResultWebService;

public class QueryRepository {
    private QueryViewModel queryViewModel;
    private FetchSearchResultWebService searchResultWebService;

    public QueryRepository(QueryViewModel queryViewModel) {
        this.queryViewModel = queryViewModel;
        this.searchResultWebService = null;
    }

    public void startServiceForSearchResult(int pageNumber, int resultsPerPage, int searchType, String query, Activity activity) {
        this.searchResultWebService = new FetchSearchResultWebService(this, activity, searchType);
        this.searchResultWebService.retrieveSearchResult(pageNumber, resultsPerPage, query);
    }
    public void passSearchResult(SearchResult searchResult) {
        this.queryViewModel.appendSearchResult(searchResult);
        cleanUp();
    }
    public void cleanUp() {
        if (this.searchResultWebService != null) {
            this.searchResultWebService.cleanUp();
            this.searchResultWebService = null;
        }
        this.queryViewModel = null;
    }
}
