package com.example.demo.service.impl;

import com.example.demo.entity.userlist.RequestUserListEntity;
import com.example.demo.entity.userlist.UserListEntity;
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

import java.util.List;
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
            log.warn("IN saveUserList recommend list cannot be blank");
            throw new RecommendListIsBlankException("Recommend list cannot be blank");
        }

        if (userList.getRecommendList().equals(userList.getBanList())) {
            log.warn("IN saveUserList recommend list cannot be equals to ban list");
            throw new RecommendAndBanListException("Recommend list cannot equals ban list");
        }
        User user = userService.findById(userId);
        userList.setUser(user);
        userListRepository.save(userList);
        log.info("IN saveUserList userlist successfully saved");
        return userList;
    }

    public RequestUserListEntity getUserListById(Long id) throws UserListNotFoundException, UserNotFoundException {
        List<UserListEntity> userListEntityList = userListRepository.findAllByUser(userService.findById(id));
        if (userListEntityList.isEmpty()) {
            log.warn("IN getUserListById cannot found userList by user id: {}", id);
            throw new UserListNotFoundException("cannot found userList by user id: " + id);
        }

        log.info("IN getUserListById size of list {} elements: {}",userListEntityList.size(), userListEntityList);
        UserListEntity userListEntity = userListEntityList.get(userListEntityList.size() - 1);

        return new RequestUserListEntity(userListEntity.getRecommendList(), userListEntity.getBanList(), userListEntity.getFilter());
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

        currentEntity.setRecommendList(userListEntity.getRecommendList());
        currentEntity.setBanList(userListEntity.getBanList());
        currentEntity.setFilter(userListEntity.getFilter());

        userListRepository.save(currentEntity);
    }
}

