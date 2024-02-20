package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MapDOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderBase;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a handler for the search, insertion
 * and visualization of the geolocatable entities
 */
public class GeoLocatableHandler {
    private static final MapProvider mapProvider = new MapProviderBase();
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    /**
     * Returns the Detailed Format of a map which contains all geoLocatable points
     *
     * @param municipalityID the ID of the municipality
     * @return the Detailed Format of the map
     */
    public static MapDOF visualizeInitialMap(long municipalityID) throws IOException {
        return mapProvider
                .getMap(municipalityRepository.getItemByID(municipalityID))
                .getDetailedFormat();
    }

    /**
     * Returns the Detailed Format of a map which contains all geoLocatable points
     *
     * @param municipalityID the ID of the municipality
     * @param coordinatesBox the coordinates box
     * @return the Detailed Format of the map
     */
    public static MapDOF visualizeMap(long municipalityID, CoordinatesBox coordinatesBox) throws IOException {
        return mapProvider
                .getMap(municipalityRepository.getItemByID(municipalityID), coordinatesBox)
                .getDetailedFormat();
    }

    public static MapDOF visualizeFilteredMap(long municipalityID, CoordinatesBox coordinatesBox, List<SearchFilter> filters) throws IOException {
        return mapProvider
                .getFilteredMap(municipalityRepository.getItemByID(municipalityID), filters)
                .getDetailedFormat();
    }


    /**
     * Returns the Detailed Format of an empty map
     *
     * @param municipalityID the ID of the municipality
     * @return the Detailed Format of the empty map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF getEmptyMap(long municipalityID) throws IOException {
        return mapProvider
                .getEmptyMap(municipalityRepository.getItemByID(municipalityID))
                .getDetailedFormat();

    }

    /**
     * Returns the Detailed Format of an empty map
     *
     * @param coordinatesBox the coordinates box
     * @return the Detailed Format of the empty map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF getEmptyMap(CoordinatesBox coordinatesBox) throws IOException {
        return mapProvider
                .getEmptyMap(coordinatesBox)
                .getDetailedFormat();
    }

    /**
     * Returns the Synthesized Format of all the geoLocatables that correspond to the given criteria
     *
     * @param municipalityID the ID of the municipality
     * @param filters        the filters to apply
     * @return the Synthesized Format of all the geoLocatables that correspond to the given criteria
     */
    @SuppressWarnings("unchecked")
    public static List<GeoLocatableSOF> searchGeoLocatables(long municipalityID, List<SearchFilter> filters) {
        return (List<GeoLocatableSOF>) SearchHandler.getFilteredItems(
                municipalityRepository.getItemByID(municipalityID).getGeoLocatables(),
                filters
        );
    }

    /**
     * Returns the Detailed Format of a geoLocatable
     *
     * @param geoLocatableID the ID of the geoLocatable
     * @return the Detailed Format of the geoLocatable
     */
    public static Identifiable visualizeDetailedGeoLocatable(long geoLocatableID) {
        return municipalityRepository.getGeoLocatableByID(geoLocatableID).getDetailedFormat();
    }

    /**
     * Returns true if the given position is in the municipality corresponding to the given ID
     *
     * @param municipalityID the ID of the municipality
     * @param position       the position
     * @return true if the given position is in the municipality
     */
    public static boolean isPositionInMunicipality(long municipalityID, Position position) {
        return municipalityRepository.getItemByID(municipalityID).getCoordinatesBox().contains(position);
    }

    /**
     * Inserts the given point of interest in the municipality corresponding to the given ID.
     *
     * @param userID            the ID of the user who is inserting the point of interest
     * @param pointOfInterestIF the DTO of the point of interest to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the point
     *                                  of interest is not valid
     */
    public static long insertPointOfInterest(long userID, PointOfInterestIF pointOfInterestIF) {
        User user = userRepository.getItemByID(userID);

        PointOfInterestBuilder builder =
                new PointOfInterestBuilder(municipalityRepository.getItemByID(pointOfInterestIF.municipalityID()),
                        user);

        fillPointOfInterestBuilderFields(builder, pointOfInterestIF);

        GeoLocatable geoLocatable = builder.build().obtainResult();

        GeoLocatableControllerUtils.insertGeoLocatable(geoLocatable, user);

        return geoLocatable.getID();
    }

