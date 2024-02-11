package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalCoordinatesException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.PositionParserException;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.osm.*;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

/**
 * This class handles the insertion of a point of interest.
 */
public class PointOfInterestInsertionHandler {
    private final User user;
    private final Municipality municipality;
    private final PointOfInterestBuilder builder;
    private PointOfInterest poi;
    private Map<? extends Positionable> map;

    /**
     * Constructor for a PointOfInterestInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the point of interest
     */
    public PointOfInterestInsertionHandler(long userId, long municipalityId) throws IOException {
        this.user = UserRepository.getInstance().getItemByID(userId);
        this.municipality = MunicipalityRepository.getInstance().getItemByID(municipalityId);
        this.builder = new PointOfInterestBuilder(municipality, user);
        this.map = new MapProviderProxy(new MapProviderBase()).getEmptyMap(municipality);
    }

    /**
     * Inserts the given point of interest in the municipality corresponding to the given ID.
     *
     * @param userId the ID of the user who is inserting the point of interest
     * @param municipalityId the ID of the municipality to which the point of interest is related
     * @param poiIF the DTO of the point of interest to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the point
     * of interest is not valid
     */
    public static void insertPointOfInterest(long userId, long municipalityId, PointOfInterestIF poiIF) {
        PointOfInterestBuilder builder =
                new PointOfInterestBuilder(MunicipalityRepository.getInstance().getItemByID(municipalityId),
                        UserRepository.getInstance().getItemByID(userId));

        builder.setTitle(poiIF.name())
                .setDescription(poiIF.description());
        poiIF.images().forEach(builder::addImage);
        builder.setPosition(poiIF.position())
                .setClassification(PointOfInterest.stringToClass.get(poiIF.classification()));

        if (poiIF.classification().equals(Attraction.class.getSimpleName()))
                builder.setAttractionType(AttractionTypeEnum.fromString(poiIF.type()));
        else if (poiIF.classification().equals(Event.class.getSimpleName()))
                builder.setStartDate(poiIF.startDate())
                        .setEndDate(poiIF.endDate());
        else if (poiIF.classification().equals(Activity.class.getSimpleName()))
                builder.setActivityType(ActivityTypeEnum.fromString(poiIF.type()))
                        .setTimetable(poiIF.timetable());
        else throw new IllegalArgumentException("Invalid classification");

        builder.build();

        GeoLocatableControllerUtils.insertGeoLocatable(builder.obtainResult(), UserRepository.getInstance().getItemByID(userId));
    }

    /**
     * Inserts the type of the point of interest.
     * @throws IllegalArgumentException if the type is not valid
     */
    public void insertClassification(String poiClassification) {
        if (poiClassification == null)
            throw new IllegalArgumentException("Type cannot be null");

        builder.setClassification(PointOfInterest.stringToClass.get(poiClassification));
    }

    public void insertAttractionType(String type){
        if (type == null)
            throw new IllegalArgumentException("Type cannot be null");

        builder.setAttractionType(AttractionTypeEnum.fromString(type));
    }

    public void insertActivityType(String type){
        if (type == null)
            throw new IllegalArgumentException("Type cannot be null");

        builder.setActivityType(ActivityTypeEnum.fromString(type));
    }


    /**
     * Inserts the start date of the point of interest.
     * @param startDate
     */
    public void insertStartDate(String startDate){
        if (startDate == null)
            throw new IllegalArgumentException("Start date cannot be null");

        builder.setStartDate(Date.valueOf(startDate));
    }


    /**
     * Inserts the end date of the point of interest.
     * @param endDate
     */
    public void insertEndDate(String endDate){
        if (endDate == null)
            throw new IllegalArgumentException("End date cannot be null");

        builder.setEndDate(Date.valueOf(endDate));
    }

    //TODO: insert all poi classification details
    /**
     * Returns the map of the point of interest in the detailed format.
     */
    public Identifiable getMap(){
        return map.getDetailedFormat();
    }

    /**
     * Modifies the map of the point of interest.
     * @param box the box of the map
     * @throws IOException if the map cannot be created
     */
    public  void handleMap(CoordinatesBox box) throws IOException {
        this.map = new MapProviderProxy(new MapProviderBase()).getEmptyMap(box);
    }

    /**
     * Inserts the coordinates of the point of interest.
     * @param coordinates the coordinates of the point of interest
     * @throws IllegalCoordinatesException the coordinates are not valid
     * @throws PositionParserException if the coordinates are not valid
     */
    public void insertCoordinates(String coordinates)
            throws IllegalCoordinatesException, PositionParserException {
        Position position = PositionParser.parse(coordinates);
        if(!municipality.getCoordinatesBox().contains(position))
            throw new IllegalCoordinatesException("The coordinates are not valid for this municipality");
        builder.setPosition(PositionParser.parse(coordinates));
    }

    /**
     * Inserts the name of the point of interest.
     * @param name the name of the point of interest
     */
    public void insertName(String name){
        builder.setTitle(name);
    }

    /**
     * Inserts the description of the point of interest.
     * @param description the description of the point of interest
     */
    public void insertDescription(String description){
        builder.setDescription(description);
    }

    /**
     * Inserts the image of the point of interest.
     * @param image the image of the point of interest
     */
    public void insertImage(File image){
        builder.addImage(image);
    }

    /**
     * remove the image from the point of interest
     */
    public void removeImage(File image){
        builder.removeImage(image);
    }


    /**
     * Creates the point of interest.
     */
    public void createPointOfInterest(){
        this.poi = this.builder.build()
                .obtainResult();
    }

    /**
     * Inserts the point of interest in the Municipality.
     */
    public long insertPointOfInterest(){
        GeoLocatableControllerUtils
                .insertGeoLocatable(poi, user);

        return poi.getID();
    }

}
