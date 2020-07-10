package io.github.mazleo.snap.model;

import androidx.annotation.NonNull;

public class SearchState {
    private int totalResults;
    private int openPages;
    private int resultsPerPage;
    private int searchType;

    public SearchState() {
        this.totalResults = 0;
        this.openPages = 0;
        this.resultsPerPage = 0;
        this.searchType = 0;
    }

    public SearchState(int totalResults) {
        this();
        this.totalResults = totalResults;
    }

    public SearchState(int totalResults, int openPages) {
        this(totalResults);
        this.openPages = openPages;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "[total_result: " + this.totalResults
                        + ", open_pages: " + this.openPages
                        + ", results_per_page: " + this.resultsPerPage
                        + ", search_type: " + this.searchType
                        + "]";
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getOpenPages() {
        return openPages;
    }

    public void setOpenPages(int openPages) {
        this.openPages = openPages;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
}
