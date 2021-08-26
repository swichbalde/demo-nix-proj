package com.example.demo.repository;

import com.example.demo.entity.UserListEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserListRepository extends CrudRepository<UserListEntity, Long> {
    UserListEntity findAllById(Long id);
}
