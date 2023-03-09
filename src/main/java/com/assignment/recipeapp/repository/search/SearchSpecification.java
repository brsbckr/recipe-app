package com.assignment.recipeapp.repository.search;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SearchSpecification<T, U extends SearchRequest> {

    public abstract Set<Path<?>> getSearchableProperties(Root<T> root);

    /**
     * Add extra search predicates
     */
    public List<Predicate> addCustomSearchPredicates(Root<T> root, CriteriaBuilder cb, U searchRequest) {
        return Collections.emptyList();
    }

    private Set<String> parseSearchTerms(String searchText) {
        if (searchText == null) {
            return Collections.emptySet();
        }
        return Set.of(searchText.split(" ")).stream()
                .map(s -> s.replaceAll("[^a-zA-Z0-9]", "")) // keeps alphanumeric
                .map(String::toLowerCase)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
    }

    protected Specification<T> createSearchSpecification(U searchRequest) {
        return (root, query, cb) -> {

            // Parses search terms
            Set<String> searchTerms = parseSearchTerms(searchRequest.getSearchText());

            // Search term predicates
            List<Predicate> searchPredicates = searchTerms.stream()
                    .map(searchTerm -> matchesAnyProperty(getSearchableProperties(root), searchTerm, cb))
                    .collect(Collectors.toList());

            searchPredicates.addAll(addCustomSearchPredicates(root, cb, searchRequest));

            // Each predicate has to match
            return cb.and(searchPredicates.toArray(new Predicate[]{}));
        };
    }

    private Predicate matchesAnyProperty(Set<Path<?>> searchablePaths, String searchTerm, CriteriaBuilder cb) {
        return cb.or(
                searchablePaths.stream()
                        .map(path -> cb.like(cb.lower(path.as(String.class)), '%' + searchTerm + '%'))
                        .toList()
                        .toArray(new Predicate[0])
        );
    }

}
