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
public class MunicipalityVisualizationHandler extends SearchHandler<Municipality> {

    public MunicipalityVisualizationHandler() {
        super(MunicipalityRepository.getInstance().getItemStream().toList());
    }

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
    @SuppressWarnings("unchecked")
    public static List<MunicipalitySOF> viewFilteredMunicipalities(List<SearchFilter> filters) {
        return (List<MunicipalitySOF>) getFilteredItems(MunicipalityRepository.getInstance().getItemStream().toList(), filters);
    }

    /**
     * Returns the Detailed Format of a Municipality having the given ID.
     *
     * @param municipalityID the ID of the Municipality to visualize
     * @return the Detailed Format of the Municipality having the given ID
     * @throws IllegalArgumentException if the Municipality having the given ID is not found
     **/
    public static MunicipalityDOF viewMunicipality(long municipalityID) {
        return MunicipalityRepository.getInstance().getItemByID(municipalityID).getDetailedFormat();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MunicipalitySOF> getSearchResult(){
        return (List<MunicipalitySOF>) super.getSearchResult();
    }
}
