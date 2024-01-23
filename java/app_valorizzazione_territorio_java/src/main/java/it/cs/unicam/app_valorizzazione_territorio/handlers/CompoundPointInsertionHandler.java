package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.PointOfInterestSOF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.TypeNotSetException;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;

import java.util.List;
import java.io.File;

import static it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion.EQUALS_ID;

public class CompoundPointInsertionHandler {
    private Municipality municipality;
    private final long userId;
    private CompoundPointBuilder builder;
    private CompoundPoint compoundPoint;

    /**
     * Constructor for a CompoundPointInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the compound point
     */
    public CompoundPointInsertionHandler(long userId) {
        this.userId = userId;
    }

    /**
     * Sets the municipality in which the compound point will be inserted.
     * @param municipalityID the ID of the municipality
     */
    private void setMunicipality(long municipalityID) {
        this.municipality = MunicipalityRepository.getInstance().getItemByID(municipalityID);
        if(municipality == null)
            throw new IllegalArgumentException("Municipality not found");
    }

    /**
     * Inserts the type of the compound point.
     */
    public void insertType(String type) {
        if(type == null)
            throw new IllegalArgumentException("Type cannot be null" );


        builder = new CompoundPointBuilder(CompoundPointTypeEnum.fromString(type),
                municipality);
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

        builder.addPointOfInterest(poiFromID(pointOfInterestID));
    }

    /**
     * Returns the added point of interests.
     * @return the added point of interests
     * @throws TypeNotSetException if the type has not been inserted yet
     */
    public List<PointOfInterestSOF> getAddedPointOfInterests() {
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

        builder.eliminatePointOfInterest(poiFromID(pointOfInterestID));
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


    //TODO: del added file


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
        if (compoundPoint == null)
            throw new IllegalStateException("Compound point must be created first");
        User user = UserRepository.getInstance().getItemByID(userId);
        if(Role.isAtLeastContributorForMunicipality(municipality).test(user)){
            municipality.addGeoLocatable(compoundPoint);
        }
        else {
            ApprovalRequestRepository.getInstance().add(
                    new MunicipalityApprovalRequest(user, compoundPoint, municipality));
        }
    }


    private PointOfInterest  poiFromID(long pointOfInterestID){
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(municipality.getGeoLocatables());
        searchEngine.addCriterion(Parameter.ID, EQUALS_ID, pointOfInterestID);
        GeoLocatable result = searchEngine.search().getResults().stream().findFirst().orElse(null);

        if (result == null)
            throw new IllegalArgumentException("Point of interest not found");
        if(!(result instanceof PointOfInterest pointOfInterest))
            throw new IllegalArgumentException("Wrong poi id");

        return pointOfInterest;
    }

}
