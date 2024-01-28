package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

/**
 * This class handles the insertion of a content.
 */
public class PointOfInterestContentInsertionHandler extends ContentInsertionHandler<PointOfInterest,
        PointOfInterestContent>{
    private User user;
    /**
     * Constructor for a ContentInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the content
     * @param poiId the ID of the point of interest to which the content is related
     */
    public PointOfInterestContentInsertionHandler(long userId, long poiId) {
        super(new PointOfInterestContentBuilder((PointOfInterest) MunicipalityRepository.getInstance().getGeoLocatableByID(poiId),
                        UserRepository.getInstance().getItemByID(userId)));
        this.user = UserRepository.getInstance().getItemByID(userId);
    }

    /**
     * Inserts the content in its Point of interest.
     */
    public void insertContent(){
        PointOfInterestContent content = super.getContent();
        if(content == null)
            throw new IllegalStateException("Content must first be created");
        GeoLocatableControllerUtils.insertPoiContent(content, user);
    }
}
