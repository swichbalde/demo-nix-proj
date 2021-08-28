package com.example.demo.service;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.exception.RecipeListIsBlankException;
import com.example.demo.exception.RecipeNotFoundException;
import com.example.demo.exception.UserNotFoundException;

import java.util.List;

public interface RecipeService {

    RecipeEntity getRandomRecipe() throws RecipeNotFoundException;

    void saveRecipe(RecipeEntity recipeEntity);

    List<RecipeEntity> getRecipeByIngredients(String id) throws UserNotFoundException;

    List<RecipeEntity> getAllRecipeEntity() throws RecipeListIsBlankException;
}
