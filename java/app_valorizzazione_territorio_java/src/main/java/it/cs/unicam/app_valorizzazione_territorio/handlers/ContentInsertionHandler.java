package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.io.File;
import java.util.List;

/**
 * This class handles the insertion of a content.
 */
public class ContentInsertionHandler {
    private User user;
    private PointOfInterest poi;
    private PointOfInterestContent content;
    private PointOfInterestContentBuilder builder;
    /**
     * Constructor for a ContentInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the content
     * @param poiId the ID of the point of interest to which the content is related
     */
    public ContentInsertionHandler(long userId, long poiId) {
        this.user = UserRepository.getInstance().getItemByID(userId);
        this.poi = MunicipalityRepository.getInstance().getPointOfInterestByID(poiId);
        this.builder = new PointOfInterestContentBuilder(poi);
    }

     /**
     * Inserts the description of the content.
     * @param description the description of the content
     */
    public void insertDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        builder.buildDescription(description);
    }

    /**
     * Adds a file to the content.
     * @param file the file to added
     */
    public void addFile(File file){
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");
        builder.buildFile(file);
    }
    /**
     * Removes a file from the content.
     * @param file the file to removed
     */
    public void removeFile(File file) {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");
        builder.removeFile(file);
    }

    /**
     * Returns the files added to the content.
     * @return the files added to the content
     */
    public List<File> obtainAddedFiles(){
        return builder.getFiles();
    }

    /**
     * Destroys the content.
     */
    public void destroyContent(){
        builder.reset();
    }


    /**
     * Creates the content.
     */
    public void createContent(){
        this.content = builder.build();
    }

    /**
     * Inserts the content in its Point of interest.
     */
    public void insertContent(){
        GeoLocatableControllerUtils.insertContent(content, user);
    }
}
