package com.example.demo.service.impl;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.Status;
import com.example.demo.entity.user.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registration(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roleList = new ArrayList<>();

        roleList.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleList);
        user.setStatus(Status.ACTIVE);

        User regUser = userRepository.save(user);

        log.info("IN registration: user - {} successfully registered", regUser.getId());
        return regUser;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = userRepository.findAll();
        log.info("IN getAll: {} users found", userList.size());
        return null;
    }

    @Override
    public User findByLogin(String login) {
        User resultUser = userRepository.findByLogin(login);
        log.info("IN findByLogin: found user by id - {}", resultUser.getId());
        return resultUser;
    }

    @Override
    public User findById(Long id) {
        User resultUser = userRepository.getById(id);
        log.info("IN findById: found user - {}", id);
        return resultUser;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("IN deleteById: deleted with id {}", id);
    }
}
