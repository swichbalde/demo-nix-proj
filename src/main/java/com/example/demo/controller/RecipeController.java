package com.example.demo.controller;

import com.example.demo.exception.list.RecipeListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.recipe.RecipeNotFoundException;
import com.example.demo.service.impl.RecipeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeServiceImpl recipeService;

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
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getRecipeByIdWithFilters(@PathVariable String id) {
        try {
            return ResponseEntity.ok(recipeService.getRecipeByIngredients(id));
        } catch (UserListNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity getAllRecipes() {
        try {
            return ResponseEntity.ok(recipeService.getAllRecipeEntity());
        }catch (RecipeListIsBlankException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Unknown error");
        }
    }
}
