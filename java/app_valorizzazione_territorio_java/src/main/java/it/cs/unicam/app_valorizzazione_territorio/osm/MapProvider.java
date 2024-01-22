package it.cs.unicam.app_valorizzazione_territorio.osm;


import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;

import java.io.IOException;

/**
 * This class provides methods to retrieve maps composed by data from the OSM API and geo-localzable from
 * the given municipalities.
 */
public class MapProvider {
    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality,
     * and contains all its geo-locatable points.
     *
     * @param municipalityID the municipality ID
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<GeoLocatable> getMap(long municipalityID) throws IOException {
        return getMap(MunicipalityRepository.getInstance().getItemByID(municipalityID));
    }
    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality,
     * and contains all its geo-locatable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<GeoLocatable> getMap(Municipality municipality) throws IOException {
        return new MapBuilder<GeoLocatable>()
                .buildOsmData(municipality.getCoordinatesBox())
                .buildPointsList(municipality.getGeoLocatables())
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
    public static Map<GeoLocatable> getMap(Municipality municipality, CoordinatesBox box) throws IOException {
        SearchEngine<GeoLocatable> engine = new SearchEngine<>(municipality.getGeoLocatables());
        engine.addCriterion(Parameter.POSITION, SearchCriterion.INCLUDED_IN_BOX, box);
        return new MapBuilder<GeoLocatable>()
                .buildOsmData(box)
                .buildPointsList(engine.search().getResults())
                .getResult();
    }

    /**
     * Returns an empty map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality, but it
     * contains no geo-locatable points.
     *
     * @param municipalityID the municipality ID
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<?> getEmptyMap(long municipalityID) throws IOException {
        return getEmptyMap(MunicipalityRepository.getInstance().getItemByID(municipalityID));
    }

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
        return new MapBuilder<>()
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
    public static Map<?> getEmptyMap(CoordinatesBox box) throws IOException {
        return new MapBuilder<>()
                .buildOsmData(box)
                .getResult();
    }

    /**
     * Returns a map of the Italian territory containing all the municipalities
     * registered in the system.
     *
     * @return a map of the Italian territory
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static Map<Municipality> getMunicipalityMap() throws IOException {
        return new MapBuilder<Municipality>()
                .buildOsmData(CoordinatesBox.ITALY)
                .buildPointsList(MunicipalityRepository.getInstance().getItemStream().toList())
                .getResult();
    }
}
