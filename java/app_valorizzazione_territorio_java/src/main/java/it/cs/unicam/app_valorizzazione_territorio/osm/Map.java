package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;

import java.util.List;

/**
 * This class represents a geographical visualizable map, composed of geographical OSM data and
 * {@link Positionable} objects that are also {@link Identifiable}.
 */
public class Map<P extends Positionable & Identifiable> {
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

    /**
     * Returns the positionable point in this map with the given ID, if present.
     *
     * @param id the ID of the geo-locatable point
     * @return the geo-locatable point with the given ID, if any, null otherwise
     */
    public P getPointByID(long id) {
        return positionablePoints.stream()
                .filter(p -> p.getID() == id)
                .findFirst()
                .orElse(null);
    }
}
