package it.cs.unicam.app_valorizzazione_territorio.osm;


import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;

import java.io.IOException;

/**
 * This class provides methods to retrieve maps composed by data from the OSM API and geo-localzable from
 * the given municipalities.
 */
public class MapProvider {
    //TODO: overload methods with a municipalityID parameter instead of a municipality parameter
    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality,
     * and contains all its geo-localizable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map getMap(Municipality municipality) throws IOException {
        return new MapBuilder()
                .buildOsmData(municipality.getCoordinatesBox())
                .buildGeoLocalizableList(municipality.getGeoLocalizables())
                .getResult();
    }

    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the given {@link CoordinatesBox} and contains all
     * the geo-localizable points included in the given box.
     *
     * @param municipality the municipality
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map getMap(Municipality municipality, CoordinatesBox box) throws IOException {
        SearchEngine<PointOfInterest> engine = new SearchEngine<>(municipality.getGeoLocalizables());
        engine.addCriterion(Parameter.POSITION, SearchCriterion.INCLUDED_IN_BOX, box);
        return new MapBuilder()
                .buildOsmData(box)
                .buildGeoLocalizableList(engine.search())
                .getResult();
    }

    /**
     * Returns an empty map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality, but it
     * contains no geo-localizable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map getEmptyMap(Municipality municipality) throws IOException {
        return new MapBuilder()
                .buildOsmData(municipality.getCoordinatesBox())
                .getResult();
    }

    /**
     * Returns an empty map delimited by the given box.
     * The returned map contains the associated OSM data but no geo-localizable points.
     *
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map getEmptyMap(CoordinatesBox box) throws IOException {
        return new MapBuilder()
                .buildOsmData(box)
                .getResult();
    }
}
