package com.assignment.recipeapp.service;

import com.assignment.recipeapp.dto.IngredientDto;
import com.assignment.recipeapp.dto.request.IngredientSearchRequest;
import com.assignment.recipeapp.dto.request.IngredientUpdateRequest;
import com.assignment.recipeapp.dto.response.IngredientListingElementDto;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.exception.EntityAlreadyExistsException;
import com.assignment.recipeapp.exception.IngredientNotFoundException;
import com.assignment.recipeapp.mapper.ObjectMapper;
import com.assignment.recipeapp.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.assignment.recipeapp.repository.search.IngredientSearchSpecification.searchByText;

/**
 * Service for Ingredient entities.
 * This service is responsible for all business logic related to ingredients.
 * It uses the IngredientRepository to access the database.
 * It uses the ObjectMapper to map between entities and DTOs.
 * It throws an IngredientNotFoundException when an ingredient is not found.
 * It throws a ResponseStatusException when an ingredient is not found.
 * @author Boris Becker
 * @since 1.0
 * @version 1.0
 * @see Ingredient
 * @see IngredientDto
 * @see IngredientRepository
 * @see IngredientNotFoundException
 * @see ObjectMapper
 */
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ObjectMapper objectMapper;

    /**
     * Creates an ingredient.
     * @param ingredientDto the ingredient to create
     * @return the created ingredient
     */
    @Transactional
    public IngredientDto createIngredient(IngredientUpdateRequest ingredientDto) {
        ingredientRepository.findFirstByNameIgnoreCase(ingredientDto.getName()).ifPresent(ingredient -> {
            throw new EntityAlreadyExistsException("Ingredient", ingredientDto.getName());
        });
        return objectMapper.toIngredientDto(ingredientRepository.save(objectMapper.toIngredient(ingredientDto)));
    }

    /**
     * gets an ingredient by id.
     * @param id the id of the ingredient to get
     * @return the ingredient
     */
    public IngredientDto getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        return objectMapper.toIngredientDto(ingredient);
    }

    /**
     * get all ingredients.
     * @return a list of all ingredients
     */
    public Page<IngredientListingElementDto> searchIngredients(IngredientSearchRequest ingredientSearchRequest, Pageable pageable) {
        return ingredientRepository.findAll(searchByText(ingredientSearchRequest), pageable)
                .map(objectMapper::toIngredientListingElementDto);
    }

    /**
     * Updates an ingredient.
     * @param id the id of the ingredient to update
     * @param ingredientDto the updated ingredient information
     * @return the updated ingredient
     */
    @Transactional
    public IngredientDto updateIngredient(Long id, IngredientDto ingredientDto) {
        Ingredient optionalIngredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        optionalIngredient.setName(ingredientDto.getName());
        Ingredient updatedIngredient = ingredientRepository.save(optionalIngredient);
        return objectMapper.toIngredientDto(updatedIngredient);
    }

    /**
     * Deletes an ingredient by id.
     * @param id the id of the ingredient to delete
     */
    public void deleteIngredientById(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientNotFoundException(id);
        }
        ingredientRepository.deleteById(id);
    }
}

