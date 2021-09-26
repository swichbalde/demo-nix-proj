package com.example.demo.repository;

import com.example.demo.entity.user.User;
import com.example.demo.entity.userlist.UserListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserListRepository extends CrudRepository<UserListEntity, Long> {
    UserListEntity findAllById(Long id);

    List<UserListEntity> findAllByUser(User user);
}
