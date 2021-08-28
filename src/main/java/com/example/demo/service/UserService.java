package com.example.demo.service;

import com.example.demo.entity.user.User;
import com.example.demo.exception.user.DuplicateUserLogin;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UserPasswordSmall;

import java.util.List;

public interface UserService {

    User registration(User user) throws DuplicateUserLogin, UserPasswordSmall;

    List<User> getAll();

    User findByLogin(String login) throws UserNotFoundException;

    User findById(Long id) throws UserNotFoundException;

    User deleteById(Long id) throws UserNotFoundException;

}
