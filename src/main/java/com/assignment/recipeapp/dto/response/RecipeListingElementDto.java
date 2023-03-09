package com.assignment.recipeapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RecipeListingElementDto (
        Long id,
        String name,
        String description,
        List<IngredientResponse> ingredients,
        String instructions,
        Integer servings,
        Boolean vegetarian

){
}
