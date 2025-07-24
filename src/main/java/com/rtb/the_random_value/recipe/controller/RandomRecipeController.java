package com.rtb.the_random_value.recipe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rtb.the_random_value.recipe.dto.RecipeResponse;
import com.rtb.the_random_value.recipe.service.RandomRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/the-random-value/recipe")
public class RandomRecipeController {

    private final RandomRecipeService randomRecipeService;

    @GetMapping
    public ResponseEntity<RecipeResponse> getRandomRecipe(@RequestParam(value = "region", required = false) String region,
                                                          @RequestParam(value = "ingredients", required = false) String ingredients,
                                                          @RequestParam(value = "otherConsiderations", required = false) String otherConsiderations
    ) throws JsonProcessingException {

        return ResponseEntity.ok(randomRecipeService.getRandomRecipe(region, ingredients, otherConsiderations));
    }

}
