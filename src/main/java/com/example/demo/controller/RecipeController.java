package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @GetMapping("")
    public ResponseEntity getRecipes() {
        try {
            return ResponseEntity.ok("Work");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }


}
