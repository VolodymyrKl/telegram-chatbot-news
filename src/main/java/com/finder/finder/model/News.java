package com.finder.finder.model;

import com.finder.finder.model.enums.NewsSource;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String idTel;
    private String newsSource;

    @ManyToOne
    private User user;
}
