package com.assignment.recipeapp.controller;

import com.assignment.recipeapp.dto.IngredientDto;
import com.assignment.recipeapp.dto.request.IngredientSearchRequest;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.dto.response.RecipeListingElementDto;
import com.assignment.recipeapp.service.IngredientService;
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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class IngredientControllerTests {

    private static final Long INGREDIENT_ID = 1L;

    @MockBean
    private IngredientService ingredientService;

    @MockBean
    private IngredientController ingredientController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        ingredientController = new IngredientController(ingredientService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
                .build();
    }

    @Test
    public void createIngredient_returnsCreatedStatusCode() throws Exception {
        Long id = 1L;
        IngredientDto ingredientDto = new IngredientDto(id, "Salt");

        when(ingredientService.createIngredient(any(IngredientUpdateRequest.class))).thenReturn(ingredientDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ingredientDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/ingredients/" + ingredientDto.getId()))
                .andExpect(jsonPath("$.name").value("Salt"));
    }

    @Test
    public void getIngredientById_returnsIngredient() throws Exception {
        Long id = 1L;
        IngredientDto ingredientDto = new IngredientDto(id, "Salt");

        when(ingredientService.getIngredientById(Mockito.anyLong())).thenReturn(ingredientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ingredients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Salt"));
    }

    @Test
    public void getAllIngredients_returnsListOfIngredients() throws Exception {
        IngredientSearchRequest searchCriteria = new IngredientSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        IngredientListingElementDto ingredientListingElementDto = new IngredientListingElementDto(1L, "Salt");
        Page<IngredientListingElementDto> ingredientListingElementDtoPage = new PageImpl<>(Collections.singletonList(ingredientListingElementDto), pageable, 1);

        when(ingredientService.searchIngredients(searchCriteria, pageable)).thenReturn(ingredientListingElementDtoPage);
        Page<IngredientListingElementDto> responseEntity = ingredientController.getIngredients(searchCriteria, pageable);
        assertEquals(1, responseEntity.getContent().size());
        assertEquals(ingredientListingElementDtoPage, responseEntity);
        // verify that the recipe service method was called with the correct parameters
        Mockito.verify(ingredientService).searchIngredients(searchCriteria, pageable);
    }

    @Test
    void deleteIngredientById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/ingredients/{id}", INGREDIENT_ID))
                .andExpect(status().isNoContent());

        verify(ingredientService).deleteIngredientById(INGREDIENT_ID);
    }

    @Test
    void updateIngredient_ReturnsUpdatedIngredient() throws Exception {
        IngredientDto updatedIngredient = new IngredientDto();
        updatedIngredient.setId(INGREDIENT_ID);
        updatedIngredient.setName("Updated Ingredient");

        when(ingredientService.updateIngredient(eq(INGREDIENT_ID), any(IngredientDto.class)))
                .thenReturn(updatedIngredient);

        String requestBody = "{\"name\": \"Updated Ingredient\", \"description\": \"Updated Ingredient Description\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/ingredients/{id}", INGREDIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(INGREDIENT_ID.intValue()))
                .andExpect(jsonPath("$.name").value(updatedIngredient.getName()));

        verify(ingredientService).updateIngredient(eq(INGREDIENT_ID), any(IngredientDto.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

