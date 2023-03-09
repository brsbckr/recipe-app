package com.assignment.recipeapp.dto.request;

import com.assignment.recipeapp.repository.search.SearchRequest;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class IngredientSearchRequest implements SearchRequest {
    private Set<Long> ids = new HashSet<>();
    private Set<String> name = new HashSet<>();
    private String searchText;
}
