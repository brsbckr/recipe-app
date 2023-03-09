package com.assignment.recipeapp.controller;

import com.assignment.recipeapp.dto.RecipeDto;
import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.dto.request.RecipeUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientResponse;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class RecipeControllerTests {

    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_NAME = "Test Recipe";

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeController recipeController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .build();
    }

    @Test
    public void testGetRecipeById() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(RECIPE_ID);
        recipeDto.setName(RECIPE_NAME);

        Mockito.when(recipeService.getRecipe(RECIPE_ID)).thenReturn(recipeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(RECIPE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(RECIPE_NAME));
    }

    @Test
    public void testGetAllRecipes() {

        // create sample search criteria and pageable object
        RecipeSearchRequest searchCriteria = new RecipeSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        // create a sample recipe listing element DTO
        RecipeListingElementDto recipeListingElementDto = new RecipeListingElementDto(
                RECIPE_ID,
                RECIPE_NAME,
                null,
                null,
                null,
                null,
                null
        );

        // create a sample page of recipe listing element DTOs
        Page<RecipeListingElementDto> recipeListingElementDtoPage = new PageImpl<>(Collections.singletonList(recipeListingElementDto), pageable, 1);
        // mock the recipe service method to return the sample page of recipe listing element DTOs
        Mockito.when(recipeService.searchRecipes(searchCriteria, pageable)).thenReturn(recipeListingElementDtoPage);
        // make the GET request to the controller and assert the response
        Page<RecipeListingElementDto> responseEntity = recipeController.getRecipes(searchCriteria, pageable);
        assertEquals(1, responseEntity.getContent().size());
        assertEquals(recipeListingElementDtoPage, responseEntity);
        // verify that the recipe service method was called with the correct parameters
        Mockito.verify(recipeService).searchRecipes(searchCriteria, pageable);
    }

    @Test
    public void testAddRecipe() throws Exception {
        RecipeUpdateRequest recipeDto = new RecipeUpdateRequest();
        recipeDto.setName(RECIPE_NAME);
        recipeDto.setDescription("Test Description");
        recipeDto.setServings(4);
        recipeDto.setInstructions("Test Instructions");
        recipeDto.setVegetarian(false);
        RecipeUpdateRequest.IngredientRequest ingredientRequest = new RecipeUpdateRequest.IngredientRequest();
        ingredientRequest.setId(1L);
        recipeDto.setIngredients(Collections.singletonList(ingredientRequest));
        IngredientResponse ingredientResponse = new IngredientResponse("Test Ingredient");
        RecipeListingElementDto savedRecipe = new RecipeListingElementDto(RECIPE_ID, RECIPE_NAME, "Test Description", Collections.singletonList(ingredientResponse), "Test Instructions", 4, false);

        Mockito.when(recipeService.createRecipe(Mockito.any(RecipeUpdateRequest.class))).thenReturn(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(RECIPE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(RECIPE_NAME));
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName(RECIPE_NAME);

        RecipeDto updatedRecipe = new RecipeDto();
        updatedRecipe.setId(RECIPE_ID);
        updatedRecipe.setName(RECIPE_NAME);

        Mockito.when(recipeService.updateRecipe(Mockito.anyLong(), Mockito.any(RecipeUpdateRequest.class)))
                .thenReturn(updatedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(recipeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(RECIPE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(RECIPE_NAME));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
