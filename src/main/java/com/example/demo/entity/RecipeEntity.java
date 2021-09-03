package com.example.demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String recipeName;

    private String ingredients;
    private Integer cookTime;
    private Integer calories;
    private Integer cost;

    public RecipeEntity(Long id, String recipeName, String ingredients, Integer cookTime, Integer calories, Integer cost) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
        this.calories = calories;
        this.cost = cost;
    }
}
