package com.example.demo.entity.model;

import com.example.demo.entity.user.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserLoginModel {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private List<Role> roles;
}
