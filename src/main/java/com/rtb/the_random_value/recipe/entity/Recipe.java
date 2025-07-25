package com.rtb.the_random_value.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ai_app_recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipeTitle;

    @Column(length = 2000)
    private String description;
    private String yield;
    private String prepTime;
    private String cookTime;

    @Column(length = 1000)
    private String imagePrompt;

    @ElementCollection
    @CollectionTable(name = "ai_app_recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient", length = 1000)
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "ai_app_recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "instruction", length = 1000)
    private List<String> instructions;

}
