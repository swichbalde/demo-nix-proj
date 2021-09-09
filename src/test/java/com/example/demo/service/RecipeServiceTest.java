package com.example.demo.service;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UserListEntity;
import com.example.demo.entity.user.User;
import com.example.demo.exception.recipe.RecipeNotFoundException;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UserListRepository;
import com.example.demo.service.impl.RecipeServiceImpl;
import com.example.demo.util.MapUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    RecipeRepository recipeRepository;
    UserListRepository userListRepository;
    MapUtils mapUtils;
    UserService userService;

    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeRepository = mock(RecipeRepository.class);
        userListRepository = mock(UserListRepository.class);
        mapUtils = mock(MapUtils.class);
        userService = mock(UserService.class);
        recipeService = new RecipeServiceImpl(recipeRepository, userListRepository, mapUtils, userService);
    }

    @Test
    void testAllRecipe() throws Exception {
        List<RecipeEntity> allRecipeEntity = recipeService.getAllRecipeEntity();

        assertEquals(0, allRecipeEntity.size());
        verify(recipeRepository).getRecipeEntities();

        verifyNoMoreInteractions(recipeRepository);
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

        when(recipeService.saveRecipe(notNull())).thenAnswer(invocation -> {
            RecipeEntity entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNotNull();
            assertThat(entity.getRecipeName()).isEqualTo(recipe.getRecipeName());
            assertThat(entity.getIngredients()).isEqualTo(recipe.getIngredients());
            assertThat(entity.getCost()).isEqualTo(recipe.getCost());
            assertThat(entity.getCookTime()).isEqualTo(recipe.getCookTime());
            assertThat(entity.getCalories()).isEqualTo(recipe.getCalories());
            entity.setId(id);
            return entity;
        });

        RecipeEntity recipeEntity = recipeService.saveRecipe(recipe);

        assertThat(recipe.getId()).isEqualTo(id);
        assertThat(recipe.getRecipeName()).isEqualTo(recipeEntity.getRecipeName());
        assertThat(recipe.getIngredients()).isEqualTo(recipeEntity.getIngredients());
        assertThat(recipe.getCost()).isEqualTo(recipeEntity.getCost());
        assertThat(recipe.getCalories()).isEqualTo(recipeEntity.getCalories());
        assertThat(recipe.getCookTime()).isEqualTo(recipeEntity.getCookTime());

    }

    @Test
    void testGetRecipeById() throws Exception {
        long id = 1;
        RecipeEntity recipeEntity = new RecipeEntity(id, "scrumble", "egg", 10, 10, 10);
        recipeService.saveRecipe(recipeEntity);
        User user = new User(id, "swich", "testtest");
        when(userService.registration(user)).thenReturn(user);
        UserListEntity userListEntity = new UserListEntity(id, "salt", "egg", "cost", userService.registration(user));
        when(userService.findById(id)).thenReturn(user);
        when(userListRepository.findAllByUser(userService.findById(id))).thenReturn(List.of(userListEntity));

        when(userListRepository.save(userListEntity)).thenReturn(userListEntity);

        assertThatExceptionOfType(RecipeNotFoundException.class)
                .isThrownBy(() -> recipeService.getRecipeByIngredients(String.valueOf(id)));

        verify(userListRepository).findAllByUser(userService.findById(id));
    }
}
