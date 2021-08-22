package com.example.demo.repository;

import com.example.demo.entity.UserListEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface UserListRepository extends CrudRepository<UserListEntity, Long> {
}
