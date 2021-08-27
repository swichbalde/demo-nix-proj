package com.example.demo.controller;

import com.example.demo.entity.model.UserModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
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
}
