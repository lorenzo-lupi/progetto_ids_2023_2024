package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This class provides methods to retrieve maps composed by data from the OSM API and geo-localzable from
 * the given municipalities.
 */
@Component
public abstract class MapProvider {

    /**
     * Returns an empty map associated with the given municipality.
     * The returned map has the size of the default {@link CoordinatesBox} of the municipality, but it
     * contains no geo-locatable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<?> getEmptyMap(Municipality municipality) throws IOException;

    /**
     * Returns an empty map delimited by the given box.
     * The returned map contains the associated OSM data but no geo-localizable points.
     *
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<?> getEmptyMap(CoordinatesBox box) throws IOException;

    /**
     * Returns a map associated with the given municipality.
     * The returned map has the size of the default CoordinateBox of the municipality,
     * and contains all its geo-locatable points.
     *
     * @param municipality the municipality
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<GeoLocatable> getMap(Municipality municipality) throws IOException;

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
    public abstract Map<GeoLocatable> getMap(Municipality municipality, CoordinatesBox box) throws IOException;



    /**
     * Returns a map of the Italian territory containing all the municipalities
     * registered in the system.
     *
     * @return a map of the Italian territory
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<Municipality> getMunicipalitiesMap() throws IOException;


    /**
     * Returns a map of a municipality containing all the filtered geo-locatable points.
     * @param municipality the municipality
     * @param filters the filters to apply
     * @return a map of the municipality containing all the filtered geo-locatable points
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<GeoLocatable> getFilteredMap(Municipality municipality, List<SearchFilter> filters) throws IOException;

    /**
     * Returns a map of a municipality containing all the filtered geo-locatable points.
     * The returned map has the size of the given CoordinateBox and contains the filtered
     * geo-localizable points included in the given box.
     *
     * @param municipality the municipality
     * @param box the coordinates box
     * @param filters the filters to apply
     * @return a map of the municipality containing all the filtered geo-locatable points
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Map<GeoLocatable> getFilteredMap(Municipality municipality,
                                     CoordinatesBox box,
                                     List<SearchFilter> filters) throws IOException;

    /**
     * Returns the municipality where the given position resides.
     * The returned municipality is the municipality registered in the system that contains the given position.
     * If no municipality contains the given position, an empty optional object is returned.
     *
     * @param position the position
     * @return the municipality where the given position resides,
     * or an empty optional object if no municipality contains the given position
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public abstract Optional<Municipality> getMunicipalityByPosition(Position position) throws IOException;
}
