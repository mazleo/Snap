package io.github.mazleo.snap.network;

import io.github.mazleo.snap.model.SearchResult;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SearchResultService {
    @GET("{urlSearchType}/search?page={pageNum}&per_page={resultsPerPage}&query={query}")
    Observable<SearchResult> fetchSearchResult(@Path("pageNum") int pageNumber, @Path("resultsPerPage") int resultsPerPage, @Path("query") String query, @Path("urlSearchType") String urlSearchType);
}
