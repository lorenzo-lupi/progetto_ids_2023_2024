package it.cs.unicam.app_valorizzazione_territorio.osm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a map.
 */
public class MapBuilder<P extends Positionable & Visualizable> {

    private final OSMRequestHandler osmRequestHandler;
    private final XmlMapper xmlMapper;
    private String osmData;
    private List<P> pointsList;
    private Map<P> map;

    public MapBuilder(OSMRequestHandler osmRequestHandler) {
        this.osmRequestHandler = osmRequestHandler;
        this.xmlMapper = new XmlMapper();
        this.osmData = "";
        this.pointsList = new ArrayList<>();
    }

    /**
     * Builds the osmData of the map with the given data.
     *
     * @param osmData the osmData
     * @return the builder
     */
    public MapBuilder<P> buildOsmData(String osmData) {
        this.osmData = osmData;
        return this;
    }

    /**
     * Builds the osmData of the map with the geographical data included in the given coordinates box.
     * @param coordinatesBox the coordinates box.
     * @return the builder
     * @throws IOException if an I/O error occurs
     */
    public MapBuilder<P> buildOsmData(CoordinatesBox coordinatesBox) throws IOException {
        this.osmData = osmRequestHandler.retrieveOSMData(coordinatesBox);
        return this;
    }

    /**
     * Builds the geo-locatable list of the map with the given data.
     *
     * @param  pointsList the list of positionable points
     * @return the builder
     */
    public MapBuilder<P> buildPointsList(List<P> pointsList) {
        this.pointsList = pointsList;
        return this;
    }

    /**
     * Adds a geo-locatable point to the map to be built.
     *
     * @param point the geo-locatable point to added
     * @return true if the geo-locatable point has been added, false otherwise
     */
    public boolean addGeoLocatable(P point) {
        return this.pointsList.add(point);
    }

    /**
     * Removes a geo-locatable point from the map to be built.
     *
     * @param point the geo-locatable point to removed
     * @return true if the geo-locatable point has been removed, false otherwise
     */
    public boolean removeGeoLocatable(P point) {
        return this.pointsList.remove(point);
    }

    public MapBuilder<P> build() throws JsonProcessingException {
        this.map =  new Map<P>(
                xmlMapper.readValue(this.osmData, Object.class),
                this.pointsList.stream().toList());
        return this;
    }

    /**
     * Builds a map with the data provided.
     *
     * @return the map built
     */
    public Map<P> getResult() {
        return this.map;
    }

    /**
     * Resets the builder.
     *
     * @return the builder
     */
    public MapBuilder<P> reset() {
        this.osmData = "";
        this.pointsList = new ArrayList<>();
        return this;
    }
}
