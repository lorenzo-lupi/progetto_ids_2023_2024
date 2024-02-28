package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MapOF;

import java.util.List;

/**
 * This class represents a geographical visualizable map, composed of geographical OSM data and
 * {@link Positionable} objects that are also {@link Identifiable}.
 */
public class Map<P extends Positionable & Visualizable> implements Visualizable {
    private final Object osmData;
    private final List<P> positionablePoints;

    public Map(Object osmData, List<P> positionablePoints) {
        this.osmData = osmData;
        this.positionablePoints = positionablePoints;
    }

    public Object getOsmData() {
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

    @Override
    public long getID() {
        return 0L;
    }

    @Override
    public MapOF getOutputFormat() {
        return new MapOF(osmData,
                positionablePoints.stream()
                        .map(Visualizable::getOutputFormat)
                        .toList()
        );
    }
}
