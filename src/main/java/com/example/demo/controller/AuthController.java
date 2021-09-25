package com.example.demo.controller;

import com.example.demo.entity.model.UserLoginModel;
import com.example.demo.entity.user.User;
import com.example.demo.exception.user.DuplicateUserLogin;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UserPasswordSmall;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginModel userLoginModel) {
        String username = userLoginModel.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginModel.getPassword()));
        User user;
        try {
            user = userService.findByLogin(username);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        return getResponseEntity(username, user, jwtTokenProvider, user.getId());
    }

    static ResponseEntity getResponseEntity(String username, User user, JwtTokenProvider jwtTokenProvider, Long id) {
        String token = jwtTokenProvider.createToken(username, user.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("id", String.valueOf(id));
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@Valid @RequestBody UserLoginModel userLoginModel) {
        String username = userLoginModel.getUsername();
        User user = new User();
        user.setLogin(username);
        user.setPassword(userLoginModel.getPassword());
        User registrationUser;
        try {
            registrationUser = userService.registration(user);
        } catch (UserPasswordSmall | DuplicateUserLogin e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }

        String token = jwtTokenProvider.createToken(username, registrationUser.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("id", String.valueOf(user.getId()));
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

}
