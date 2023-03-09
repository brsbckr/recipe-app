package com.assignment.recipeapp.mapper;

import com.assignment.recipeapp.dto.IngredientDto;
import com.assignment.recipeapp.dto.RecipeDto;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Maps between entities and DTOs.
 */
@Component
@Mapper(componentModel = "spring", implementationName = "ObjectMapperImpl")
public interface ObjectMapper {
    @Mapping(target = "description", source = "description")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeListingElementDto toRecipeListingElementDto(Recipe recipe);

    @Mapping(target = "description", source = "description")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDto toRecipeDto(Recipe recipe);

    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "ingredients", source = "ingredients")
    Recipe toRecipe(RecipeUpdateRequest recipeUpdateRequest);

    @Mapping(target = "id", source = "ingredient.id")
    @Mapping(target = "name", source = "ingredient.name")
    IngredientListingElementDto toIngredientListingElementDto(Ingredient ingredient);

    @Mapping(target = "recipes", ignore = true)
    @Mapping(target = "name", source = "ingredientDto.name")
    Ingredient toIngredient(IngredientUpdateRequest ingredientDto);

    @Mapping(target = "id", source = "ingredient.id")
    @Mapping(target = "name", source = "ingredient.name")
    IngredientDto toIngredientDto(Ingredient ingredient);

    @Mapping(target = "id", source = "ingredientDto.id")
    RecipeUpdateRequest.IngredientRequest toIngredientRequest(Ingredient ingredientDto);
}

