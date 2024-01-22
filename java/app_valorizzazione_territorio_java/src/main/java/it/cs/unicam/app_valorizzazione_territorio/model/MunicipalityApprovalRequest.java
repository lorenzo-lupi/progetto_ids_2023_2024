package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;

import java.util.Date;
import java.util.Calendar;

public class MunicipalityApprovalRequest<I extends Approvable> extends ApprovalRequest<I>{

    private final Municipality municipality;

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param approvableItem the item to be approved.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityApprovalRequest(User user, I approvableItem, Municipality municipality) {
        this(user, approvableItem, municipality, Calendar.getInstance().getTime());
    }

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param date the date of the request.
     * @param approvableItem the item to be approved.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityApprovalRequest(User user, I approvableItem, Municipality municipality, Date date) {
        super(user, approvableItem, date);
        this.municipality = municipality;
    }

    @Override
    public boolean canBeApprovedBy(User user) {
        return user.getAuthorizations(municipality).contains(RoleTypeEnum.CURATOR);
    }
}
