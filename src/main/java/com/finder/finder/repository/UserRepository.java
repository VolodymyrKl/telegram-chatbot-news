package com.finder.finder.repository;

import com.finder.finder.model.User;
import com.finder.finder.model.enums.NewsSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

//    List<User> findAllByIdTelEquals(String id);

    @Query("SELECT u.lastNewsSeen FROM User u WHERE u.idTel = id")
    Map<NewsSource, String> findLastNewsSeenByIdTel(@Param("id") String id);

//    Map<NewsSource, String> findByLastNewsSeenIdTel(String id);

    @Query("UPDATE News n SET n.newsSource = newsSource")
// find news correcpongind to user and update it
    void updateLastNewsSeen(@Param("newsSource") String тщ,@Param("newsSource") String newsSource);

}
