package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocalizable;

import java.util.List;

/**
 * This class represents a geographical visualizable map, composed of geographical OSM data and
 * {@link GeoLocalizable} objects.
 */
public class Map {
    private final String osmData;
    private final List<GeoLocalizable> geoLocalizableList;

    public Map(String osmData, List<GeoLocalizable> geoLocalizableList) {
        this.geoLocalizableList = geoLocalizableList;
        this.osmData = osmData;
    }

    public String getOsmData() {
        return osmData;
    }

    public List<GeoLocalizable> getGeoLocalizableList() {
        return geoLocalizableList.stream().toList();
    }
}
