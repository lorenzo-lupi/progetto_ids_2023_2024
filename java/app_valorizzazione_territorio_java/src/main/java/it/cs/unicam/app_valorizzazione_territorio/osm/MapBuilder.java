package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocalizable;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchResult;

import java.io.IOException;
import java.util.List;

/**
 * Builder for a map.
 */
public class MapBuilder {
    private String osmData;
    private List<GeoLocalizable> geoLocalizableList;

    public MapBuilder() {
        this.osmData = "";
        this.geoLocalizableList = List.of();
    }

    /**
     * Builds the osmData of the map with the given data.
     *
     * @param osmData the osmData
     * @return the builder
     */
    public MapBuilder buildOsmData(String osmData) {
        this.osmData = osmData;
        return this;
    }

    /**
     * Builds the osmData of the map with the geographical data included in the given coordinates box.
     * @param coordinatesBox the coordinates box.
     * @return the builder
     * @throws IOException if an I/O error occurs
     */
    public MapBuilder buildOsmData(CoordinatesBox coordinatesBox) throws IOException {
        this.osmData = OSMRequestHandler.getInstance().retrieveOSMData(coordinatesBox);
        return this;
    }

    /**
     * Builds the geo-localizable list of the map with the given data.
     *
     * @param  geoLocalizableList the geo-localizable list
     * @return the builder
     */
    public MapBuilder buildGeoLocalizableList(List<GeoLocalizable> geoLocalizableList) {
        this.geoLocalizableList = geoLocalizableList;
        return this;
    }

    /**
     * Builds the geo-localizable list of the map with the data included in the given search result.
     *
     * @param geoLocalizableResult the search result
     * @return the builder
     */
    public MapBuilder buildGeoLocalizableList(SearchResult<GeoLocalizable> geoLocalizableResult) {
        this.geoLocalizableList = geoLocalizableResult.getResults();
        return this;
    }

    /**
     * Adds a geo-localizable point to the map to be built.
     *
     * @param geoLocalizable the geo-localizable point to added
     * @return true if the geo-localizable point has been added, false otherwise
     */
    public boolean addGeoLocalizable(GeoLocalizable geoLocalizable) {
        return this.geoLocalizableList.add(geoLocalizable);
    }

    /**
     * Removes a geo-localizable point from the map to be built.
     *
     * @param geoLocalizable the geo-localizable point to removed
     * @return true if the geo-localizable point has been removed, false otherwise
     */
    public boolean removeGeoLocalizable(GeoLocalizable geoLocalizable) {
        return this.geoLocalizableList.remove(geoLocalizable);
    }

    /**
     * Builds a map with the data provided.
     *
     * @return the map built
     */
    public Map getResult() {
        return new Map(this.osmData, this.geoLocalizableList.stream().toList());
    }

    /**
     * Resets the builder.
     *
     * @return the builder
     */
    public MapBuilder reset() {
        this.osmData = "";
        this.geoLocalizableList = List.of();
        return this;
    }
}
