package it.cs.unicam.app_valorizzazione_territorio.model.contents;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a contest content. A ContestContent is hosted in a Contest
 */
public class ContestContent extends Content<Contest> {
    private final Contest contest;
    private final List<User> voters;
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
        this.voters = new ArrayList<>();
    }

    public List<User> getVoters() {
        return this.voters;
    }

    public boolean addVoter(User user) {
        return this.voters.add(user);
    }

    public boolean removeVoter(User user) {
        return this.voters.remove(user);
    }

    @Override
    public Contest getHost() {
        return this.contest;
    }

    @Override
    public Runnable getDeletionAction() {
        return () -> {
            this.contest.getProposalRequests().removeProposal(this);
            MunicipalityRepository.getInstance().removeContent(this);
        };
    }
}
