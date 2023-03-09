package com.assignment.recipeapp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeUpdateRequest  {
    @NotBlank
    @Size(max = 50, message = "Name cannot exceed {max} characters")
    String name;
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed {max} characters")
    String description;
    @Valid
    @NotEmpty(message = "Ingredients cannot be empty")
    List<IngredientRequest> ingredients;
    @NotBlank(message = "Instructions cannot be blank")
    String instructions;
    @Positive(message = "Servings must be positive")
    Integer servings;
    Boolean vegetarian;

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IngredientRequest {
        Long id;
    }
}
