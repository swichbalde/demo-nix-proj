package com.example.demo.controller;

import com.example.demo.entity.model.UserLoginModel;
import com.example.demo.entity.user.User;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginModel userLoginModel) {
        String username = userLoginModel.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginModel.getPassword()));
        User user = userService.findByLogin(username);

        String token = jwtTokenProvider.createToken(username, user.getRoles());

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
