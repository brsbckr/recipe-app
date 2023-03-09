package com.assignment.recipeapp.dto.request;

import com.assignment.recipeapp.repository.search.SearchRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RecipeSearchRequest implements SearchRequest {

    private Set<String> name = new HashSet<>();
    private Boolean vegetarian;
    private Integer servings;
    private List<String> includeIngredients = new ArrayList<>();
    private List<String> excludeIngredients = new ArrayList<>();
    private String searchText;

    // getters and setters
}

