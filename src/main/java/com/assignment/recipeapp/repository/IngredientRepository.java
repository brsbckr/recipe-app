package com.assignment.recipeapp.repository;

import com.assignment.recipeapp.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for Ingredient entities.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {

    /**
     * Finds an ingredient by name.
     * @param name the name of the ingredient
     * @return the ingredient
     */
    Optional<Ingredient> findFirstByNameIgnoreCase(@Param("name") String name);

}

