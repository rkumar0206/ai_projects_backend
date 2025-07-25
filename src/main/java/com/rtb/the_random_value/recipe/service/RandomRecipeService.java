package com.rtb.the_random_value.recipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtb.common.service.CommonService;
import com.rtb.the_random_value.recipe.dto.RecipeDTO;
import com.rtb.the_random_value.recipe.entity.Recipe;
import com.rtb.the_random_value.recipe.mapper.RecipeMapper;
import com.rtb.the_random_value.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RandomRecipeService {

    private final CommonService commonService;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeDTO getRandomRecipe(String region, String ingredients, String otherConsideration) throws JsonProcessingException {

        String prompt = generatePrompt(region, ingredients, otherConsideration);

        String promptTextResult = commonService.getPromptTextResult(prompt);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(promptTextResult.replace("```", "").replace("json", ""), RecipeDTO.class);
    }

    public RecipeDTO createRecipe(RecipeDTO dto) {
        Recipe recipe = recipeMapper.toEntity(dto);
        return recipeMapper.toDTO(recipeRepository.save(recipe));
    }

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RecipeDTO getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
        return recipeMapper.toDTO(recipe);
    }

    public RecipeDTO updateRecipe(Long id, RecipeDTO dto) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));

        existing.setRecipeTitle(dto.getRecipeTitle());
        existing.setDescription(dto.getDescription());
        existing.setYield(dto.getYield());
        existing.setPrepTime(dto.getPrepTime());
        existing.setCookTime(dto.getCookTime());
        existing.setIngredients(dto.getIngredients());
        existing.setInstructions(dto.getInstructions());
        existing.setImagePrompt(dto.getImagePrompt());

        return recipeMapper.toDTO(recipeRepository.save(existing));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private String generatePrompt(String region, String ingredients, String otherConsideration) {

        return "You are a highly creative culinary expert and recipe generator. Your task is to provide a complete, practical, and edible food recipe based on user specifications.\n" +
                "\n" +
                "**Input Parameters:**\n" +
                "The user may provide the following optional parameters:\n" +
                "*   **`region`**: (Optional) A specific regional cuisine (e.g., \"Italian\", \"Mexican\", \"Thai\", \"Mediterranean\").\n" +
                "*   **`ingredients`**: (Optional) A comma-separated list of key ingredients to incorporate (e.g., \"chicken, rice, spinach\", \"tofu, noodles, peanut butter\").\n" +
                "\n" +
                "**Instructions:**\n" +
                "1.  **Recipe Selection:**\n" +
                "    *   If a `region` is provided, generate a *random* recipe characteristic of that cuisine.\n" +
                "    *   If `ingredients` are provided, generate a *random* recipe that prominently features those ingredients. You may suggest a few additional common pantry staples if necessary to complete the dish.\n" +
                "    *   If both `region` and `ingredients` are provided, generate a *random* recipe that integrates the specified ingredients within the given regional cuisine.\n" +
                "    *   If *neither* `region` nor `ingredients` are provided, generate a *truly random* recipe from any global cuisine. Ensure diversity in dish type, main course, and origin with each request (e.g., don't provide two pasta dishes in a row if no constraints are given).\n" +
                "    *   Also any other consideration provided by the user, keep that in mind as well.\n" +
                "2.  **Recipe Output Format:** Present the recipe in a clear, structured format, including:\n" +
                "{\n" +
                "  \"recipeTitle\": \"RECIPE NAME HERE\",\n" +
                "  \"description\": \"A short overview of the dish.\",\n" +
                "  \"yield\": \"Serves X\",\n" +
                "  \"prepTime\": \"XX minutes\",\n" +
                "  \"cookTime\": \"XX minutes\",\n" +
                "  \"ingredients\": [\n" +
                "    \"1 tbsp olive oil\",\n" +
                "    \"2 garlic cloves, minced\",\n" +
                "    \"1 medium onion, chopped\"\n" +
                "    // ... more ingredients\n" +
                "  ],\n" +
                "  \"instructions\": [\n" +
                "    \"Heat the olive oil in a large pan over medium heat.\",\n" +
                "    \"Add the garlic and onions, and sauté until translucent.\",\n" +
                "    \"Add remaining ingredients and cook as directed.\"\n" +
                "    // ... more steps\n" +
                "  ],\n" +
                "  \"imagePrompt\": \"A image prompt for generating image of the dish.\"\n" +
                "}\n" +
                "\n" +
                "**Constraint:** The recipe must be feasible for a home cook and use commonly available ingredients. Do not provide recipes that require highly specialized equipment or exotic ingredients unless explicitly requested." +
                "Region: " + region + "\n" +
                "Ingredients: " + ingredients + "\n" +
                "Other considerations: " + otherConsideration;
    }
}
