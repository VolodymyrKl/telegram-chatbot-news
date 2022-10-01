package com.finder.finder.model;

import com.finder.finder.model.enums.NewsSource;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String idTel;
    private String firstName;
    private String lastName;
    private String userName;
    private LocalDateTime lastTimeSeen;
    @OneToMany
    private List<News> lastNewsSeen;
}
