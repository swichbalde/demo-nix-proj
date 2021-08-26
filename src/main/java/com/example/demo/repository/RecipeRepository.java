package com.example.demo.repository;

import com.example.demo.entity.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
    @Query("select p.id from RecipeEntity p")
    List<Long> getAllIds();

    @Query("select r from RecipeEntity r")
    List<RecipeEntity> getRecipeEntities();
}
