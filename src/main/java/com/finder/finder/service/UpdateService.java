package com.finder.finder.service;

import com.finder.finder.model.User;
import com.finder.finder.model.enums.NewsSource;

import java.util.Map;

public interface UpdateService {

    boolean isTelIdPresent(String id);

    void createUser(User user);

    Map<NewsSource, String> getLastNewsSeen(String id);

    void updateLastNewsSeen(String id);
}
