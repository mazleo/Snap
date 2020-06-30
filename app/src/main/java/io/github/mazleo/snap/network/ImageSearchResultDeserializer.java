package io.github.mazleo.snap.network;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import io.github.mazleo.snap.model.SearchResult;

public class ImageSearchResultDeserializer implements JsonDeserializer<SearchResult> {
    public SearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Log.i("APPDEBUG", json.toString());
        return null;
    }
}
