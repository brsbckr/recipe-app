package com.assignment.recipeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.FOUND)
public class EntityAlreadyExistsException extends ResponseStatusException {

    public EntityAlreadyExistsException(String entityName, String name) {
        super(HttpStatus.FOUND, String.format("%s with name %s already exists.", entityName, name));
    }
}
