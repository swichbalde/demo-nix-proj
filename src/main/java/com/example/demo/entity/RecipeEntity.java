package com.example.demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String recipeName;

    @NotBlank
    private String ingredients;

    @NotBlank
    private Integer cookTime;

    @NotBlank
    private Integer calories;

    @NotBlank
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
