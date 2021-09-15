package com.example.demo.controller;

import com.example.demo.entity.userlist.RequestUserListEntity;
import com.example.demo.entity.userlist.UserListEntity;
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
    public ResponseEntity postUserList(@RequestBody RequestUserListEntity requestUserListEntity, @PathVariable Long id) {
        try {
            UserListEntity userListEntity = requestUserListEntity.toUserList(requestUserListEntity);
            userListService.saveUserList(userListEntity, id);
            return ResponseEntity.ok(requestUserListEntity);
        } catch (RecommendListIsBlankException | RecommendAndBanListException | UserNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUserListById(@PathVariable Long id, @RequestBody RequestUserListEntity requestUserListEntity) {
        try {
            UserListEntity userListEntity = requestUserListEntity.toUserList(requestUserListEntity);
            userListService.updateUserListById(id, userListEntity);
            return ResponseEntity.ok(requestUserListEntity);
        }catch (UserListNotFoundException | RecommendListIsBlankException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserListByUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userListService.getUserListById(id));
        }catch (UserListNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }
}
