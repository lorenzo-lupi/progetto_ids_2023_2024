package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.CompoundPointOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.PointOfInterestOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MapOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.InsertionUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.*;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents a handler for the search, insertion
 * and visualization of the geo-locatable entities
 */
@Service
@ComponentScan(basePackageClasses = {MapProviderBase.class})
public class GeoLocatableHandler {
    @Value("${fileResources.path}")
    private String filePath;
    private final MapProvider mapProvider;
    private final UserJpaRepository userRepository;
    private final MunicipalityJpaRepository municipalityRepository;
    private final GeoLocatableJpaRepository geoLocatableJpaRepository;
    @Autowired
    private final OSMRequestHandler osmRequestHandler;
    private final InsertionUtils insertionUtils;

    @Autowired
    public GeoLocatableHandler(UserJpaRepository userRepository,
                               MunicipalityJpaRepository municipalityRepository,
                               GeoLocatableJpaRepository geoLocatableJpaRepository,
                               InsertionUtils insertionUtils,
                               MapProvider mapProvider,
                               OSMRequestHandler osmRequestHandler) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.geoLocatableJpaRepository = geoLocatableJpaRepository;
        this.insertionUtils = insertionUtils;
        this.mapProvider = mapProvider;
        this.osmRequestHandler = osmRequestHandler;
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
                .getFilteredMap(municipality.get(), coordinatesBox, filters)
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
        return (List<GeoLocatableOF>) SearchUtils.getFilteredItems(
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
        return SearchUtils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     *
     * @return the search parameters for the user entity
     */
    public List<String> getSearchParameters() {
        return List.of(Parameter.NAME.toString(),
                Parameter.DESCRIPTION.toString(),
                Parameter.MUNICIPALITY.toString(),
                Parameter.POSITION.toString(),
                Parameter.APPROVAL_STATUS.toString(),
                Parameter.USERNAME.toString(),
                Parameter.THIS.toString());
    }

    /**
     * Returns the geo-locatable with the given ID, if any.
     *
     * @param geoLocatableID the ID of the geoLocatable
     * @return the geo-locatable
     */
    public GeoLocatableOF visualizeDetailedGeoLocatable(long geoLocatableID) {
        Optional<GeoLocatable> geoLocatable = geoLocatableJpaRepository.findById(geoLocatableID);
        if (geoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");

        return geoLocatable.get().getOutputFormat();
    }

    /**
     * Returns the point of interest with the given ID, if any.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the point of interest
     */
    public PointOfInterestOF visualizeDetailedPointOfInterest(long pointOfInterestID) {
        Optional<PointOfInterest> pointOfInterest = geoLocatableJpaRepository.findPointOfInterestById(pointOfInterestID);
        if (pointOfInterest.isEmpty())
            throw new IllegalArgumentException("Point of interest not found");

        return pointOfInterest.get().getOutputFormat();
    }

    /**
     * Returns the compound point with the given ID, if any.
     *
     * @param compoundPointID the ID of the compound point
     * @return the compound point
     */
    public CompoundPointOF visualizeDetailedCompoundPoint(long compoundPointID) {
        Optional<CompoundPoint> compoundPoint = geoLocatableJpaRepository.findCompoundPointById(compoundPointID);
        if (compoundPoint.isEmpty())
            throw new IllegalArgumentException("Compound point not found");

        return compoundPoint.get().getOutputFormat();
    }

    /**
     * Returns true if the given position is in the municipality corresponding to the given ID
     *
     * @param municipalityID the ID of the municipality
     * @param position       the position
     * @return true if the given position is in the municipality
     */
    public boolean isPositionInMunicipality(long municipalityID, Position position) throws IOException {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return osmRequestHandler.getMunicipalityOfPosition(position).contains(municipality.get().getName());//municipality.get().getCoordinatesBox().contains(position);
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
        geoLocatableJpaRepository.save(geoLocatable);

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
                .addImage(compoundPointIF.files().stream()
                        .map(fileName -> new File(filePath + fileName)).toList());
        compoundPointIF.pointsOfInterestIDs().stream()
                .map(geoLocatableJpaRepository::findPointOfInterestById)
                .forEach(p -> p.ifPresentOrElse(builder::addPointOfInterest, () -> {
                    throw new IllegalArgumentException("Point of interest not found");
                }));
        return builder.obtainResult();
    }


    private void fillPointOfInterestBuilderFields(PointOfInterestBuilder builder, PointOfInterestIF pointOfInterestIF) {
        builder.setTitle(pointOfInterestIF.name())
                .setDescription(pointOfInterestIF.description())
                .addImage(pointOfInterestIF.images().stream()
                        .map(fileName -> new File(filePath + fileName))
                        .toList());

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

