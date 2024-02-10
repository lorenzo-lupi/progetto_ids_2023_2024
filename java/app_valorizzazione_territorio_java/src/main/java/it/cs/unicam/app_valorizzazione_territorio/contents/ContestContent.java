package it.cs.unicam.app_valorizzazione_territorio.contents;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents a contest content. A ContestContent is hosted in a Contest
 */
public class ContestContent extends Content<Contest> {
    private final Contest contest;
    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files       the multimedia files of the content
     * @throws IllegalArgumentException if description, pointOfInterest or files are null
     */
    public ContestContent(String description, Contest contest, List<File> files, User user) {
        super(description, files, user);
        if (contest == null)
            throw new IllegalArgumentException("Contest cannot be null");
        this.contest = contest;

    }

    @Override
    public Contest getHost() {
        return this.contest;
    }

    @Override
    public Runnable getDeletionAction() {
        return () -> this.contest.getProposalRequests().removeProposal(this);
    }
}
