package com.example.demo.repository;

import com.example.demo.entity.RecipeEntity;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
}
