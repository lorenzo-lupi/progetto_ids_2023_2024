package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusENUM;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

/**
 * This class represents a request for approval of an item.
 * @param <I> the type of the item to be approved.
 */
public abstract class ApprovalRequest<I extends Approvable> implements Approvable, Identifiable {

    private final User user;
    private final I approvableItem;
    private final Date date;
    private final long ID = ApprovalRequestRepository.getInstance().getNextID();

    public static Predicate<User> getMunicipalityPredicate(Municipality municipality) {
        return user -> user.getAuthorizations(municipality).contains(RoleTypeEnum.CURATOR);
    }

    public static Predicate<User> getContestPredicate(User animator) {
        return user -> user.equals(animator);
    }

    public ApprovalRequest(User user, I approvableItem) {
        this(user, approvableItem, Calendar.getInstance().getTime());
    }
    /**
     * Constructor for an approval request.
     *
     * @param user the user who made the request.
     * @param approvableItem the item to be approved.
     * @param date the date of the request.
     */
    public ApprovalRequest(User user, I approvableItem, Date date) {
        this.user = user;
        this.date = date;
        this.approvableItem = approvableItem;
    }

    /**
     * Returns true if the user has the authorization to approve this request, false otherwise.
     *
     * @param user the user
     * @return true if the user has the authorization to approve this request, false otherwise.
     */
    public abstract boolean canBeApprovedBy(User user);

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

    @Override
    public long getID() {
        return this.ID;
    }
}
