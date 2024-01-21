package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;

import java.util.List;

/**
 * This class represents a geographical visualizable map, composed of geographical OSM data and
 * {@link PointOfInterest} objects.
 */
public class Map {
    private final String osmData;
    private final List<PointOfInterest> pointOfInterestList;

    public Map(String osmData, List<PointOfInterest> pointOfInterestList) {
        this.pointOfInterestList = pointOfInterestList;
        this.osmData = osmData;
    }

    public String getOsmData() {
        return osmData;
    }

    public List<PointOfInterest> getGeoLocalizableList() {
        return pointOfInterestList.stream().toList();
    }
}
