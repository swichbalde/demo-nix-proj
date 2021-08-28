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
import org.springframework.stereotype.Service;

@Service
public class UserListServiceImpl implements UserListService {

    private final UserListRepository userListRepository;
    private final UserService userService;

    public UserListServiceImpl(UserListRepository userListRepository, UserService userService) {
        this.userListRepository = userListRepository;
        this.userService = userService;
    }

    public void saveUserList(UserListEntity userList, Long id) throws RecommendListIsBlankException,
            RecommendAndBanListException, UserNotFoundException {
        if (userList.getRecommend_list().isBlank())
            throw new RecommendListIsBlankException("Recommend list cannot be blank");
        if (userList.getRecommend_list().equals(userList.getBan_list()))
            throw new RecommendAndBanListException("Recommend list cannot equals ban list");
        User user = userService.findById(id);
        userList.setUser(user);
        userListRepository.save(userList);
    }

    public UserListEntity getUserListById(Long id) throws UserListNotFoundException {
        UserListEntity currentEntity = userListRepository.findAllById(id);
        if (currentEntity == null) {
            throw new UserListNotFoundException("user list not found by this id : " + id);
        }
        return currentEntity;
    }

    public void updateUserListById(Long id, UserListEntity userListEntity) throws UserListNotFoundException, RecommendListIsBlankException {
        UserListEntity currentEntity = userListRepository.findAllById(id);
        if (currentEntity == null) throw new UserListNotFoundException("user list not found by this id : " + id);
        if (userListEntity.getRecommend_list().isBlank()) throw new RecommendListIsBlankException("Recommend list cannot be blank");

        if (!userListEntity.getRecommend_list().isBlank())
            currentEntity.setRecommend_list(userListEntity.getRecommend_list());

        if (!userListEntity.getBan_list().isBlank())
            currentEntity.setBan_list(userListEntity.getBan_list());

        if (!userListEntity.getFilter().isBlank())
            currentEntity.setFilter(userListEntity.getFilter());
        userListRepository.save(currentEntity);
    }
}
