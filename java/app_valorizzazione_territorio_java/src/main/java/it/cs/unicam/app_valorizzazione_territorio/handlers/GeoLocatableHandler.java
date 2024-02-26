package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUltils;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MapOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.InsertionUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderBase;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents a handler for the search, insertion
 * and visualization of the geolocatable entities
 */
@Service
@ComponentScan(basePackageClasses = {MapProviderBase.class})
public class GeoLocatableHandler {
    private final MapProvider mapProvider;
    private final UserJpaRepository userRepository;
    private final MunicipalityJpaRepository municipalityRepository;
    private final GeoLocatableJpaRepository geoLocatableJpaRepository;
    private final InsertionUtils insertionUtils;

    @Autowired
    public GeoLocatableHandler(UserJpaRepository userRepository,
                               MunicipalityJpaRepository municipalityRepository,
                               GeoLocatableJpaRepository geoLocatableJpaRepository,
                               InsertionUtils insertionUtils,
                               MapProvider mapProvider) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.geoLocatableJpaRepository = geoLocatableJpaRepository;
        this.insertionUtils = insertionUtils;
        this.mapProvider = mapProvider;
    }

    /**
     * Returns the Detailed Format of a map which contains all geoLocatable points
     *
     * @param municipalityID the ID of the municipality
     * @return the Detailed Format of the map
     */
    public MapOF visualizeInitialMap(long municipalityID) throws IOException {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return mapProvider
                .getMap(municipality.get())
                .getOutputFormat();
    }

    /**
     * Returns the Detailed Format of a map which contains all geoLocatable points
     *
     * @param municipalityID the ID of the municipality
     * @param coordinatesBox the coordinates box
     * @return the Detailed Format of the map
     */
    public MapOF visualizeMap(long municipalityID, CoordinatesBox coordinatesBox) throws IOException {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return mapProvider
                .getMap(municipality.get(), coordinatesBox)
                .getOutputFormat();
    }

    public MapOF visualizeFilteredMap(long municipalityID, CoordinatesBox coordinatesBox, List<SearchFilter> filters) throws IOException {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return mapProvider
                .getFilteredMap(municipality.get(), filters)
                .getOutputFormat();
    }


    /**
     * Returns the Detailed Format of an empty map
     *
     * @param municipalityID the ID of the municipality
     * @return the Detailed Format of the empty map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public MapOF getEmptyMap(long municipalityID) throws IOException {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return mapProvider
                .getEmptyMap(municipality.get())
                .getOutputFormat();

    }

    /**
     * Returns the Detailed Format of an empty map
     *
     * @param coordinatesBox the coordinates box
     * @return the Detailed Format of the empty map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public MapOF getEmptyMap(CoordinatesBox coordinatesBox) throws IOException {
        return mapProvider
                .getEmptyMap(coordinatesBox)
                .getOutputFormat();
    }

    /**
     * Returns the Synthesized Format of all the geoLocatables that correspond to the given criteria
     *
     * @param municipalityID the ID of the municipality
     * @param filters        the filters to apply
     * @return the Synthesized Format of all the geoLocatables that correspond to the given criteria
     */
    @SuppressWarnings("unchecked")
    public List<GeoLocatableOF> searchFilteredGeoLocatables(long municipalityID, List<SearchFilter> filters) {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return (List<GeoLocatableOF>) SearchUltils.getFilteredItems(
                municipality.get().getGeoLocatables(),
                filters
        );
    }

    /**
     * Returns the set of all the criteria available for the search.
     *
     * @return the set of all the criteria available for the search
     */
    public Set<String> getSearchCriteria() {
        return SearchUltils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     *
     * @return the search parameters for the user entity
     */
    public List<String> getParameters() {
        return List.of(Parameter.NAME.toString(),
                Parameter.DESCRIPTION.toString(),
                Parameter.MUNICIPALITY.toString(),
                Parameter.POSITION.toString(),
                Parameter.APPROVAL_STATUS.toString(),
                Parameter.USERNAME.toString(),
                Parameter.THIS.toString());
    }

    /**
     * Returns the Detailed Format of a geoLocatable
     *
     * @param geoLocatableID the ID of the geoLocatable
     * @return the Detailed Format of the geoLocatable
     */
    public Identifiable visualizeDetailedGeoLocatable(long geoLocatableID) {
        Optional<GeoLocatable> geoLocatable = geoLocatableJpaRepository.findByID(geoLocatableID);
        if (geoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");
        return geoLocatable.get().getOutputFormat();
    }

    /**
     * Returns true if the given position is in the municipality corresponding to the given ID
     *
     * @param municipalityID the ID of the municipality
     * @param position       the position
     * @return true if the given position is in the municipality
     */
    public boolean isPositionInMunicipality(long municipalityID, Position position) {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return municipality.get().getCoordinatesBox().contains(position);
    }

    /**
     * Inserts the given point of interest in the municipality corresponding to the given ID.
     *
     * @param userID            the ID of the user who is inserting the point of interest
     * @param pointOfInterestIF the DTO of the point of interest to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the point
     *                                  of interest is not valid
     */
    public long insertPointOfInterest(long userID, PointOfInterestIF pointOfInterestIF) {
        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty()) throw new IllegalArgumentException("User not found");

        Optional<Municipality> municipality = municipalityRepository.getByID(pointOfInterestIF.municipalityID());
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");

        PointOfInterestBuilder builder =
                new PointOfInterestBuilder(municipality.get(),
                        user.get());

        fillPointOfInterestBuilderFields(builder, pointOfInterestIF);

        GeoLocatable geoLocatable = geoLocatableJpaRepository.save(builder.build().obtainResult());

        insertGeoLocatable(geoLocatable, user.get());

        return geoLocatable.getID();
    }

    /**
     * Returns the Synthesized Format of all the point of interests that correspond to the given criteria
     *
     * @param municipalityID the ID of the municipality
     * @param filters        the filters to apply
     * @return the Synthesized Format of all the point of interests that correspond to the given criteria
     */
    public List<GeoLocatableOF> getFilteredPointOfInterests(long municipalityID,
                                                            List<SearchFilter> filters) {
        List<SearchFilter> userFilters = new LinkedList<>(filters);
        userFilters.add(new SearchFilter("THIS", "CLASS_IS_POI", ""));
        return searchFilteredGeoLocatables(municipalityID, userFilters);
    }

    //TODO
    public static List<String> obtainPointOfInterestSearchParameters() {
        return null;
    }


    /**
     * Inserts the given compound point in the municipality corresponding to the given ID.
     *
     * @param municipalityID  the ID of the municipality
     * @param userID          the ID of the user who is inserting the compound point
     * @param compoundPointIF the DTO of the compound point to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the compound
     *                                  point is not valid
     */
    public long insertCompoundPoint(long municipalityID,
                                    long userID,
                                    CompoundPointIF compoundPointIF) {

        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty()) throw new IllegalArgumentException("User not found");
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");

        CompoundPoint compoundPoint = geoLocatableJpaRepository.save(buildCompoundPoint(
                municipality.get(),
                user.get(),
                compoundPointIF
        ));
        insertGeoLocatable(compoundPoint, user.get());

        return compoundPoint.getID();
    }

    private CompoundPoint buildCompoundPoint(Municipality municipality,
                                             User user,
                                             CompoundPointIF compoundPointIF) {
        CompoundPointBuilder builder = new CompoundPointBuilder(
                CompoundPointTypeEnum.fromString(compoundPointIF.compoundPointType()),
                municipality,
                user);

        builder.setTitle(compoundPointIF.title())
                .setDescription(compoundPointIF.description())
                .addImage(compoundPointIF.images());
        compoundPointIF.pointsOfInterestIDs().stream()
                .map(geoLocatableJpaRepository::findPointOfInterestById)
                .forEach(p -> p.ifPresentOrElse(builder::addPointOfInterest, () -> {
                    throw new IllegalArgumentException("Point of interest not found");
                }));
        return builder.obtainResult();
    }


    private void fillPointOfInterestBuilderFields(PointOfInterestBuilder builder, PointOfInterestIF pointOfInterestIF) {
        builder.setTitle(pointOfInterestIF.name())
                .setDescription(pointOfInterestIF.description());
        pointOfInterestIF.images().forEach(builder::addImage);

        try {
            Optional<Municipality> municipality = mapProvider.getMunicipalityByPosition(pointOfInterestIF.position());
            if (municipality.isEmpty())
                throw new IllegalArgumentException("The position is not in the municipality");
            if (!builder.getMunicipality().equals(municipality.get()))
                throw new IllegalArgumentException("The position is not in the municipality");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while checking the position");
        }

        builder.setPosition(pointOfInterestIF.position());

        setPointOfInterestClassification(builder, pointOfInterestIF);
    }

    private void setPointOfInterestClassification(PointOfInterestBuilder builder,
                                                  PointOfInterestIF pointOfInterestIF) {
        builder.setClassification(PointOfInterest.stringToClass.get(pointOfInterestIF.classification()));
        if (pointOfInterestIF.classification().equalsIgnoreCase(Attraction.class.getSimpleName()))
            builder.setAttractionType(AttractionTypeEnum.fromString(pointOfInterestIF.type().toUpperCase()));
        else if (pointOfInterestIF.classification().equalsIgnoreCase(Event.class.getSimpleName()))
            builder.setStartDate(pointOfInterestIF.startDate())
                    .setEndDate(pointOfInterestIF.endDate());
        else if (pointOfInterestIF.classification().equalsIgnoreCase(Activity.class.getSimpleName()))
            builder.setActivityType(ActivityTypeEnum.fromString(pointOfInterestIF.type().toUpperCase()))
                    .setTimetable(pointOfInterestIF.timetable());
        else throw new IllegalArgumentException("Invalid classification");
    }

    private void insertGeoLocatable(GeoLocatable geoLocatable, User user) {
        Municipality municipality = geoLocatable.getMunicipality();
        insertionUtils.insertItemApprovableByContributors(geoLocatable, user, municipality, municipality::addGeoLocatable);
    }

}

