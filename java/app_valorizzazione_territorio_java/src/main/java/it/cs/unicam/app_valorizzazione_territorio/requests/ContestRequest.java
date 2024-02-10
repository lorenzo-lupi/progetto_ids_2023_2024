package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;

import java.util.Date;

public class ContestRequest extends Request<ContestContent> {
    private final Contest contest;

    /**
     * Constructor for a contest approval request.
     *
     * @param command the item to be approved.
     */
    public ContestRequest(RequestCommand<ContestContent> command) {
        super(command);
        this.contest = command.getItem().getHost();
    }

    /**
     * Constructor for a contest approval request.
     */
    public ContestRequest(User user, RequestCommand<ContestContent> command, Date date, String message) {
        super(user, command, date, message);
        this.contest = command.getItem().getHost();
    }

    public Contest getContest() {
        return contest;
    }

    @Override
    public boolean canBeApprovedBy(User user) {
        return user.equals(contest.getEntertainer());
    }

    @Override
    public ContestRequestSOF getSynthesizedFormat() {
        return new ContestRequestSOF(this.getSender().getUsername(), this.getContest().getName(), this.getDate(), this.getID());
    }

    @Override
    public ContestRequestDOF getDetailedFormat() {
        return new ContestRequestDOF(this.getSender().getSynthesizedFormat(), this.getContest().getSynthesizedFormat(),
                this.getDate(), this.getItem().getDetailedFormat(), this.getID());
    }
}
