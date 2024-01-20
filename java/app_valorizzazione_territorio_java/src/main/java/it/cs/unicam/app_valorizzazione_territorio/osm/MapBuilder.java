package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocalizable;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class MapBuilder {
    private String osmData;
    private List<GeoLocalizable> geoLocalizableList;

    public MapBuilder buildOsmData(String osmData) {
        this.osmData = osmData;
        return this;
    }

    public MapBuilder buildGeoLocalizableList(List<GeoLocalizable> geoLocalizableList) {
        this.geoLocalizableList = geoLocalizableList;
        return this;
    }

    public MapBuilder buildGeoLocalizableList(SearchResult<GeoLocalizable> geoLocalizableResult) {
        this.geoLocalizableList = geoLocalizableResult.getResults();
        return this;
    }

    /**
     * Builds the osmData of the map from the given coordinates box.
     * @param coordinatesBox the coordinates box.
     * @return the builder
     * @throws IOException if an I/O error occurs
     */
    public MapBuilder buildPerimeter(CoordinatesBox coordinatesBox) throws IOException {
        this.osmData = OSMRequestHandler.getInstance().retrieveOSMData(coordinatesBox);
        return this;
    }

    public Map getResult() {
        return new Map(this.osmData, this.geoLocalizableList.stream().toList());
    }
}
