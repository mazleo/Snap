package io.github.mazleo.snap.network;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImageService {
    @GET("{imagePath}")
    Observable<ResponseBody> fetchMediaBytes(@Path("imagePath") String path, @Query("w") int width, @Query("h") int height, @Query("fit") String fit);
}
