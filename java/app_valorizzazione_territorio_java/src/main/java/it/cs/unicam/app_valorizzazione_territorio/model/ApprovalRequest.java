package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;

import java.util.Date;
import java.util.function.Predicate;

/**
 * This class represents a request for approval of an item.
 * @param <I> the type of the item to be approved.
 *
 * @param user the user who made the request.
 * @param date the date of the request.
 * @param approvableItem the item to be approved.
 */
public record ApprovalRequest <I extends Approvable> (User user,
                                                      Date date,
                                                      I approvableItem,
                                                      Predicate<User> canApprove) implements Approvable {

    /**
     * Returns true if the user can approve the item, false otherwise.
     */
    @Override
    public boolean isApproved() {
        return approvableItem.isApproved();
    }

    /**
     * Rejects the item.
     */
    @Override
    public void reject() {
        approvableItem.reject();
    }

    /**
     * Approves the item.
     */
    @Override
    public void approve() {
        approvableItem.approve();
    }

    /**
     * Retrieves the current approval status of the item.
     *
     * @return ApprovalStatusENUM representing the current approval status.
     */
    @Override
    public ApprovalStatusENUM getApprovalStatus() {
        return approvableItem.getApprovalStatus();
    }
}
