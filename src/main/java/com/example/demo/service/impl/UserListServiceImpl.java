package com.example.demo.service.impl;

import com.example.demo.entity.UserListEntity;
import com.example.demo.entity.user.User;
import com.example.demo.exception.list.RecommendAndBanListException;
import com.example.demo.exception.list.RecommendListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.repository.UserListRepository;
import com.example.demo.service.UserListService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserListServiceImpl implements UserListService {

    private final UserListRepository userListRepository;
    private final UserService userService;

    public UserListServiceImpl(UserListRepository userListRepository, UserService userService) {
        this.userListRepository = userListRepository;
        this.userService = userService;
    }

    public UserListEntity saveUserList(UserListEntity userList, Long userId) throws RecommendListIsBlankException,
            RecommendAndBanListException, UserNotFoundException {
        if (userList.getRecommendList().isBlank()) {
            log.warn("Recommend list cannot be blank");
            throw new RecommendListIsBlankException("Recommend list cannot be blank");
        }
        if (userList.getRecommendList().equals(userList.getBanList())) {
            log.warn("Recommend list cannot be equals to ban list");
            throw new RecommendAndBanListException("Recommend list cannot equals ban list");
        }
        User user = userService.findById(userId);
        userList.setUser(user);
        userListRepository.save(userList);
        return userList;
    }

    public UserListEntity getUserListById(Long id) throws UserListNotFoundException {
        Optional<UserListEntity> tmpUserList = userListRepository.findById(id);
        return tmpUserList.orElseThrow(() -> {
            log.warn("IN getUserListById user list not found by this id : {}", id);
            return new UserListNotFoundException("user list not found by this id : " + id);
        });
    }

    public void updateUserListById(Long id, UserListEntity userListEntity) throws UserListNotFoundException, RecommendListIsBlankException {
        Optional<UserListEntity> tmpUserList = userListRepository.findById(id);
        UserListEntity currentEntity = tmpUserList.orElseThrow(() -> {
            log.warn("IN updateUserListById user list not found by this id : {}", id);
            return new UserListNotFoundException("user list not found by this id : " + id);
        });

        if (userListEntity.getRecommendList().isBlank()) {
            log.warn("IN updateUserListById recommend list cannot be blank");
            throw new RecommendListIsBlankException("Recommend list cannot be blank");
        }

        if (!userListEntity.getRecommendList().isBlank())
            currentEntity.setRecommendList(userListEntity.getRecommendList());

        if (!userListEntity.getBanList().isBlank())
            currentEntity.setBanList(userListEntity.getBanList());

        if (!userListEntity.getFilter().isBlank())
            currentEntity.setFilter(userListEntity.getFilter());
        userListRepository.save(currentEntity);
    }
}

