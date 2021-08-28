package com.example.demo.exception.recipe;

public class RecipeNotFoundException extends Exception{
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
