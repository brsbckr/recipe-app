package com.assignment.recipeapp.repository;

import com.assignment.recipeapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Recipe entities.
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    /**
     * Finds a recipe by name.
     * @param name the name of the recipe
     * @return the recipe
     */
    Optional<Recipe> findByName(String name);
}

