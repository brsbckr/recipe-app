package com.assignment.recipeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when an ingredient is not found.
 */

public class RecipeNotFoundException extends EntityNotFoundException {

    public RecipeNotFoundException(Long id) {
        super("Recipe", id);
    }
}
