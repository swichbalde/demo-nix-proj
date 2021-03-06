package com.example.demo.service;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.exception.list.RecipeListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.recipe.RecipeNotFoundException;
import com.example.demo.exception.user.UserNotFoundException;

import java.util.List;

public interface RecipeService {

    RecipeEntity getRandomRecipe() throws RecipeNotFoundException;

    RecipeEntity saveRecipe(RecipeEntity recipeEntity);

    List<RecipeEntity> getRecipeByIngredients(Long id) throws RecipeNotFoundException, UserNotFoundException, UserListNotFoundException;

    List<RecipeEntity> getAllRecipeEntity() throws RecipeListIsBlankException;

}
