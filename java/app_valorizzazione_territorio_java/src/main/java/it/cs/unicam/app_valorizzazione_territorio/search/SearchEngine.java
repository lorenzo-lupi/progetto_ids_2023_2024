package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * This class represents a search engine that can be used to search in a given collection of items
 * of a specific {@link Searchable} and {@link Visualizable}type.
 *
 * @param <T> the type of the searchable items
 */
public class SearchEngine<T extends Searchable & Visualizable> {
    private final Map<Parameter, List<SearchCriterion<?>>> criteria;
    private final Collection<T> collection;

    /**
     * Creates a new search engine that searches in the given collection of searchable items.
     *
     * @param collection the collection of searchable items
     */
    public SearchEngine(Collection<T> collection) {
        this.criteria = new HashMap<>();
        this.collection = collection;
    }

    /**
     * Adds the given {@link BiPredicate} as a criterion to the search engine
     * applying it on the given parameter and reference value.
     * The added criterion will be used to filter the items during subsequent searches on the given parameter
     * and reference value.
     *
     * @param parameter the parameter to apply the criterion on
     * @param predicate the {@link BiPredicate} to add as a criterion
     * @param value the reference value
     */
    public void addCriterion(Parameter parameter, BiPredicate<Object, Object> predicate, Object value) {
        if (parameter == null || predicate == null || value == null)
            throw new IllegalArgumentException("Parameters must not be null");

        addCriterion(parameter, new SearchCriterion<>(predicate, value));
    }

    /**
     * Adds the given criterion to the search engine applying it on the given parameter.
     * The added criterion will be used to filter the items during subsequent searches on the given parameter.
     *
     * @param parameter the parameter to apply the criterion on
     * @param criterion the criterion to add
     */
    public void addCriterion(Parameter parameter, SearchCriterion<?> criterion) {
        if (parameter == null || criterion == null)
            throw new IllegalArgumentException("Parameters must not be null");

        this.criteria.computeIfAbsent(parameter, k -> new ArrayList<>());
        this.criteria.get(parameter).add(criterion);
    }

    /**
     * Resets the search engine, removing all the criteria previously added to it.
     */
    public void resetCriteria() {
        criteria.clear();
    }

    /**
     * Computes and returns the current criterion of the search engine, obtained as the conjunction
     * of all the criteria added to the search engine.
     *
     * @return the current criterion of the search engine
     */
    public Predicate<T> getCurrentPredicate() {
        Predicate<T> predicate = item -> true;
        for (Parameter parameter : criteria.keySet()) {
            for (SearchCriterion<?> criterion : criteria.get(parameter)) {
                predicate = predicate.and(item -> criterion.test(item.getParametersMapping().get(parameter)));
            }
        }
        return predicate;
    }

    /**
     * Performs the search on this engine collection according to the given current criteria
     * and returns the result.
     *
     * @return the result of the search
     */
    public SearchResult<T> search() {
        SearchResult<T> result = new SearchResult<>();
        Predicate<T> predicate = getCurrentPredicate();
        this.collection.stream()
                .filter(predicate)
                .forEach(result::addResult);
        return result;
    }

}
