package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Date;

public class ContestApprovalRequest extends ApprovalRequest<Content>{
    private final Contest contest;

    /**
     * Constructor for a contest approval request.
     *
     * @param user the user who made the request.
     * @param approvableItem the item to be approved.
     * @param contest the contest to which the request is addressed.
     */
    public ContestApprovalRequest(User user, Content approvableItem, Contest contest) {
        super(user, approvableItem);
        this.contest = contest;
    }

    /**
     * Constructor for a contest approval request.
     *
     * @param user the user who made the request.
     * @param approvableItem the item to be approved.
     * @param contest the contest to which the request is addressed.
     * @param date the date of the request.
     */
    public ContestApprovalRequest(User user, Content approvableItem, Contest contest, Date date) {
        super(user, approvableItem, date);
        this.contest = contest;
    }

    public Contest getContest() {
        return contest;
    }

    @Override
    public boolean canBeApprovedBy(User user) {
        return user.equals(contest.getAnimator());
    }

    @Override
    public ContestRequestSOF getSynthesizedFormat() {
        return new ContestRequestSOF(this.getUser().getUsername(), this.getContest().getName(), this.getDate(), this.getID());
    }

    @Override
    public Identifiable getDetailedFormat() {
        return new ContestRequestDOF(this.getUser().getSynthesizedFormat(), this.getContest().getSynthesizedFormat(),
                this.getDate(), this.getApprovableItem().getDetailedFormat(), this.getID());
    }
}