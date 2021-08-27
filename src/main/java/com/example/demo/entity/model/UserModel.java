package com.example.demo.entity.model;

import com.example.demo.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {
    private Long id;
    private String login;

    public static UserModel fromUser(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setLogin(user.getLogin());

        return userModel;
    }
}
