package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalitySOF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representative entity of a municipal territory registered in the system.
 * It acts as a container for geo-locatable points.
 */
public class Municipality implements Searchable, Identifiable, Visualizable, Positionable {
    private final String name;
    private final String description;
    private final Position position;
    private final CoordinatesBox coordinatesBox;
    private final List<File> files;
    private final List<GeoLocatable> geoLocatables;
    private final List<Contest> contests;
    private final List<Notification> notifications;
    private final long ID = MunicipalityRepository.getInstance().getNextID();

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     * @throws IllegalArgumentException if position or coordinatesBox are null
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox, List<File> files) {
        this(name, description, position, coordinatesBox, files, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a municipality.
     *
     * @param name name of the municipality
     * @param description description of the municipality
     * @param position geographical coordinates of the municipality
     * @param files representative multimedia content of the municipality
     * @param geoLocatables geo-localizable points of the municipality
     * @throws IllegalArgumentException if position, coordinatesBox, files or geoLocatables are null
     */
    public Municipality (String name, String description, Position position, CoordinatesBox coordinatesBox, List<File> files, List<GeoLocatable> geoLocatables, List<Contest> contests) {
        if (position == null || coordinatesBox == null || files == null || geoLocatables == null || contests == null)
            throw new IllegalArgumentException("The parameters cannot be null.");
        this.name = name;
        this.description = description;
        this.position = position;
        this.coordinatesBox = coordinatesBox;
        this.files = files;
        this.geoLocatables = geoLocatables;
        this.contests = contests;
        this.notifications = new ArrayList<>();
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

    public List<GeoLocatable> getGeoLocatables() {
        return geoLocatables;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public boolean addFile(File file) {
        return this.files.add(file);
    }

    public boolean removeFile(File file) {
        return this.files.remove(file);
    }

    public boolean addGeoLocatable(GeoLocatable geoLocatable) {
        return this.geoLocatables.add(geoLocatable);
    }

    public boolean removeGeoLocatable(GeoLocatable geoLocatable) {
        return this.geoLocatables.remove(geoLocatable);
    }

    public boolean addContest(Contest contest) {
        return this.contests.add(contest);
    }

    public boolean removeContest(Contest contest) {
        return this.contests.remove(contest);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public boolean addNotification(Notification notification) {
        return this.notifications.add(notification);
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
        if (geoLocatables.isEmpty()) return getCoordinatesBox();
        double minLatitude = geoLocatables.stream().map(GeoLocatable::getPosition).map(Position::getLatitude).min(Double::compare).get();
        double maxLatitude = geoLocatables.stream().map(GeoLocatable::getPosition).map(Position::getLatitude).max(Double::compare).get();
        double minLongitude = geoLocatables.stream().map(GeoLocatable::getPosition).map(Position::getLongitude).min(Double::compare).get();
        double maxLongitude = geoLocatables.stream().map(GeoLocatable::getPosition).map(Position::getLongitude).max(Double::compare).get();
        return new CoordinatesBox(new Position(minLatitude, minLongitude), new Position(maxLatitude, maxLongitude));
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.POSITION, this.getPosition(),
                Parameter.DESCRIPTION, this.getDescription(),
                Parameter.NAME, this.getName());
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public MunicipalitySOF getSynthesizedFormat() {
        return new MunicipalitySOF(this.getName(),
                this.getFiles().isEmpty() ? null : this.getFiles().get(0),
                this.getID());
    }

    @Override
    public MunicipalityDOF getDetailedFormat() {
        return new MunicipalityDOF(this.getName(), this.getDescription(), this.getPosition(), this.getFiles(), this.getID());
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }
}
