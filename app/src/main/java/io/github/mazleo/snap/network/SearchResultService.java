package io.github.mazleo.snap.network;

import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.SearchInfo;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchResultService {
    @Headers("Authorization: " + SearchInfo.PEXELS_API_KEY)
    @GET("{urlSearchType}/search")
    Observable<SearchResult> fetchSearchResult(@Path("urlSearchType") String urlSearchType, @Query("page") int pageNumber, @Query("per_page") int resultsPerPage, @Query("query") String query);
}