    /**
     * Returns the Synthesized Format of all the point of interests that correspond to the given criteria
     *
     * @param municipalityID the ID of the municipality
     * @param filters        the filters to apply
     * @return the Synthesized Format of all the point of interests that correspond to the given criteria
     */
    public static List<GeoLocatableSOF> getFilteredPointOfInterests(long municipalityID,
                                                                    List<SearchFilter> filters) {
        List<SearchFilter> userFilters = new LinkedList<>(filters);
        userFilters.add(new SearchFilter("THIS", "CLASS_IS_POI", ""));
        return searchGeoLocatables(municipalityID, userFilters);
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
    public static long insertCompoundPoint(long municipalityID,
                                           long userID,
                                           CompoundPointIF compoundPointIF) {
        User user = userRepository.getItemByID(userID);

        CompoundPoint compoundPoint = buildCompoundPoint(
                municipalityRepository.getItemByID(municipalityID),
                user,
                compoundPointIF
        );
        GeoLocatableControllerUtils.insertGeoLocatable(compoundPoint, user);

        return compoundPoint.getID();
    }

    private static CompoundPoint buildCompoundPoint(Municipality municipality,
                                                    User user,
                                                    CompoundPointIF compoundPointIF) {
        CompoundPointBuilder builder = new CompoundPointBuilder(
                CompoundPointTypeEnum.fromString(compoundPointIF.compoundPointType()),
                municipality,
                user);

        builder.setTitle(compoundPointIF.title())
                .setDescription(compoundPointIF.description())
                .addImage(compoundPointIF.images());
        compoundPointIF.pointsOfInterests().stream()
                .map(id -> MunicipalityRepository.getInstance().getGeoLocatableByID(id))
                .map(PointOfInterest.class::cast)
                .forEach(builder::addPointOfInterest);

        return builder.obtainResult();
    }


    private static void fillPointOfInterestBuilderFields(PointOfInterestBuilder builder, PointOfInterestIF pointOfInterestIF) {
        builder.setTitle(pointOfInterestIF.name())
                .setDescription(pointOfInterestIF.description());
        pointOfInterestIF.images().forEach(builder::addImage);

        try {
            if (!builder.getMunicipality().equals(mapProvider.getMunicipalityByPosition(pointOfInterestIF.position())))
                throw new IllegalArgumentException("The position is not in the municipality");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while checking the position");
        }

        builder.setPosition(pointOfInterestIF.position());

        setPointOfInterestClassification(builder, pointOfInterestIF);
    }

    private static void setPointOfInterestClassification(PointOfInterestBuilder builder,
                                                         PointOfInterestIF pointOfInterestIF) {
        builder.setClassification(PointOfInterest.stringToClass.get(pointOfInterestIF.classification()));
        if (pointOfInterestIF.classification().equals(Attraction.class.getSimpleName()))
            builder.setAttractionType(AttractionTypeEnum.fromString(pointOfInterestIF.type()));
        else if (pointOfInterestIF.classification().equals(Event.class.getSimpleName()))
            builder.setStartDate(pointOfInterestIF.startDate())
                    .setEndDate(pointOfInterestIF.endDate());
        else if (pointOfInterestIF.classification().equals(Activity.class.getSimpleName()))
            builder.setActivityType(ActivityTypeEnum.fromString(pointOfInterestIF.type()))
                    .setTimetable(pointOfInterestIF.timetable());
        else throw new IllegalArgumentException("Invalid classification");
    }


}

