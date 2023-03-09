package com.assignment.recipeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends ResponseStatusException {

    public EntityNotFoundException(String entityName, Long entityId) {
        super(HttpStatus.NOT_FOUND, String.format("%s with id %d not found.", entityName, entityId));
    }
}

