package com.example.demo.controller;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.exception.RecipeNotFoundException;
import com.example.demo.service.impl.RecipeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    final
    RecipeServiceImpl recipeService;

    public RecipeController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/random")
    public ResponseEntity getRandomRecipe() {
        try {
            return ResponseEntity.ok(recipeService.getRandomRecipe());
        }catch (RecipeNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/admin/one")
    public ResponseEntity<String> postRecipe(@RequestBody RecipeEntity recipeEntity) {
        try {
            recipeService.saveRecipe(recipeEntity);
            return ResponseEntity.ok("recipe created");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getRandomRecipe(@PathVariable String id) {
        try {
            return ResponseEntity.ok(recipeService.getRecipeByIngredients(id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity getAllRecipes() {
        try {
            return ResponseEntity.ok(recipeService.getAllRecipeEntity());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
