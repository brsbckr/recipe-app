package com.assignment.recipeapp.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private Long id;
    @NotBlank
    @Size(max = 50, message = "Name cannot exceed {max} characters")
    private String name;

}


