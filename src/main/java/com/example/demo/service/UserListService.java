package com.example.demo.service;

import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.list.RecommendAndBanListException;
import com.example.demo.exception.list.RecommendListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.user.UserNotFoundException;

public interface UserListService {
    UserListEntity saveUserList(UserListEntity userList, Long userId) throws RecommendListIsBlankException,
            RecommendAndBanListException, UserNotFoundException;

    UserListEntity getUserListById(Long id) throws UserListNotFoundException;

    void updateUserListById(Long id, UserListEntity userListEntity) throws UserListNotFoundException, RecommendListIsBlankException;
}
