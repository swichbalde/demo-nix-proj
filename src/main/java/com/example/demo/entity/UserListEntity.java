package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recommend_list;
    private String ban_list;
    private String filter;

    public UserListEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(String recommend_list) {
        this.recommend_list = recommend_list;
    }

    public String getBan_list() {
        return ban_list;
    }

    public void setBan_list(String ban_list) {
        this.ban_list = ban_list;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
