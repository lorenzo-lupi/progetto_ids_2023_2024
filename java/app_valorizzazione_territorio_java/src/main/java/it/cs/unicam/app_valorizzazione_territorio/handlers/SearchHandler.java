package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.Collection;
import java.util.List;

public class SearchHandler<S extends Searchable & Visualizable> {

    protected final SearchEngine<S> searchEngine;

    /**
     * Creates a new search handler that searches in the given collection of searchable items.
     *
     * @param collection the collection of searchable items
     */
    public SearchHandler(Collection<S> collection){
        this.searchEngine = new SearchEngine<>(collection);
    }

    /**
     * Performs a search on the given list of searchable items using the given filters.
     * The filters are applied in logical and.
     *
     * @param list the list of searchable items
     * @param filters the filters to apply
     * @return the list of items corresponding to the given filters
     * @param <S> the type of the searchable items
     */
    protected static <S extends Searchable & Visualizable> List<? extends Identifiable> getFilteredItems (
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
     * Starts a new search by resetting the criteria of the search engine.
     */
    public void startSearch(){
        this.searchEngine.resetCriteria();
    }

    /**
     * Adds a new criterion to the search engine.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param parameter the searchable item parameter to apply the criterion on
     * @param criterion the criterion to add
     * @param value the reference value
     * @throws IllegalArgumentException if any of the arguments is null or invalid
     */
    public void setSearchCriterion(String parameter, String criterion, Object value){
        this.setSearchCriterion(new SearchFilter(parameter, criterion, value));
    }

    /**
     * Adds a new criterion to the search engine.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param filter the filter to add
     * @throws IllegalArgumentException if the filter is null or any of the parameters
     * in the filter is null or invalid
     */
    public void setSearchCriterion(SearchFilter filter){
        if (filter == null)
            throw new IllegalArgumentException("The search filter must not be null");

        this.searchEngine.addCriterion(
                Parameter.stringToParameter.get(filter.parameter()),
                SearchCriterion.stringToBiPredicate.get(filter.predicate()),
                filter.value());
    }

    /**
     * Returns the search result based on the previously submitted search criteria.
     *
     * @return the search result of the search engine
     */
    public List<? extends Identifiable> getSearchResult(){
        return this.searchEngine.search().getResults().stream()
                .map(S::getSynthesizedFormat)
                .toList();
    }
}
