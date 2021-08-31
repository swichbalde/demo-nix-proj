package com.example.demo;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    RecipeRepository recipeRepository;

    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeRepository = mock(RecipeRepository.class);
        recipeService = mock(RecipeService.class);
    }

    @Test
    void testRecipeById() {
        var absentId = 125L;
        var presentId = 1L;
        var recipe = new RecipeEntity();
        recipe.setRecipeName("aa");
        recipe.setIngredients("aa aa");
        recipe.setCost(10);
        recipe.setId(presentId);
        recipe.setCookTime(10);
        recipe.setCalories(10);

        when(recipeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(recipeRepository.findById(presentId)).thenReturn(Optional.of(recipe));

        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    void testAllRecipe() {
        List<RecipeEntity> mockList;
        mockList = mock(recipeRepository.getRecipeEntities().getClass());
        when(mockList.size()).thenReturn(5);
        assertEquals(5, mockList.size());

        verifyNoMoreInteractions(recipeService);
    }

    @Test
    void testSaveRecipe() {
        long id = 1;
        var recipe = new RecipeEntity();
        recipe.setRecipeName("aa");
        recipe.setIngredients("aa aa");
        recipe.setCost(10);
        recipe.setId(id);
        recipe.setCookTime(10);
        recipe.setCalories(10);

        when(recipeRepository.save(notNull())).thenAnswer(invocation -> {
            RecipeEntity entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNotNull();
            assertThat(entity.getRecipeName()).isEqualTo(recipe.getRecipeName());
            assertThat(entity.getIngredients()).isEqualTo(recipe.getIngredients());
            entity.setId(id);
            return entity;
        });

        RecipeEntity recipeEntity = recipeRepository.save(recipe);
        when(recipeEntity).thenReturn(recipe);

        assertEquals(recipeEntity.getId(), id);
    }
}
