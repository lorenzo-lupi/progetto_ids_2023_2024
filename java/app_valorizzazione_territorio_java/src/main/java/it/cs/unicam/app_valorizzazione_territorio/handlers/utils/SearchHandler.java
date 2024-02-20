package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SearchHandler<S extends Searchable & Visualizable> {
    protected final SearchEngine<S> searchEngine;

    /**
     * Creates a new search handler that searches in the given collection of searchable items.
     *
     * @param collection the collection of searchable items
     */
    public SearchHandler(Collection<S> collection) {
        this.searchEngine = new SearchEngine<>(collection);
    }

    /**
     * Performs a search on the given list of searchable items using the given filters.
     * The filters are applied in logical and.
     *
     * @param list    the list of searchable items
     * @param filters the filters to apply
     * @param <S>     the type of the searchable items
     * @return the list of items corresponding to the given filters
     */
    public static <S extends Searchable & Visualizable> List<? extends Identifiable> getFilteredItems(
            List<S> list, List<SearchFilter> filters) {

        SearchEngine<S> searchEngine = new SearchEngine<>(list);
        filters.forEach(filter -> searchEngine.addCriterion(
                Parameter.stringToParameter.get(filter.parameter()),
                SearchCriterion.stringToBiPredicate.get(filter.predicate()),
                filter.value()));

        return searchEngine.search().getResults().stream()
                .map(S::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the set of all the criteria available for the search.
     * @return the set of all the criteria available for the search
     */
    public static Set<String> getSearchCriteria(){
        return SearchCriterion.stringToBiPredicate.keySet();
    }
}
