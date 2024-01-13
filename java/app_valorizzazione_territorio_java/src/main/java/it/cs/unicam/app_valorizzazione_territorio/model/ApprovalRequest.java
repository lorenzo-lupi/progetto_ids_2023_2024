package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.Date;

/**
 * This class represents a request for approval of an item.
 * @param <I> the type of the item to be approved.
 *
 * @param user the user who made the request.
 * @param date the date of the request.
 * @param approvableItem the item to be approved.
 * @param municipality the municipality to which the item belongs.
 */
public record ApprovalRequest<I extends Approvable>(User user, Date date, I approvableItem, Municipality municipality) {

    /**
     * Sets the acceptance of the request.
     */
    public void setAcceptance(boolean acceptance) {
        this.approvableItem.setApproved(acceptance);
    }
}
