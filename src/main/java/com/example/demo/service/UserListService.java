package com.example.demo.service;

import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.RecommendListIsBlankException;
import com.example.demo.exception.UserListNotFoundException;
import com.example.demo.repository.UserListRepository;
import org.springframework.stereotype.Service;

@Service
public class UserListService {

    private final UserListRepository userListRepository;

    public UserListService(UserListRepository userListRepository) {
        this.userListRepository = userListRepository;
    }

    public void saveUserList(UserListEntity userList) throws RecommendListIsBlankException {
        if (userList.getRecommend_list().isBlank())
            throw new RecommendListIsBlankException("Recommend list cannot be blank");
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
