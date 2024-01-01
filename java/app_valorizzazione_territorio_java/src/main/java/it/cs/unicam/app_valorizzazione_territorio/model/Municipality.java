package it.cs.unicam.app_valorizzazione_territorio.model;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Representative entity of a municipal territory registered in the system.
 * It acts as a container for geo-localizable points.
 */
public class Municipality {
    private final String name;
    private final String description;
    private final Position position;
    private final List<Files> files;
    private final List<GeoLocalizable> geoLocalizables;

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     */
    public Municipality (String name, String description, Position position) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.files = new ArrayList<>();
        this.geoLocalizables = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Position getPosition() {
        return position;
    }

    public List<Files> getFiles() {
        return files;
    }

    public List<GeoLocalizable> getGeoLocalizables() {
        return geoLocalizables;
    }

    public CoordinatesBox getCoordinatesBox() {
        //TODO
        return null;
    }

}
