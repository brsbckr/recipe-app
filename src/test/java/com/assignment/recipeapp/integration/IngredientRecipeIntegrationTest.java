package com.assignment.recipeapp.integration;

import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.entity.Recipe;
import com.assignment.recipeapp.repository.IngredientRepository;
import com.assignment.recipeapp.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class IngredientRecipeIntegrationTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testAddRecipeToIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Pasta");
        ingredientRepository.save(ingredient);

        Recipe recipe = new Recipe();
        recipe.setName("Spaghetti");
        recipe.setInstructions("Boil pasta and add sauce.");
        recipe.setDescription("Italian dish");
        recipe.setVegetarian(true);
        recipe.setServings(4);
        recipe.getIngredients().add(ingredient);
        recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredient = ingredientRepository.findFirstByNameIgnoreCase("Pasta");
        Optional<Recipe> savedRecipe = recipeRepository.findByName("Spaghetti");
        Ingredient ingredient1 = savedIngredient.get();
        Recipe recipe1 = savedRecipe.get();
        assertNotNull(recipe1);
        assertNotNull(ingredient1);
        assertTrue(recipe1.getIngredients().contains(ingredient1));
    }

    @Test
    public void testRemoveRecipeFromIngredient() {
        // Create an ingredient and add it to a recipe
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");
        Recipe recipe = new Recipe();
        recipe.setName("Fried Rice");
        recipe.setInstructions("Cook rice and mix with veggies and eggs");
        recipe.setDescription("Chinese dish");
        recipe.setVegetarian(false);
        recipe.setServings(4);
        recipe.getIngredients().add(ingredient);
        ingredient.getRecipes().add(recipe);
        recipeRepository.save(recipe);

        // Remove the recipe from the ingredient
        recipe.getIngredients().remove(ingredient);
        recipeRepository.save(recipe);

        // Ensure that the ingredient is removed from the recipe
        recipe = recipeRepository.findByName("Fried Rice").orElse(null);
        assertNotNull(recipe);
        assertFalse(recipe.getIngredients().contains(ingredient));
    }


}

