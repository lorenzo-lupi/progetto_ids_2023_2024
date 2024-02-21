package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class ProposalRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @Transient // TODO : @OneToMany(fetch = FetchType.EAGER, mappedBy = "contest")
    private Set<ContestContent> proposals;

    public ProposalRegister() {
        proposals = new HashSet<>();
    }

    /**
     * Returns the list of proposed contents combined with their number of received votes from users.
     *
     * @return the list of proposals.
     */
    public List<VotedContent> getProposals() {
        return proposals
                .stream()
                .map(content -> new VotedContent(content, content.getVoters().size()))
                .toList();
    }

    /**
     * Checks if a user has voted for a content.
     *
     * @param user the user to check
     * @return true if the user has voted for a content, false otherwise
     */
    public boolean hasVoted(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");

        return proposals
                .stream()
                .anyMatch(content -> content.getVoters().contains(user));
    }

    /**
     * Adds a vote to the given content by the given user.
     *
     * @param content the content to be voted.
     * @param user    the user who wants to vote.
     * @throws IllegalArgumentException if the content or the user are null,
     * if the content is not in the list or if the user has already voted.
     */
    public void addVote(ContestContent content, User user) {
        if (content == null || user == null)
            throw new IllegalArgumentException("Parameters must not be null");

        if (!proposals.contains(content))
            throw new IllegalArgumentException("Content not found");

        if (hasVoted(user))
            throw new IllegalArgumentException("User already voted");

        content.addVoter(user);
    }

    /**
     * Removes a user's vote from his voted content of this contest, if any.
     *
     * @param user the user who wants to remove his vote.
     * @throws IllegalArgumentException if the user has not voted for any content.
     */
    public void removeVote(User user) {
        if (user == null)
            throw new IllegalArgumentException("Parameters must not be null");

        if (!hasVoted(user))
            throw new IllegalArgumentException("User has not voted");

        proposals.stream()
                .filter(content -> content.getVoters().contains(user))
                .forEach(content -> content.removeVoter(user));
    }

    /**
     * Adds a content to the list of proposals.
     *
     * @param content the content to be added.
     * @throws IllegalArgumentException if the content is already in the list.
     */
    public void proposeContent(ContestContent content) {
        if (content == null)
            throw new IllegalArgumentException("Content must not be null");

        if (proposals.contains(content))
            throw new IllegalArgumentException("Content already exists");

        proposals.add(content);
    }

    /**
     * Removes a content from the list of proposals.
     *
     * @param content the content to be removed.
     * @throws IllegalArgumentException if the content is not in the list.
     */
    public ContestContent removeProposal(ContestContent content) {
        if (content == null)
            throw new IllegalArgumentException("Content must not be null");

        if (!proposals.contains(content))
            throw new IllegalArgumentException("Content not found");

        proposals.remove(content);

        return content;
    }

    /**
     * Returns the content with the most votes.
     * If there are no proposals, the method returns null.
     */
    public ContestContent getWinner(){
        return proposals
                .stream()
                .max(Comparator.comparingInt(content -> content.getVoters().size()))
                .orElse(null);
    }

}
