package it.cs.unicam.app_valorizzazione_territorio.osm;


import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.builders.MapBuilder;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides methods to retrieve maps composed by data from the OSM API and geo-localzable from
 * the given municipalities.
 */
public class MapProvider {

    /**
     * Returns an empty map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality, but it
     * contains no geo-locatable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<?> getEmptyMap(Municipality municipality) throws IOException {
        return getEmptyMap(municipality.getCoordinatesBox());
    }

    /**
     * Returns an empty map delimited by the given box.
     * The returned map contains the associated OSM data but no geo-localizable points.
     *
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<?> getEmptyMap(CoordinatesBox box) throws IOException {
        return new MapBuilder<>()
                .buildOsmData(box)
                .build()
                .getResult();
    }

    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the default CoordinateBox of the municipality,
     * and contains all its geo-locatable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<GeoLocatable> getMap(Municipality municipality) throws IOException {
        return getFilteredMap(municipality, municipality.getCoordinatesBox(), List.of());
    }

    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the given CoordinateBox and contains all
     * the geo-localizable points included in the given box.
     *
     * @param municipality the municipality
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<GeoLocatable> getMap(Municipality municipality, CoordinatesBox box) throws IOException {
        return getFilteredMap(municipality, box, List.of());
    }



    /**
     * Returns a map of the Italian territory containing all the municipalities
     * registered in the system.
     *
     * @return a map of the Italian territory
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<Municipality> getMunicipalitiesMap() throws IOException {
        return fact(MunicipalityRepository.getInstance().getItemStream().toList(), CoordinatesBox.ITALY);
    }


    /**
     * Returns a map of a municipality containing all the filtered geo-locatable points.
     * @param municipality the id of the municipality
     * @param filters the filters to apply
     * @return a map of the municipality containing all the filtered geo-locatable points
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<GeoLocatable> getFilteredMap(Municipality municipality,
                                                   List<SearchFilter> filters) throws IOException {
        return getFilteredMap(municipality, municipality.getCoordinatesBox(), filters);
    }


    public static Map<GeoLocatable> getFilteredMap(Municipality municipality,
                                                   CoordinatesBox box,
                                                   List<SearchFilter> filters) throws IOException {

        if(municipality == null || box == null || filters == null)
            throw new IllegalArgumentException("The arguments cannot be null");

        if(!municipality.getCoordinatesBox().contains(box))
            throw new IllegalArgumentException("The box must be contained in the municipality box");

        filters = new LinkedList<>(filters);
        filters.add(new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX", box));

        SearchEngine<GeoLocatable> geoLocatableSearchEngine = new SearchEngine<> (municipality.getGeoLocatables().stream().filter(GeoLocatable::isApproved).toList());

        filters.forEach(filter -> geoLocatableSearchEngine.addCriterion(Parameter.valueOf(filter.parameter()),
                        SearchCriterion.stringToBiPredicate.get(filter.predicate()), filter.value()));

        return fact(geoLocatableSearchEngine.search().getResults(), box);
    }





    private  static <P extends Positionable & Visualizable> Map<P> fact(List<P> geoLocatables, CoordinatesBox box) throws IOException {
        return new MapBuilder<P>()
                .buildOsmData(box)
                .buildPointsList(geoLocatables)
                .build()
                .getResult();
    }
}
