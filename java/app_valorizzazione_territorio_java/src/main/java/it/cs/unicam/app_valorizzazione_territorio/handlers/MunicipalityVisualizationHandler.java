package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalitySOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * Controller class for the visualization of municipalities.
 */
public class MunicipalityVisualizationHandler {
    private final SearchEngine<Municipality> searchEngine =
            new SearchEngine<>(MunicipalityRepository.getInstance().getItemStream().toList());

    /**
     * Returns the Synthesized Format of all the municipalities registered in the system.
     *
     * @return the Synthesized Format of all the municipalities in the system
     */
    public static List<MunicipalitySOF> viewAllMunicipalities() {
        return MunicipalityRepository.getInstance().getItemStream()
                .map(Municipality::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the municipalities registered in the system
     * corresponding to the given filters, all applied in logical and.
     *
     * @param filters the filters to apply
     * @return the Synthesized Format of all the municipalities in the system corresponding to the given filters
     */
    public static List<MunicipalitySOF> viewFilteredMunicipalities(List<SearchFilter> filters) {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(MunicipalityRepository.getInstance().getItemStream().toList());
        filters.forEach(filter -> searchEngine.addCriterion(
                Parameter.stringToParameter.get(filter.parameter()),
                SearchCriterion.stringToBiPredicate.get(filter.predicate()),
                filter.value()));

        return searchEngine.search().getResults().stream()
                .map(Municipality::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Detailed Format of a Municipality having the given ID.
     *
     * @param municipalityID the ID of the Municipality to visualize
     * @return the Detailed Format of the Municipality having the given ID
     * @throws IllegalArgumentException if the Municipality having the given ID is not found
     **/
    public static MunicipalityDOF viewMunicipality(long municipalityID) {
        Municipality municipality = MunicipalityRepository.getInstance().getItemByID(municipalityID);
        if (municipality == null)
            throw new IllegalArgumentException("Municipality not found");

        return municipality.getDetailedFormat();
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
     * @param parameter the municipality parameter to apply the criterion on
     * @param criterion the criterion to add
     * @param value the reference value
     * @throws IllegalArgumentException if any of the parameters is null or invalid
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
     * Returns the search result based on the previously submitted search criterion.
     *
     * @return the search result of the search engine
     */
    public List<MunicipalitySOF> getSearchResult(){
        return this.searchEngine.search().getResults().stream()
                .map(Municipality::getSynthesizedFormat)
                .toList();
    }
}
