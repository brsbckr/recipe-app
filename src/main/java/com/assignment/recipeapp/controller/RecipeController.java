package com.assignment.recipeapp.controller;

import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.service.RecipeService;
import com.assignment.recipeapp.dto.RecipeDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The RecipeController.
 * <p>
 *     This class is responsible for handling requests related to recipes.
 *     It is annotated with @RestController to indicate that it is a Spring MVC controller.
 *     It is annotated with @RequestMapping to indicate that it handles requests at the /api/recipes path.
 *     It is annotated with @OpenAPIDefinition to indicate that it implements documentation
 *     It is annotated with @RequiredArgsConstructor to generate a constructor with all final fields.
 * </p>
 */
@RestController
@RequestMapping("/api/recipes")
@OpenAPIDefinition(info = @Info(title = "Recipes API", version = "v1"))
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Get recipe by id
     * @param id recipe id
     * @return recipe
     */
    @GetMapping("/{id}")
    public RecipeDto getRecipeById(@PathVariable("id") Long id) {
        return recipeService.getRecipe(id);
    }

    /**
     * Add recipe
     * @param recipeUpdateRequest recipe to be added
     * @return added recipe
     */
    @PostMapping
    public ResponseEntity<RecipeListingElementDto> createRecipe(@Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        RecipeListingElementDto savedRecipe = recipeService.createRecipe(recipeUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    /**
     * Update recipe
     * @param id recipe id
     * @param recipeDto recipe to be updated
     * @return updated recipe
     */
    @PutMapping("/{id}")
    public RecipeDto updateRecipe(@PathVariable("id") Long id, @RequestBody RecipeUpdateRequest recipeDto) {
        return recipeService.updateRecipe(id, recipeDto);
    }

    /**
     * Delete recipe
     * @param id recipe id
     * @return no content
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping
        public Page<RecipeListingElementDto> getRecipes(@ModelAttribute RecipeSearchRequest criteria, Pageable pageable) {
        return recipeService.searchRecipes(criteria, pageable);
    }
}
