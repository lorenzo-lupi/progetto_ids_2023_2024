package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;

import java.util.List;

/**
 * This class represents a geographical visualizable map, composed of geographical OSM data and
 * {@link Positionable} objects.
 */
public class Map<P extends Positionable> {
    private final String osmData;
    private final List<P> positionablePoints;

    public Map(String osmData, List<P> positionablePoints) {
        this.positionablePoints = positionablePoints;
        this.osmData = osmData;
    }

    public String getOsmData() {
        return osmData;
    }

    public List<P> getPointsList() {
        return positionablePoints.stream().toList();
    }
}
