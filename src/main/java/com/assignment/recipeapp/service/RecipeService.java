package com.assignment.recipeapp.service;

import com.assignment.recipeapp.dto.RecipeDto;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.entity.Recipe;
import com.assignment.recipeapp.exception.IngredientNotFoundException;
import com.assignment.recipeapp.exception.RecipeNotFoundException;
import com.assignment.recipeapp.mapper.ObjectMapper;
import com.assignment.recipeapp.repository.IngredientRepository;
import com.assignment.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.assignment.recipeapp.repository.search.RecipeSearchSpecification.searchByText;

/**
 * Service for Recipe entities.
 * this class is used to create, update, delete and search recipes
 *
 * @author Boris Becker
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    private final ObjectMapper objectMapper;

    /**
     * creates a recipe
     *
     * @param recipeUpdateRequest the recipe to create
     * @return the created recipe
     */
    public RecipeListingElementDto createRecipe(RecipeUpdateRequest recipeUpdateRequest) {
        List<Ingredient> ingredients = ingredientRepository.findAllById(recipeUpdateRequest.getIngredients()
                .stream().map(RecipeUpdateRequest.IngredientRequest::getId).toList());
        Recipe recipe = objectMapper.toRecipe(recipeUpdateRequest);
        recipe.setIngredients(new HashSet<>(ingredients));
        recipe = recipeRepository.save(recipe);
        return objectMapper.toRecipeListingElementDto(recipe);
    }

    /**
     * updates a recipe
     *
     *
     * @param id the id of the recipe to update
     * @param recipeDto the recipe to update
     * @return the recipe
     */
    @Transactional
    public RecipeDto updateRecipe(Long id, RecipeUpdateRequest recipeDto) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        recipe.setName(recipeDto.getName());
        recipe.setVegetarian(recipeDto.getVegetarian());
        recipe.setServings(recipeDto.getServings());
        recipe.setInstructions(recipeDto.getInstructions());

        Set<Ingredient> ingredients = new HashSet<>();
        if (recipeDto.getIngredients() != null) {
            for (RecipeUpdateRequest.IngredientRequest ingredientDto : recipeDto.getIngredients()) {
                Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientDto.getId());
                Ingredient ingredient = optionalIngredient.orElseThrow(() -> new IngredientNotFoundException(ingredientDto.getId()));
                ingredients.add(ingredient);
            }
            recipe.setIngredients(ingredients);
        }


        recipe = recipeRepository.save(recipe);
        return objectMapper.toRecipeDto(recipe);

    }

    /**
     * deletes a recipe
     *
     * @param id the id of the recipe to delete
     */
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        recipeRepository.delete(recipe);
    }

    /**
     * gets a recipe
     *
     * @param id the id of the recipe to get
     * @return the recipe
     */
    public RecipeDto getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        return objectMapper.toRecipeDto(recipe);
    }

    /**
     * searches recipes
     * this method is used to search recipes by vegetarian, servings and ingredients
     * it uses the Specification pattern to build the query dynamically based on the search criteria
     *
     * @param criteria the search criteria
     * @return the recipes
     */
    @Transactional
    public Page<RecipeListingElementDto> searchRecipes(RecipeSearchRequest recipeSearchRequest, Pageable pageable) {
        return recipeRepository.findAll(searchByText(recipeSearchRequest), pageable)
                .map(objectMapper::toRecipeListingElementDto);
    }

}


