package com.example.demo.controller;

import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.RecommendAndBanListException;
import com.example.demo.exception.RecommendListIsBlankException;
import com.example.demo.exception.UserListNotFoundException;
import com.example.demo.service.UserListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
public class UserListController {

    private final UserListService userListService;

    public UserListController(UserListService userListService) {
        this.userListService = userListService;
    }

    @PostMapping("/recommend")
    public ResponseEntity<String> postUserList(@RequestBody UserListEntity userListEntity) {
        try {
            userListService.saveUserList(userListEntity);
            return ResponseEntity.ok("user_list created");
        } catch (RecommendListIsBlankException | RecommendAndBanListException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUserList(@PathVariable String id, @RequestBody UserListEntity userListEntity) {
        try {
            userListService.updateUserListById(Long.valueOf(id), userListEntity);
            return ResponseEntity.ok("user_list patched");
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
