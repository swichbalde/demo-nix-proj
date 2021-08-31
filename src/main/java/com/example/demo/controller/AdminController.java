package com.example.demo.controller;

import com.example.demo.entity.model.UserLoginModel;
import com.example.demo.entity.model.UserModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.DuplicateUserLogin;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UserPasswordSmall;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUserById(@PathVariable String id) {
        User resultUser;
        try {
            resultUser = userService.findById(Long.valueOf(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        UserModel userModel = UserModel.fromUser(resultUser);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity inactiveUser(@PathVariable String id) {
        try {
            userService.deleteById(Long.valueOf(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity getAllUsers() {
        List<User> all = userService.getAll();
        List<UserModel> allUsers = new ArrayList<>();
        for (User user : all) {
            allUsers.add(UserModel.fromUser(user));
        }
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping("/registration")
    public ResponseEntity registrationAdmin(@Valid @RequestBody UserLoginModel userLoginModel) {
        String username = userLoginModel.getUsername();
        User user = new User();
        user.setLogin(username);
        user.setPassword(userLoginModel.getPassword());
        User registrationUser;
        try {
            registrationUser = userService.registrationAdmin(user);
            if (registrationUser.getRoles().get(0) == null) {
                registrationUser.setRoles(userLoginModel.getRoles());
            }
        } catch (UserPasswordSmall | DuplicateUserLogin e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        String token = jwtTokenProvider.createToken(username, registrationUser.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
