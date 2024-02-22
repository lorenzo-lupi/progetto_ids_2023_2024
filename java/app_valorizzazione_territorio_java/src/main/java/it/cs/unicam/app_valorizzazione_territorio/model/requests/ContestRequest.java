package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@DiscriminatorValue("Contest")
@NoArgsConstructor(force = true)
public class ContestRequest extends Request<ContestContent> {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id")
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
    public ContestRequest(User user, RequestCommand<ContestContent> command, String message) {
        super(user, command, message);
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
    public ContestRequestOF getOutputFormat() {
        return new ContestRequestOF(
                this.getSender().getOutputFormat(),
                this.getContest().getName(),
                this.getContest().getOutputFormat(),
                this.getDate(),
                this.getItem().getOutputFormat(),
                this.getID());
    }
}
