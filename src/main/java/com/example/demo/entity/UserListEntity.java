package com.example.demo.entity;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recommendList;
    private String banList;
    private String filter;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserListEntity(Long id, String recommendList, String banList, String filter, User user) {
        this.id = id;
        this.recommendList = recommendList;
        this.banList = banList;
        this.filter = filter;
        this.user = user;
    }
}
