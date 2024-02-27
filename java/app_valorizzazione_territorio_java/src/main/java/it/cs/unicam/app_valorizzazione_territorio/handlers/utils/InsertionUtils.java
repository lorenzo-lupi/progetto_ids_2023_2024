package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.RequestJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * This class contains utility methods for the controllers of the geo-locatable items.
 */
@Component
public class InsertionUtils {

    private RequestJpaRepository requestRepository;

    @Autowired
    InsertionUtils(RequestJpaRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * Inserts the given item in the municipality and, if the user is a contributor, approves it.
     * @param item the item to insert
     * @param user the user who is inserting the item
     * @param municipality the municipality in which the item is inserted
     * @param storingAction the action to perform to store the item
     * @param <T> the type of the item
     */
    public <T extends Approvable & Visualizable> void insertItemApprovableByContributors(T item,
                                                                                         User user,
                                                                                         Municipality municipality,
                                                                                         Consumer<T> storingAction) {

        if (item == null || user == null || municipality == null || storingAction == null)
            throw new IllegalArgumentException("Item, user and municipality must not be null");

        storingAction.accept(item);
        if (Role.isAtLeastContributorForMunicipality(municipality).test(user)) {
            item.approve();
        } else {
            Request<?> request = RequestFactory.getApprovalRequest(item);
            request.setSender(user);
            requestRepository.save(request);
        }
    }

}
