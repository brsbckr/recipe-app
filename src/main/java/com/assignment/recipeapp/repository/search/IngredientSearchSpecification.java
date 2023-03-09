package com.assignment.recipeapp.repository.search;


import com.assignment.recipeapp.dto.request.IngredientSearchRequest;
import com.assignment.recipeapp.entity.Ingredient;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IngredientSearchSpecification extends SearchSpecification<Ingredient, IngredientSearchRequest>{

    public static Specification<Ingredient> searchByText(IngredientSearchRequest searchRequest) {
        return new IngredientSearchSpecification().createSearchSpecification(searchRequest);
    }

    @Override
    public List<Predicate> addCustomSearchPredicates(Root<Ingredient> root, CriteriaBuilder cb, IngredientSearchRequest searchRequest) {
        List<Predicate> predicates = new ArrayList<>();

        if (!searchRequest.getIds().isEmpty()) {
            CriteriaBuilder.In<Long> inClause = cb.in(root.get("id"));
            searchRequest.getIds().forEach(inClause::value);
            predicates.add(inClause);
        }

        if (!searchRequest.getName().isEmpty()) {
            CriteriaBuilder.In<String> inClause = cb.in(root.get("name"));
            searchRequest.getName().forEach(inClause::value);
            predicates.add(inClause);
        }

        return predicates;
    }
    @Override
    public Set<Path<?>> getSearchableProperties(Root<Ingredient> root) {
        return Set.of(
                root.get("id"),
                root.get("name")
        );
    }
}
