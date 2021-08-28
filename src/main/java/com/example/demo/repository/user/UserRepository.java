package com.example.demo.repository.user;

import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    boolean existsUserByLogin(String login);
}
