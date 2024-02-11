package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a generic request that can be approved or rejected, and that can perform an action
 * on a specific item (e.g. a content, a user, etc.) upon approval or rejection.
 *
 * @param <I> the type of the item related to the request.
 */
public abstract class Request<I extends Visualizable> implements Approvable, Identifiable, Visualizable {

    private User sender;
    private final RequestCommand<I> command;
    private final Date date;
    private String message;
    private ApprovalStatusEnum status;
    private final long ID = ApprovalRequestRepository.getInstance().getNextID();

    /**
     * Constructor for a request.
     * @param command the command that represents the request.
     */
    public Request(RequestCommand<I> command) {
        this(null, command, Calendar.getInstance().getTime(), null);
    }

    /**
     * Constructor for a request.
     *
     * @param sender the user who made the request.
     * @param command the command that represents the request.
     * @param message the message of the request.
     */
    public Request(User sender, RequestCommand<I> command, String message) {
        this(sender, command, Calendar.getInstance().getTime(), message);
    }

    /**
     * Constructor for a request.
     *
     * @param sender the user who made the request.
     * @param command the command that represents the request.
     * @param date the date of the request.
     * @param message the message of the request.
     */
    public Request(User sender, RequestCommand<I> command, Date date, String message) {
        this.sender = sender;
        this.command = command;
        this.date = date;
        this.message = message;
        this.status = ApprovalStatusEnum.PENDING;

    }

    public User getSender() {
        return sender;
    }

    public void setSender (User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public I getItem() {
        return command.getItem();
    }

    public Date getDate() {
        return date;
    }

    /**
     * Returns true if the user has the authorization to approve this request, false otherwise.
     *
     * @param user the user
     * @return true if the user has the authorization to approve this request, false otherwise.
     */
    public abstract boolean canBeApprovedBy(User user);

    /**
     * Approves the request.
     */
    @Override
    public void approve() {
        this.status = ApprovalStatusEnum.APPROVED;
        this.command.accept();
    }

    /**
     * Rejects the request.
     */
    @Override
    public void reject() {
        this.status = ApprovalStatusEnum.REJECTED;
        this.command.reject();
    }

    /**
     * Retrieves the current approval status of the item.
     *
     * @return ApprovalStatusEnum representing the current approval status.
     */
    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return this.status;
    }

    @Override
    public boolean isApproved() {
        return this.getApprovalStatus() == ApprovalStatusEnum.APPROVED;
    }

    @Override
    public long getID() {
        return this.ID;
    }
}