package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MapDOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Map;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;

import java.io.IOException;


/**
 * A search handler that searches for GeoLocatable objects using a map.
 */
public class VisualizeGeoLocatableThroughMapHandler extends SearchHandler<GeoLocatable> {

    private Map<GeoLocatable> map;
    private Municipality municipality;

    /**
     * Creates a new search handler that searches in the given collection of searchable items.
     * @param municipality the municipality to search in
     */
    public VisualizeGeoLocatableThroughMapHandler(Municipality municipality) throws IOException {
        super(municipality.getGeoLocatables());
        this.map = MapProvider.getMap(municipality);
    }

    /**
     * builds a new map with the given coordinate boxes
     * @param coordinatesBox the coordinates of the map
     * @throws IOException if the map cannot be created
     */
    public void handleMap(CoordinatesBox coordinatesBox) throws IOException {
        this.map = MapProvider.getMap(this.municipality, coordinatesBox);
    }

    /**
     *  Visualizes the map
     */
    public MapDOF visualizeMap() {
        return map.getDetailedFormat();
    }

    /**
     * Visualizes the given geo-localizable object in the detailed format.
     * @param id the id of the geo-localizable object
     * @return the geo-localizable object in the detailed format
     */
    public Identifiable visualizeGeoLocatableInDetailedFormat(long id) {

        return MunicipalityRepository
                .getInstance()
                .getGeoLocatableByID(id)
                .getDetailedFormat();
    }

}
