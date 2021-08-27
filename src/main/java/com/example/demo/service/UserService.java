package com.example.demo.service;

import com.example.demo.entity.user.User;
import com.example.demo.exception.RecipeListIsBlankException;
import com.example.demo.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    User registration(User user);

    List<User> getAll();

    User findByLogin(String login);

    User findById(Long id) throws UserNotFoundException;

    void deleteById(Long id);
}
