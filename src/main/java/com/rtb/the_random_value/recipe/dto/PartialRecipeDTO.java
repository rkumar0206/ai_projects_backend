package com.rtb.the_random_value.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartialRecipeDTO {

    private Long id;
    private String recipeTitle;
}
