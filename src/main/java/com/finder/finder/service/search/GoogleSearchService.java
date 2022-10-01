package com.finder.finder.service.search;

import com.google.gson.JsonObject;

public interface GoogleSearchService {
    JsonObject search(final String query, String location);
}
