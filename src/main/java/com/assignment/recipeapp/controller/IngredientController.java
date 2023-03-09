package com.assignment.recipeapp.controller;

import com.assignment.recipeapp.dto.IngredientDto;
import com.assignment.recipeapp.dto.request.IngredientSearchRequest;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.service.IngredientService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * The IngredientController.
 * <p>
 *     This class is responsible for handling requests related to ingredients.
 *     It is annotated with @RestController to indicate that it is a Spring MVC controller.
 *     It is annotated with @RequestMapping to indicate that it handles requests at the /api/ingredients path.
 *     It is annotated with @OpenAPIDefinition to indicate that it implements documentation
 *     It is annotated with @RequiredArgsConstructor to generate a constructor with all final fields.
    * </p>
 */
@RestController
@RequestMapping("/api/ingredients")
@OpenAPIDefinition(info = @Info(title = "Ingredients API", version = "v1"))
@RequiredArgsConstructor
public class IngredientController {

    /**
     * The IngredientService.
     */
    private final IngredientService ingredientService;

    /**
     * Creates a new ingredient.
     *
     * @param ingredientDto the ingredient to create
     * @return the created ingredient
     */
    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@Valid @RequestBody IngredientUpdateRequest ingredientDto) {
        IngredientDto createdIngredient = ingredientService.createIngredient(ingredientDto);
        URI locationUri = URI.create("/api/ingredients/" + createdIngredient.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return ResponseEntity.created(locationUri).headers(headers).body(createdIngredient);
    }

    /**
     * gets an ingredient by id.
      * @param id the id of the ingredient
     * @return the ingredient
     */
    @GetMapping("/{id}")
    public IngredientDto getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id);
    }

    /**
     * Gets all ingredients.
     *
     * @return the list of all ingredients
     */
    @GetMapping("/")
    public Page<IngredientListingElementDto> getIngredients(@ModelAttribute IngredientSearchRequest criteria, Pageable pageable) {
        return ingredientService.searchIngredients(criteria, pageable);
    }

    /**
     * Updates an existing ingredient.
     *
     * @param id the id of the ingredient to update
     * @param ingredientDto the new data for the ingredient
     * @return the updated ingredient
     */
    @PutMapping("/{id}")
    public IngredientDto updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientDto ingredientDto) {
        return ingredientService.updateIngredient(id, ingredientDto);
    }

    /**
     * Deletes an ingredient by id.
     *
     * @param id the id of the ingredient to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredientById(@PathVariable Long id) {
        ingredientService.deleteIngredientById(id);
    }

}
