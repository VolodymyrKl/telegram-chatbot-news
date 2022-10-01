package com.finder.finder.service.search.impl;

import com.finder.finder.service.search.GoogleSearchService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import serpapi.GoogleSearch;
import serpapi.SerpApiSearchException;

import java.util.HashMap;
import java.util.Map;

import static serpapi.SerpApiSearch.API_KEY_NAME;

@Service
public class DefaultGoogleSearchService implements GoogleSearchService {
    @Override
    public JsonObject search(final String query, String location) {
        JsonObject firstResult = null;
        Map<String, String> parameter = new HashMap<>();
        parameter.put("q", query);
        parameter.put("location", location);
        parameter.put(API_KEY_NAME, "674a4b624e7942d40f5f92b306cf23df4f09ceb3ed9d9e5965d9f10cbb023421");

        GoogleSearch search = new GoogleSearch(parameter);
        try {
            JsonObject data = search.getJson();
            JsonElement localResults = data.get("local_results");
            JsonElement places = localResults.getAsJsonObject().get("places");
            JsonArray asJsonArray = places.getAsJsonArray();
            firstResult = asJsonArray.get(0).getAsJsonObject();
        } catch (SerpApiSearchException e) {
            System.out.println("Exception detected: " + e);
        }
        return firstResult;
    }
}
