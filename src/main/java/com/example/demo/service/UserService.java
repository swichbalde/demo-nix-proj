package com.example.demo.service;

import com.example.demo.entity.user.User;

import java.util.List;

public interface UserService {

    User registration(User user);

    List<User> getAll();

    User findByLogin(String login);

    User findById(Long id);

    void deleteById(Long id);
}
