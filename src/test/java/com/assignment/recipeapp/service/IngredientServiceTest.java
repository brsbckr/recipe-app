package com.assignment.recipeapp.service;

import com.assignment.recipeapp.dto.IngredientDto;
import com.assignment.recipeapp.dto.request.IngredientSearchRequest;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.exception.IngredientNotFoundException;
import com.assignment.recipeapp.mapper.ObjectMapper;
import com.assignment.recipeapp.repository.IngredientRepository;
import com.assignment.recipeapp.repository.search.IngredientSearchSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.assignment.recipeapp.repository.search.IngredientSearchSpecification.searchByText;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IngredientService.class, IngredientSearchSpecification.class})
public class IngredientServiceTest {

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientService ingredientService;

    private Ingredient ingredient;
    private IngredientDto ingredientDto;
    private IngredientUpdateRequest ingredientUpdateRequest;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");

        ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        ingredientDto.setName("Test Ingredient");

        ingredientUpdateRequest = new IngredientUpdateRequest();
        ingredientUpdateRequest.setName("Test Ingredient");

    }

    @Test
    public void testCreateIngredient() {
        when(objectMapper.toIngredient(ingredientUpdateRequest)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(objectMapper.toIngredientDto(ingredient)).thenReturn(ingredientDto);

        IngredientDto createdIngredientDto = ingredientService.createIngredient(ingredientUpdateRequest);

        assertEquals(ingredientDto, createdIngredientDto);
        verify(objectMapper).toIngredient(ingredientUpdateRequest);
        verify(ingredientRepository).save(ingredient);
        verify(objectMapper).toIngredientDto(ingredient);
    }

    @Test
    public void testGetIngredientById() {
        Long id = 1L;

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(objectMapper.toIngredientDto(ingredient)).thenReturn(ingredientDto);

        IngredientDto foundIngredientDto = ingredientService.getIngredientById(id);

        assertEquals(ingredientDto, foundIngredientDto);
        verify(ingredientRepository).findById(id);
        verify(objectMapper).toIngredientDto(ingredient);
    }

    @Test
    public void testUpdateIngredient() {
        Long id = 1L;
        IngredientDto updatedIngredientDto = new IngredientDto();
        updatedIngredientDto.setId(id);
        updatedIngredientDto.setName("Updated Test Ingredient");

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(objectMapper.toIngredientDto(ingredient)).thenReturn(ingredientDto);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(objectMapper.toIngredientDto(ingredient)).thenReturn(updatedIngredientDto);

        IngredientDto updatedIngredient = ingredientService.updateIngredient(id, updatedIngredientDto);

        assertEquals(updatedIngredientDto, updatedIngredient);
        assertEquals(updatedIngredientDto.getName(), ingredient.getName());
        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).save(ingredient);
        verify(objectMapper).toIngredientDto(ingredient);
    }

    @Test
    public void testDeleteIngredientById() {
        Long id = 1L;

        when(ingredientRepository.existsById(id)).thenReturn(true);

        ingredientService.deleteIngredientById(id);

        verify(ingredientRepository).existsById(id);
        verify(ingredientRepository).deleteById(id);
    }

    @Test
    void testDeleteIngredientByIdNotFound() {
        Long id = 1L;

        when(ingredientRepository.existsById(id)).thenReturn(false);

        IngredientNotFoundException exception = assertThrows(IngredientNotFoundException.class, () -> {
            ingredientService.deleteIngredientById(id);
        });

        assertEquals("404 NOT_FOUND \"Ingredient with id " + id + " not found.\"", exception.getMessage());
    }


}

