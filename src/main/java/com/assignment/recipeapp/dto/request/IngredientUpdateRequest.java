package com.assignment.recipeapp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientUpdateRequest {
    @NotBlank
    @Size(max = 50, message = "Name cannot exceed {max} characters")
    String name;
}
