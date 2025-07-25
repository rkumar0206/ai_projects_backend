package com.rtb.the_random_value.recipe.mapper;

import com.rtb.the_random_value.recipe.dto.PartialRecipeDTO;
import com.rtb.the_random_value.recipe.dto.RecipeDTO;
import com.rtb.the_random_value.recipe.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy =  ReportingPolicy.IGNORE)
public interface RecipeMapper {


    RecipeDTO toDTO(Recipe entity);

    PartialRecipeDTO toPartialDTO(Recipe entity);

    Recipe toEntity(RecipeDTO dto);
}
