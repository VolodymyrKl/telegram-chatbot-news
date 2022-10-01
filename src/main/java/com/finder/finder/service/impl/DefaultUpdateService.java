package com.finder.finder.service.impl;

import com.finder.finder.model.User;
import com.finder.finder.model.enums.NewsSource;
import com.finder.finder.repository.UserRepository;
import com.finder.finder.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultUpdateService implements UpdateService {
    private UserRepository userRepository;

    @Override
    public boolean isTelIdPresent(String id) {
        return userRepository.findLastNewsSeenByIdTel(id) != null;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Map<NewsSource, String> getLastNewsSeen(String id) {
//        userRepository.findByLastNewsSeenIdTel(id);
        return userRepository.findLastNewsSeenByIdTel(id);
    }

    @Override
    public void updateLastNewsSeen(String id) {

    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
