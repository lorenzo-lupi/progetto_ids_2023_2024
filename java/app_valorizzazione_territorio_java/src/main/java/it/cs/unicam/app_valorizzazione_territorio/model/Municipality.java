package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representative entity of a municipal territory registered in the system.
 * It acts as a container for geo-localizable points.
 */
public class Municipality implements Searchable, Identifiable, Visualizable {
    private final String name;
    private final String description;
    private final Position position;
    private final CoordinatesBox coordinatesBox;
    private final List<File> files;
    private final List<GeoLocalizable> geoLocalizables;
    private final long ID = MunicipalityRepository.getInstance().getNextID();

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
     * @param geoLocalizables geo-localizable points of the municipality
     * @throws IllegalArgumentException if position, coordinatesBox, files or geoLocalizables are null
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox, List<File> files, List<GeoLocalizable> geoLocalizables) {
        if (position == null || coordinatesBox == null || files == null || geoLocalizables == null)
            throw new IllegalArgumentException("The parameters cannot be null.");
        this.name = name;
        this.description = description;
        this.position = position;
        this.coordinatesBox = coordinatesBox;
        this.files = files;
        this.geoLocalizables = geoLocalizables;
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

    public List<GeoLocalizable> getGeoLocalizables() {
        return geoLocalizables;
    }

    public boolean addFile(File file) {
        return this.files.add(file);
    }

    public boolean removeFile(File file) {
        return this.files.remove(file);
    }

    public boolean addGeoLocalizable(GeoLocalizable geoLocalizable) {
        return this.geoLocalizables.add(geoLocalizable);
    }

    public boolean removeGeoLocalizable(GeoLocalizable geoLocalizable) {
        return this.geoLocalizables.remove(geoLocalizable);
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
        double minLatitude = geoLocalizables.stream().map(GeoLocalizable::getPosition).map(Position::getLatitude).min(Double::compare).get();
        double maxLatitude = geoLocalizables.stream().map(GeoLocalizable::getPosition).map(Position::getLatitude).max(Double::compare).get();
        double minLongitude = geoLocalizables.stream().map(GeoLocalizable::getPosition).map(Position::getLongitude).min(Double::compare).get();
        double maxLongitude = geoLocalizables.stream().map(GeoLocalizable::getPosition).map(Position::getLongitude).max(Double::compare).get();
        return new CoordinatesBox(new Position(minLatitude, minLongitude), new Position(maxLatitude, maxLongitude));
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.POSITION, this.position,
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name);
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public Identifiable getSynthesizedFormat() {
        //TODO
        return null;
    }

    @Override
    public Identifiable getDetailedFormat() {
        //TODO
        return null;
    }
}
