package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
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
     * Inserts the content obtained from the given contentIF in the point of interest with the given ID.
     * If the content insertion has success, an approval request may be created for the municipality of the point
     * of interest depending on the user's role.
     *
     * @param userID the ID of the user who is inserting the content
     * @param poiID the ID of the point of interest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public static long insertContent(long userID, long poiID, ContentIF contentIF){
        User user = UserRepository.getInstance().getItemByID(userID);
        PointOfInterest pointOfInterest = MunicipalityRepository.getInstance().getPointOfInterestByID(poiID);

        PointOfInterestContent content = ContentInsertionHandler.createContent(
                new PointOfInterestContentBuilder(pointOfInterest, user), contentIF);

        GeoLocatableControllerUtils.insertPoiContent(content, user);
        return content.getID();
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
