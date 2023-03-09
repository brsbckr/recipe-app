package com.assignment.recipeapp.integration;

import com.assignment.recipeapp.dto.RecipeDto;
import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.dto.response.IngredientResponse;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.mapper.ObjectMapper;
import com.assignment.recipeapp.repository.IngredientRepository;
import com.assignment.recipeapp.repository.RecipeRepository;
import com.assignment.recipeapp.service.IngredientService;
import com.assignment.recipeapp.service.RecipeService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private IngredientService ingredientService;

    private IngredientUpdateRequest ingredientDto;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private RecipeUpdateRequest recipeUpdateRequest;

    private List<RecipeDto> recipeDtos;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp() {
        ingredientDto = new IngredientUpdateRequest();
        ingredientDto.setName("Test Ingredient");

        recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setName("Test Recipe");
        recipeUpdateRequest.setInstructions("Test instructions");
        recipeUpdateRequest.setDescription("Test description");
        recipeUpdateRequest.setServings(4);
        recipeUpdateRequest.setVegetarian(true);
        Ingredient ingredient = objectMapper.toIngredient(ingredientDto);;
        recipeUpdateRequest.setIngredients(List.of(objectMapper.toIngredientRequest(ingredientRepository.save(ingredient))));

        //recipeDtos = new ArrayList<>();
        //recipeDtos.add(recipeUpdateRequest);
    }

    @Test
    public void testAddRecipe() {
        RecipeListingElementDto savedRecipe = recipeService.createRecipe(recipeUpdateRequest);
        assertNotNull(savedRecipe.id());
        assertEquals(recipeUpdateRequest.getName(), savedRecipe.name());
        assertEquals(recipeUpdateRequest.getInstructions(), savedRecipe.instructions());
        assertEquals(recipeUpdateRequest.getDescription(), savedRecipe.description());
        assertEquals(recipeUpdateRequest.getServings(), savedRecipe.servings());
        assertEquals(recipeUpdateRequest.getVegetarian(), savedRecipe.vegetarian());

        IngredientResponse savedIngredient = savedRecipe.ingredients().iterator().next();
        assertNotNull(savedIngredient.name());
        assertEquals(ingredientDto.getName(), savedIngredient.name());
    }

    @Test
    public void testGetRecipeById() {
        RecipeListingElementDto savedRecipe = recipeService.createRecipe(recipeUpdateRequest);
        RecipeDto retrievedRecipe = recipeService.getRecipe(savedRecipe.id());
        assertEquals(savedRecipe.id(), retrievedRecipe.getId());
    }

    @Test
    public void testUpdateRecipe() {
        RecipeListingElementDto savedRecipe = recipeService.createRecipe(recipeUpdateRequest);
        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setName("Updated Recipe");
        recipeUpdateRequest.setDescription(savedRecipe.description());
        recipeUpdateRequest.setInstructions(savedRecipe.instructions());
        recipeUpdateRequest.setServings(savedRecipe.servings());
        recipeUpdateRequest.setVegetarian(savedRecipe.vegetarian());
        RecipeDto updatedRecipe = recipeService.updateRecipe(savedRecipe.id(), recipeUpdateRequest);
        assertEquals(savedRecipe.id(), updatedRecipe.getId());
        assertEquals("Updated Recipe", updatedRecipe.getName());
    }

    @Test
    public void testDeleteRecipe() {
        RecipeListingElementDto savedRecipe = recipeService.createRecipe(recipeUpdateRequest);
        recipeService.deleteRecipe(savedRecipe.id());
        assertFalse(recipeRepository.findById(savedRecipe.id()).isPresent());
    }

    @Test
    public void testSearchRecipes() {
        RecipeListingElementDto recipeListingElementDto = recipeService.createRecipe(recipeUpdateRequest);

        RecipeSearchRequest criteria = new RecipeSearchRequest();
        criteria.setVegetarian(true);
        criteria.setServings(4);
        criteria.setIncludeIngredients(List.of(""));
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeListingElementDto> foundRecipes = recipeService.searchRecipes(criteria, pageable);

        assertEquals(1, foundRecipes.getTotalElements());
        assertEquals(recipeListingElementDto.name(), foundRecipes.stream().findFirst().get().name());
    }

}

