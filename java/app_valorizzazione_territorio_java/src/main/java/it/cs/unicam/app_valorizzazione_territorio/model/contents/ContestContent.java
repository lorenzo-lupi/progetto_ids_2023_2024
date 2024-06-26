package it.cs.unicam.app_valorizzazione_territorio.model.contents;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a contest content. A ContestContent is hosted in a Contest
 */
@Entity
@NoArgsConstructor(force = true)
@DiscriminatorValue("ContestContent")
public class ContestContent extends Content<Contest> {
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Contest contest;
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_content_voters",
            joinColumns = @JoinColumn(name = "contest_content_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "ID"))
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

    @SuppressWarnings("UnusedReturnValue")
    public boolean addVoter(User user) {
        return this.voters.add(user);
    }
    @SuppressWarnings("UnusedReturnValue")
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
            this.contest.getProposalRegister().removeProposal(this);
        };
    }

    @PreRemove
    public void preRemove() {
        super.preRemove();
        if (this.contest != null) this.contest.removeContent(this);
    }
}
