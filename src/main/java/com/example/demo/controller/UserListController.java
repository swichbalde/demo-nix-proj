package com.example.demo.controller;

import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.list.RecommendAndBanListException;
import com.example.demo.exception.list.RecommendListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.service.impl.UserListServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
public class UserListController {

    private final UserListServiceImpl userListService;

    public UserListController(UserListServiceImpl userListService) {
        this.userListService = userListService;
    }

    @PostMapping("/recommend/{id}")
    public ResponseEntity postUserList(@RequestBody UserListEntity userListEntity, @PathVariable Long id) {
        try {
            userListService.saveUserList(userListEntity, id);
            return ResponseEntity.ok(userListEntity);
        } catch (RecommendListIsBlankException | RecommendAndBanListException | UserNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUserList(@PathVariable String id, @RequestBody UserListEntity userListEntity) {
        try {
            userListService.updateUserListById(Long.valueOf(id), userListEntity);
            return ResponseEntity.ok(userListEntity);
        }catch (UserListNotFoundException | RecommendListIsBlankException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneUserList(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userListService.getUserListById(Long.valueOf(id)));
        }catch (UserListNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
