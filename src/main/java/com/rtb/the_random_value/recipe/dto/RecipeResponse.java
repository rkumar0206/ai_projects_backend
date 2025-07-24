package com.rtb.the_random_value.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {

    private String recipeTitle;
    private String description;
    private String yield;
    private String prepTime;
    private String cookTime;
    private List<String> ingredients;
    private List<String> instructions;
    private String imagePrompt;
}
