package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.IdsUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.osm.Map;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.io.File;
import java.io.IOException;

/**
 * This class handles the insertion of a point of interest.
 */
public class PointOfInterestInsertionHandler {
    private User user;
    private Municipality municipality;
    private PointOfInterestBuilder builder;
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
        this.builder = new PointOfInterestBuilder(municipality);
        this.map = MapProvider.getEmptyMap(municipality);
    }

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
        this.map = MapProvider
                .getEmptyMap(box);
    }

    /**
     * Inserts the coordinates of the point of interest.
     * @param coordinates the coordinates of the point of interest
     * @throws IllegalArgumentException if the coordinates are not valid
     */
    public void insertCoordinates(String coordinates)
            throws IllegalArgumentException{
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
    public void insertPointOfInterest(){
        GeoLocatableControllerUtils
                .insertCompoundPoint(poi, user, municipality);
    }

}
