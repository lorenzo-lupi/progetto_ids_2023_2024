package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;

/**
 * This class provides the methods to handle the deletion of a GeoLocatable object.
 */
public class DeleteGeoLocatableHandler {

    /**
     * This method deletes a GeoLocatable object.
     *
     * @param objectID the id of the object to delete
     * @param userId   the id of the user who deletes the object
     */
    public static long deleteGeoLocatable(long userId, long objectID, String message) {
        if (!UserRepository.getInstance().contains(userId))
            throw new IllegalArgumentException("User not found");
        if (!MunicipalityRepository.getInstance().containsGeoLocatable(objectID))
            throw new IllegalArgumentException("GeoLocatable not found");

        return createGeoLocatableDeletionRequest(
                MunicipalityRepository.getInstance().getGeoLocatableByID(objectID),
                UserRepository.getInstance().getItemByID(userId),
                message);
    }

    private static long createGeoLocatableDeletionRequest(GeoLocatable item, User user, String message) {
        Request<?> request = RequestFactory.getDeletionRequest(user, item, message);
        RequestRepository.getInstance().add(request);
        return request.getID();
    }

}
