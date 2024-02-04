package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.TypeNotSetException;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.IdsUtils;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.CompoundPoint;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.util.List;
import java.io.File;

public class CompoundPointInsertionHandler {
    private final Municipality municipality;
    private final User user;
    private CompoundPointBuilder builder;
    private CompoundPoint compoundPoint;


    /**
     * Constructor for a CompoundPointInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the compound point
     */
    public CompoundPointInsertionHandler(long userId, long municipalityId) {
        this.user = UserRepository.getInstance().getItemByID(userId);
        this.municipality = MunicipalityRepository.getInstance().getItemByID(municipalityId);
    }


    /**
     * Inserts the type of the compound point.
     */
    public void insertType(String type) {
        if(type == null)
            throw new IllegalArgumentException("Type cannot be null" );


        builder = new CompoundPointBuilder(CompoundPointTypeEnum.fromString(type),
                municipality, user);
    }


    /**
     * Inserts the title of the compound point.
     * @param title the title of the compound point
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void insertTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        builder.setTitle(title);
    }

    /**
     * Inserts the description of the compound point.
     * @param description the description of the compound point
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void insertDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        builder.setDescription(description);
    }

    /**
     * inserts an image in the compound point.
     * @param file the image to insert
     */
    public void insertImage(File file) {
        if (file == null)
            throw new IllegalArgumentException("Image cannot be null");
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        builder.addImage(file);
    }

    /**
     * Inserts a point of interest in the compound point.
     * @param pointOfInterestID the ID of the point of interest to insert
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void addPointOfInterest(long pointOfInterestID) {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        builder.addPointOfInterest(IdsUtils.getPoiFromID(pointOfInterestID, municipality));
    }

    /**
     * Returns the added point of interests.
     * @return the added point of interests
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public List<GeoLocatableSOF> getAddedPointOfInterests() {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        return builder.getPointOfInterests().stream()
                .map(PointOfInterest::getSynthesizedFormat)
                .toList();
    }

    /**
     * Deletes a point of interest from the compound point.
     * @param pointOfInterestID the ID of the point of interest to delete
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void deletePointOfInterest(long pointOfInterestID) {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        builder.eliminatePointOfInterest(IdsUtils.getPoiFromID(pointOfInterestID, municipality));
    }

    /**
     * Returns the added files.
     * @return the added files
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public List<File> getAddedFiles() {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");

        return builder.getImages();
    }

    /**
     * Deletes a file from the compound point.
     * @param file the file to delete
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void deleteFile(File file) {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");
        if(file == null )
            throw new IllegalArgumentException("File cannot be null");
        builder.removeImage(file);
    }


    /**
     * Creates the compound point.
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public void createCompoundPoint() {
        if (builder == null)
            throw new TypeNotSetException("Type must be inserted first");
        this.compoundPoint = builder.obtainResult();
    }


    /**
     * Inserts the compound point in the municipality.
     * @throws IllegalStateException if the compound point has not been created yet
     */
    public void insertCompoundPoint() {
        GeoLocatableControllerUtils.insertGeoLocatable(this.compoundPoint, this.user);
    }




}
