package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representative entity of a municipal territory registered in the system.
 * It acts as a container for geo-localizable points.
 */
public class Municipality implements Searchable {
    private String name;
    private String description;
    private final Position position;
    private final CoordinatesBox coordinatesBox;
    private final List<File> files;
    private final List<PointOfInterest> pointOfInterests;

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     * @throws IllegalArgumentException if position or coordinatesBox are null
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox) {
        this(name, description, position, coordinatesBox, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     * @param files representative multimedia content of the municipality
     * @param pointOfInterests geo-localizable points of the municipality
     * @throws IllegalArgumentException if position, coordinatesBox, files or geoLocalizables are null
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox, List<File> files, List<PointOfInterest> pointOfInterests) {
        if (position == null || coordinatesBox == null || files == null || pointOfInterests == null)
            throw new IllegalArgumentException("The parameters cannot be null.");
        this.name = name;
        this.description = description;
        this.position = position;
        this.coordinatesBox = coordinatesBox;
        this.files = files;
        this.pointOfInterests = pointOfInterests;
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

    public CoordinatesBox getCoordinatesBox() {
        return coordinatesBox;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<PointOfInterest> getGeoLocalizables() {
        return pointOfInterests;
    }

    public boolean addFile(File file) {
        return this.files.add(file);
    }

    public boolean removeFile(File file) {
        return this.files.remove(file);
    }

    public boolean addGeoLocalizable(PointOfInterest pointOfInterest) {
        return this.pointOfInterests.add(pointOfInterest);
    }

    public boolean removeGeoLocalizable(PointOfInterest pointOfInterest) {
        return this.pointOfInterests.remove(pointOfInterest);
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
        if (pointOfInterests.isEmpty()) return getCoordinatesBox();
        double minLatitude = pointOfInterests.stream().map(PointOfInterest::getCoordinates).map(Position::getLatitude).min(Double::compare).get();
        double maxLatitude = pointOfInterests.stream().map(PointOfInterest::getCoordinates).map(Position::getLatitude).max(Double::compare).get();
        double minLongitude = pointOfInterests.stream().map(PointOfInterest::getCoordinates).map(Position::getLongitude).min(Double::compare).get();
        double maxLongitude = pointOfInterests.stream().map(PointOfInterest::getCoordinates).map(Position::getLongitude).max(Double::compare).get();
        return new CoordinatesBox(new Position(minLatitude, minLongitude), new Position(maxLatitude, maxLongitude));
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.POSITION, this.position,
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name);
    }
}
