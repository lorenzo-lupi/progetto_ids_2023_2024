package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;

import java.util.function.Consumer;

/**
 * This class contains utility methods for the controllers of the geo-locatable items.
 */
public class InsertionUtils {
    private static final RequestRepository requestRepository = RequestRepository.getInstance();

    /**
     * Inserts the given item in the municipality and, if the user is a contributor, approves it.
     * @param item the item to insert
     * @param user the user who is inserting the item
     * @param municipality the municipality in which the item is inserted
     * @param storingAction the action to perform to store the item
     * @param <T> the type of the item
     */
    public static <T extends Approvable & Visualizable> void insertItemApprovableByContributors(T item,
                                                                                                 User user,
                                                                                                 Municipality municipality,
                                                                                                 Consumer<T> storingAction) {

        if (item == null || user == null || municipality == null || storingAction == null)
            throw new IllegalArgumentException("Item, user and municipality must not be null");

        storingAction.accept(item);
        if (Role.isAtLeastContributorForMunicipality(municipality).test(user)) {
            item.approve();
        } else {
            requestRepository.add(RequestFactory.getApprovalRequest(item));
        }
    }

}
