package com.rtb.the_random_value.recipe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rtb.the_random_value.recipe.dto.PartialRecipeDTO;
import com.rtb.the_random_value.recipe.dto.RecipeDTO;
import com.rtb.the_random_value.recipe.service.RandomRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/the-random-value/recipe")
public class RandomRecipeController {

    private final RandomRecipeService randomRecipeService;

    @GetMapping
    public ResponseEntity<RecipeDTO> getRandomRecipe(@RequestParam(value = "region", required = false) String region,
                                                     @RequestParam(value = "ingredients", required = false) String ingredients,
                                                     @RequestParam(value = "otherConsiderations", required = false) String otherConsiderations
    ) throws JsonProcessingException {

        return ResponseEntity.ok(randomRecipeService.getRandomRecipe(region, ingredients, otherConsiderations));
    }

    @PostMapping("/db")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO dto) {
        return ResponseEntity.ok(randomRecipeService.createRecipe(dto));
    }

    @GetMapping("/db")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        return ResponseEntity.ok(randomRecipeService.getAllRecipes());
    }

    @GetMapping("/db/partial-data")
    public ResponseEntity<List<PartialRecipeDTO>> getAllRecipesWithPartialData() {
        return ResponseEntity.ok(randomRecipeService.getAllRecipeWithPartialData());
    }

    @GetMapping("/db/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(randomRecipeService.getRecipeById(id));
    }

    @PutMapping("/db/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO dto) {
        return ResponseEntity.ok(randomRecipeService.updateRecipe(id, dto));
    }

    @DeleteMapping("/db/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        randomRecipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

}
