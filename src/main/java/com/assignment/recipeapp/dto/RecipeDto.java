package com.assignment.recipeapp.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed {max} characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed {max} characters")
    private String description;

    @Valid
    @NotEmpty(message = "Ingredients cannot be empty")
    private List<@NotNull IngredientDto> ingredients;

    @NotBlank(message = "Instructions cannot be blank")
    private String instructions;

    private boolean vegetarian;

    @Positive(message = "Servings must be positive")
    private int servings;

    // getters and setters
}


