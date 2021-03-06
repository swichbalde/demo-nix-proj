package com.example.demo.controller;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.model.ResponseUserAdmin;
import com.example.demo.entity.model.UserLoginModel;
import com.example.demo.entity.model.UserModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.DuplicateUserLogin;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UserPasswordSmall;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.RecipeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.controller.AuthController.getResponseEntity;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RecipeServiceImpl recipeService;

    public AdminController(UserService userService, JwtTokenProvider jwtTokenProvider, RecipeServiceImpl recipeService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.recipeService = recipeService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUserById(@PathVariable String id) {
        User resultUser;
        try {
            resultUser = userService.findById(Long.valueOf(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
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
        List<ResponseUserAdmin> responseUserAdmins;
        try {
            responseUserAdmins = userService.getAll();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(responseUserAdmins);
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
        } catch (UserPasswordSmall | DuplicateUserLogin e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        return getResponseEntity(username, registrationUser, jwtTokenProvider, user.getId());
    }

    @PostMapping("/recipe")
    public ResponseEntity postRecipe(@Valid @RequestBody RecipeEntity recipeEntity) {
        try {
            recipeService.saveRecipe(recipeEntity);
            return ResponseEntity.ok(recipeEntity);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }
}
