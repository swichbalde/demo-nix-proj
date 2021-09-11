package com.example.demo.repository;

import com.example.demo.entity.userlist.UserListEntity;
import com.example.demo.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserListRepository extends CrudRepository<UserListEntity, Long> {
    UserListEntity findAllById(Long id);

    List<UserListEntity> findAllByUser(User user);
}
