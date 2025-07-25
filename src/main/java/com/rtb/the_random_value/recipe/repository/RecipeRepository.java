package com.rtb.the_random_value.recipe.repository;

import com.rtb.the_random_value.recipe.dto.PartialRecipeDTO;
import com.rtb.the_random_value.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("""
            select new com.rtb.the_random_value.recipe.dto.PartialRecipeDTO(r.id, r.recipeTitle) from ai_app_recipe r 
            ORDER BY r.id DESC
            """)
    List<PartialRecipeDTO> findAllWithPartialData();
}