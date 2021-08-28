package com.example.demo.service.impl;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.Status;
import com.example.demo.entity.user.User;
import com.example.demo.exception.DuplicateUserLogin;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registration(User user) throws DuplicateUserLogin {
        if (userRepository.existsUserByLogin(user.getLogin())) {
            log.warn("User with login {} exist", user.getLogin());
            throw new DuplicateUserLogin("User with login:" + user.getLogin() + " exist");
        }

        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roleList = new ArrayList<>();

        roleList.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleList);
        user.setStatus(Status.ACTIVE);
        user.setCreated(Date.from(Instant.now()));

        User regUser = userRepository.save(user);

        log.info("IN registration: user : {} successfully registered", regUser.getId());
        return regUser;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = userRepository.findAll();
        log.info("IN getAll: {} users found", userList.size());
        return null;
    }

    @Override
    public User findByLogin(String login) throws UserNotFoundException {
        User resultUser = userRepository.findByLogin(login);
        if (resultUser == null) {
            log.warn("IN findByLogin user not found by login: {}", login);
            throw new UserNotFoundException("User with login " + login + " dont found");
        }
        if (resultUser.getStatus() == Status.INACTIVE) {
            log.warn("IN findByLogin user was deleted by login: {}", login);
            throw new UserNotFoundException("User with login " + login + " was deleted");
        }
        log.info("IN findByLogin: found user by id : {}", resultUser.getId());
        return resultUser;
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById no user found by id: {}", id);
            throw new UserNotFoundException("user with id:" + id + " not found");
        }

        if (result.getStatus() == Status.INACTIVE) {
            log.warn("IN findByLogin user was deleted by id: {}", id);
            throw new UserNotFoundException("User with id " + id + " was deleted");
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public User deleteById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
        log.info("IN deleteById: deleted with id: {}, user: {}", id, user);
        return user;
    }
}
