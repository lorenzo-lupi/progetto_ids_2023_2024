package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Deletable;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;


import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;


/**
 * This class provides the methods to handle the creation of a new report.
 * Only deletable objects can be reported.
 *
 */
public class ReportInsertionHandler {

    /**
     * This method creates a new report for a GeoLocatable object.
     * @param userID the id of the user who creates the report
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertGeoLocatableReport(long userID,
                                                long objectID,
                                                String description){
            if(!MunicipalityRepository.getInstance().containsGeoLocatable(objectID))
                throw new IllegalArgumentException("GeoLocatable not found");
            GeoLocatable item = MunicipalityRepository.getInstance().getGeoLocatableByID(objectID);
            return addReport(RequestFactory.getDeletionRequest(getUser(userID), item, description));
    }

    /**
     * This method creates a new report for a PointOfInterestContent object.
     * @param userID the id of the user who creates the report
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertPointerOfInterestReport(long userID,
                                                     long objectID,
                                                     String description){
        if(!MunicipalityRepository.getInstance().containsContent(objectID))
            throw new IllegalArgumentException("Content not found");
        if(!(MunicipalityRepository.getInstance().getContentByID(objectID) instanceof PointOfInterestContent item))
            throw new IllegalArgumentException("Content is not a PointOfInterestContent");
        return addReport(RequestFactory.getDeletionRequest(getUser(userID), item, description));

    }

    /**
     * This method creates a new report for a ContestContent object.
     * @param userID the id of the user who creates the report
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertContentContestReport(long userID,
                                                  long objectID,
                                                  String description){
        if(!MunicipalityRepository.getInstance().containsContent(objectID))
            throw new IllegalArgumentException("Content not found");
        if(!(MunicipalityRepository.getInstance().getContentByID(objectID) instanceof ContestContent item))
            throw new IllegalArgumentException("Content is not a ContestContent");
        return addReport(RequestFactory.getDeletionRequest(getUser(userID), item, description));
    }

    private static long addReport(Request<?> request){
        RequestRepository.getInstance().add(request);
        return request.getID();
    }

    private static User getUser(long userID){
        if(!UserRepository.getInstance().contains(userID))
            throw new IllegalArgumentException("User not found");
        return UserRepository.getInstance().getItemByID(userID);
    }


}
