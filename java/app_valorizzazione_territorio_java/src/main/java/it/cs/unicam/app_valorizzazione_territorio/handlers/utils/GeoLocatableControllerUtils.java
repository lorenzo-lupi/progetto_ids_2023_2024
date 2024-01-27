package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityApprovalRequest;

import java.util.function.Consumer;

/**
 * This class contains utility methods for the controllers of the geo-locatable items.
 */
public class GeoLocatableControllerUtils {

    /**
     * Inserts a geo-locatable item.
     * @param geoLocatable the geo-locatable item to be inserted
     * @param user the user who is inserting the item
     */
    public static void insertGeoLocatable(GeoLocatable geoLocatable, User user) {

        Municipality municipality = geoLocatable.getMunicipality();
        insertItemApprovableByContributors(geoLocatable, user, municipality, municipality::addGeoLocatable);
    }

    /**
     * Inserts a content.
     * @param content the content to be inserted
     * @param user the user who is inserting the content
     */
    public static void insertContent(PointOfInterestContent content, User user) {

        PointOfInterest pointOfInterest = content.getHost();
        insertItemApprovableByContributors(content, user, pointOfInterest.getMunicipality(), pointOfInterest::addContent);
    }

    private static <T extends Approvable & Visualizable> void insertItemApprovableByContributors(T item,
                                                                                                 User user,
                                                                                                 Municipality municipality,
                                                                                                 Consumer<T> storingAction) {

        if (item == null || user == null || municipality == null || storingAction == null)
            throw new IllegalArgumentException("Item, user and municipality must not be null");

        storingAction.accept(item);
        if (Role.isAtLeastContributorForMunicipality(municipality).test(user)) {
            item.approve();
        } else {
            ApprovalRequestRepository.getInstance().add(
                    new MunicipalityApprovalRequest<>(user, item, municipality));
        }

    }
}
