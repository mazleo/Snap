package io.github.mazleo.snap.network;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.mazleo.snap.model.PexelsElement;
import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.model.SearchState;
import io.github.mazleo.snap.utils.SearchInfo;

public class ImageSearchResultDeserializer implements JsonDeserializer<SearchResult> {
    public SearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        SearchState searchState = new SearchState();
        List<PexelsElement> pexelsElementList = new ArrayList<>();

        JsonObject resultJsonObject = json.getAsJsonObject();

        searchState.setTotalResults(resultJsonObject.getAsJsonPrimitive("total_results").getAsInt());
        searchState.setOpenPages(resultJsonObject.getAsJsonPrimitive("page").getAsInt());
        searchState.setResultsPerPage(resultJsonObject.getAsJsonPrimitive("per_page").getAsInt());
        searchState.setSearchType(resultJsonObject.has("photos") ? SearchInfo.SEARCH_TYPE_IMAGE : SearchInfo.SEARCH_TYPE_VIDEO);

        JsonArray imagesJsonArray = resultJsonObject.getAsJsonArray("photos");
        imagesJsonArray.forEach(
                imageJsonElement -> {
                    JsonObject imageJsonObject = imageJsonElement.getAsJsonObject();
                    int id = imageJsonObject.getAsJsonPrimitive("id").getAsInt();
                    int width = imageJsonObject.getAsJsonPrimitive("width").getAsInt();
                    int height = imageJsonObject.getAsJsonPrimitive("height").getAsInt();
                    String url = imageJsonObject
                            .getAsJsonObject("src")
                            .getAsJsonPrimitive("original")
                            .getAsString();
                    int photographerId = imageJsonObject.getAsJsonPrimitive("photographer_id").getAsInt();
                    String photographerName = imageJsonObject.getAsJsonPrimitive("photographer").getAsString();

                    PexelsImage pexelsImage = new PexelsImage(id, width, height, url, photographerId, photographerName);
                    pexelsElementList.add(pexelsImage);
                }
        );

        SearchResult searchResult = new SearchResult(searchState, pexelsElementList, (Class) typeOfT);

        return searchResult;
    }
}
