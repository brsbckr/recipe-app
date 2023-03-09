package com.assignment.recipeapp.repository.search;

import com.assignment.recipeapp.dto.request.RecipeSearchRequest;
import com.assignment.recipeapp.entity.Ingredient;
import com.assignment.recipeapp.entity.Recipe;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeSearchSpecification extends SearchSpecification<Recipe, RecipeSearchRequest>  {

    public static Specification<Recipe> searchByText(RecipeSearchRequest searchRequest) {
        return new RecipeSearchSpecification().createSearchSpecification(searchRequest);
    }

    @Override
    public List<Predicate> addCustomSearchPredicates(Root<Recipe> root, CriteriaBuilder cb, RecipeSearchRequest searchRequest) {
        List<Predicate> predicates = new ArrayList<>();

        if (!searchRequest.getName().isEmpty()) {
            CriteriaBuilder.In<String> inClause = cb.in(root.get("name"));
            searchRequest.getName().forEach(inClause::value);
            predicates.add(inClause);
        }
        if (searchRequest.getServings() != null) {
            predicates.add(cb.equal(root.get("servings"), searchRequest.getServings()));
        }
        if (searchRequest.getVegetarian() != null) {
            predicates.add(cb.equal(root.get("vegetarian"), searchRequest.getVegetarian()));
        }
        if (searchRequest.getIncludeIngredients() != null && !searchRequest.getIncludeIngredients().isEmpty()) {
            Join<Recipe, Ingredient> join = root.join("ingredients");

            searchRequest.getIncludeIngredients()
                    .stream()
                    .filter(ingredientName -> !ingredientName.isEmpty())
                    .map(ingredientName -> cb.like(join.get("name"), "%" + ingredientName + "%"))
                    .forEach(predicates::add);
        }
        if (searchRequest.getExcludeIngredients() != null && !searchRequest.getExcludeIngredients().isEmpty()) {
            Join<Recipe, Ingredient> join = root.join("ingredients");

            searchRequest.getExcludeIngredients()
                    .stream()
                    .filter(ingredientName -> !ingredientName.isEmpty())
                    .map(ingredientName -> cb.notLike(join.get("name"), "%" + ingredientName + "%"))
                    .forEach(predicates::add);
        }

        return predicates;
    }
        @Override
    public Set<Path<?>> getSearchableProperties(Root<Recipe> root) {
        return Set.of(
                root.get("name"),
                root.get("vegetarian"),
                root.get("servings"),
                root.get("ingredients")
        );
    }
}
