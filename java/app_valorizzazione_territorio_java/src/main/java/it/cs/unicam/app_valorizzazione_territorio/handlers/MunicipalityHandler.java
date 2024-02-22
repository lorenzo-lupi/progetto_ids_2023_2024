package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUltils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.MunicipalityBuilder;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;
import java.util.Set;

/**
 * This class represents a handler for the municipalities.
 * It handlers the creation the visualization of municipalities.
 */
public class MunicipalityHandler {
    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    /**
     * Creates a new municipality and adds it to the repository of municipalities.
     *
     * @param municipalityIF the municipality input format
     * @return the ID of the created municipality
     */
    public static long createMunicipality(MunicipalityIF municipalityIF) {
        MunicipalityBuilder builder = new MunicipalityBuilder();
        builder.buildName(municipalityIF.name())
                .buildDescription(municipalityIF.description())
                .buildPosition(municipalityIF.position())
                .buildCoordinatesBox(municipalityIF.coordinatesBox())
                .buildFiles(municipalityIF.files().stream().toList())
                .build();

        municipalityRepository.add(builder.obtainResult());
        return builder.obtainResult().getID();
    }

    /**
     * Returns the Synthesized Format of all the municipalities registered in the system.
     *
     * @return the Synthesized Format of all the municipalities in the system
     */
    public static List<MunicipalityOF> viewAllMunicipalities() {
        return municipalityRepository.getItemStream()
                .map(Municipality::getOutputFormat)
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
    public static List<MunicipalityOF> viewFilteredMunicipalities(List<SearchFilter> filters) {
        return (List<MunicipalityOF>) SearchUltils
                .getFilteredItems(municipalityRepository.getItemStream().toList(), filters);
    }

    /**
     * Returns the set of all the criteria available for the search.
     *
     * @return the set of all the criteria available for the search
     */
    public static Set<String> getSearchCriteria() {
        return SearchUltils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     * @return the search parameters for the user entity
     */
    public static List<String> getParameters(){
        return (new Municipality("test",
                "desc",
                new Position(5, -5),
                new CoordinatesBox(new Position(0, 0),
                        new Position(10, -10)),
                List.of()))
                .getParameters()
                .stream()
                .map(Object::toString)
                .toList();
    }

    /**
     * Returns the Detailed Format of a Municipality having the given ID.
     *
     * @param municipalityID the ID of the Municipality to visualize
     * @return the Detailed Format of the Municipality having the given ID
     * @throws IllegalArgumentException if the Municipality having the given ID is not found
     **/
    public static MunicipalityOF viewMunicipality(long municipalityID) {
        return municipalityRepository.getItemByID(municipalityID).getOutputFormat();
    }
}
