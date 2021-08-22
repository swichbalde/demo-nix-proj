package com.example.demo.controller;

import com.example.demo.entity.UserListEntity;
import com.example.demo.repository.UserListRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
public class UserListController {

    private final UserListRepository userListRepository;

    public UserListController(UserListRepository userListRepository) {
        this.userListRepository = userListRepository;
    }

    @PostMapping("/recommend")
    public ResponseEntity postUserList(@RequestBody UserListEntity userListEntity) {
        try {
            userListRepository.save(userListEntity);
            return ResponseEntity.ok("user created");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
