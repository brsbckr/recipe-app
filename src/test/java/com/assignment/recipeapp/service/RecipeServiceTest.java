package com.assignment.recipeapp.service;

import com.assignment.recipeapp.dto.RecipeDto;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.dto.response.IngredientResponse;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.entity.Recipe;
import com.assignment.recipeapp.exception.IngredientNotFoundException;
import com.assignment.recipeapp.mapper.ObjectMapper;
import com.assignment.recipeapp.repository.IngredientRepository;
import com.assignment.recipeapp.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = { RecipeService.class })
@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private ObjectMapper objectMapper;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createRecipeTest() {
        // Given
        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setName("test recipe");
        recipeUpdateRequest.setVegetarian(true);
        recipeUpdateRequest.setServings(4);
        recipeUpdateRequest.setDescription("test description");
        recipeUpdateRequest.setInstructions("test instructions");
        RecipeUpdateRequest.IngredientRequest ingredientRequest = new RecipeUpdateRequest.IngredientRequest();
        ingredientRequest.setId(1L);
        recipeUpdateRequest.setIngredients(Collections.singletonList(ingredientRequest));
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        RecipeListingElementDto recipeListingElementDto = new RecipeListingElementDto(
                1L,
                "test recipe",
                "test description",
                Collections.singletonList(new IngredientResponse("test ingredient")),
                "test instructions",
                4,
                true
        );
        when(objectMapper.toRecipeListingElementDto(any())).thenReturn(recipeListingElementDto);
        List<Ingredient> ingredients = Collections.singletonList(ingredient);
        when(ingredientRepository.findAllById(anyList())).thenReturn(ingredients);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("test recipe");
        recipe.setVegetarian(true);
        recipe.setServings(4);
        recipe.setInstructions("test instructions");
        when(recipeRepository.save(any())).thenReturn(recipe);
        when(objectMapper.toRecipe(recipeUpdateRequest)).thenReturn(recipe);
        // When
        RecipeListingElementDto result = recipeService.createRecipe(recipeUpdateRequest);
        // Then
        assertNotNull(result);
        assertEquals("test recipe", result.name());
        assertTrue(result.vegetarian());
        assertEquals(4, result.servings());
        assertEquals("test instructions", result.instructions());
    }

    @Test
    public void updateRecipeTest() {
        // Given
        Long id = 1L;
        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setName("updated test recipe");
        recipeUpdateRequest.setVegetarian(true);
        recipeUpdateRequest.setServings(6);
        recipeUpdateRequest.setInstructions("updated test instructions");
        RecipeUpdateRequest.IngredientRequest ingredientRequest = new RecipeUpdateRequest.IngredientRequest();
        ingredientRequest.setId(1L);
        recipeUpdateRequest.setIngredients(Collections.singletonList(ingredientRequest));

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setName("updated test recipe");
        recipeDto.setVegetarian(true);
        recipeDto.setServings(6);
        recipeDto.setInstructions("updated test instructions");
        when(objectMapper.toRecipeDto(any())).thenReturn(recipeDto);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        when(ingredientRepository.findById(any())).thenReturn(Optional.of(ingredient));
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName("test recipe");
        recipe.setVegetarian(false);
        recipe.setServings(4);
        recipe.setInstructions("test instructions");
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);
        // When
        RecipeDto result = recipeService.updateRecipe(id, recipeUpdateRequest);
        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("updated test recipe", result.getName());
        assertTrue(result.isVegetarian());
        assertEquals(6, result.getServings());
        assertEquals("updated test instructions", result.getInstructions());
    }

    @Test
    public void deleteRecipeTest() {
        // Given
        Long id = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        // When
        recipeService.deleteRecipe(id);
        // Then
        verify(recipeRepository, times(1)).delete(recipe);

    }

    @Test
    void updateRecipe_shouldThrowIngredientNotFoundException_whenIngredientNotFound() {
        // Arrange
        Long recipeId = 1L;
        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setName("Updated Recipe");
        recipeUpdateRequest.setInstructions("Updated instructions");
        recipeUpdateRequest.setVegetarian(true);
        recipeUpdateRequest.setServings(4);
        RecipeUpdateRequest.IngredientRequest ingredientRequest = new RecipeUpdateRequest.IngredientRequest();
        ingredientRequest.setId(2L);
        recipeUpdateRequest.setIngredients(Collections.singletonList(ingredientRequest));

        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName("Recipe");
        recipe.setInstructions("Instructions");
        recipe.setVegetarian(false);
        recipe.setServings(2);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IngredientNotFoundException.class, () -> recipeService.updateRecipe(recipeId, recipeUpdateRequest));

        verify(recipeRepository).findById(recipeId);
        verify(ingredientRepository).findById(2L);
        verifyNoMoreInteractions(recipeRepository, ingredientRepository);
    }

}
