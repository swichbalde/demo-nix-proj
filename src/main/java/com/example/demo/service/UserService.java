package com.example.demo.service;

import com.example.demo.entity.user.User;
import com.example.demo.exception.DuplicateUserLogin;
import com.example.demo.exception.RecipeListIsBlankException;
import com.example.demo.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    User registration(User user) throws DuplicateUserLogin;

    List<User> getAll();

    User findByLogin(String login) throws UserNotFoundException;

    User findById(Long id) throws UserNotFoundException;

    User deleteById(Long id) throws UserNotFoundException;

}
