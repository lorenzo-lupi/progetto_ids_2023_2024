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
    private final CoordinatesBox coordinatesBox;

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.files = new ArrayList<>();
        this.geoLocalizables = new ArrayList<>();
        this.coordinatesBox = coordinatesBox;
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
        return coordinatesBox;
    }

    /**
     * Returns the dynamic coordinates box of the municipality,
     * calculated from the coordinates of the geo-localizable points in the municipality in order to include
     * all the points in the box.
     * If the municipality has no geo-localizable points, the static coordinates box is returned.
     *
     * @return the dynamic coordinates box of the municipality
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public CoordinatesBox getDynamicCoordinatesBox() {
        if (geoLocalizables.isEmpty()) return getCoordinatesBox();
        double minLatitude = geoLocalizables.stream().map(GeoLocalizable::getCoordinates).map(Position::getLatitude).min(Double::compare).get();
        double maxLatitude = geoLocalizables.stream().map(GeoLocalizable::getCoordinates).map(Position::getLatitude).max(Double::compare).get();
        double minLongitude = geoLocalizables.stream().map(GeoLocalizable::getCoordinates).map(Position::getLongitude).min(Double::compare).get();
        double maxLongitude = geoLocalizables.stream().map(GeoLocalizable::getCoordinates).map(Position::getLongitude).max(Double::compare).get();
        return new CoordinatesBox(new Position(minLatitude, minLongitude), new Position(maxLatitude, maxLongitude));
    }

}
