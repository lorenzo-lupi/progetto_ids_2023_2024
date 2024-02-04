package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.*;

public class ProposalRequests {
    private Map<ContestContent, Collection<User>> votes;
    //private List<>

    public ProposalRequests() {
        votes = new HashMap<>();
    }

    /**
     * Returns the list of proposed contents combined with their number of received votes from users.
     *
     * @return the list of proposals.
     */
    public List<VotedContent> getProposals() {
        return votes.keySet()
                .stream()
                .map(content -> new VotedContent(content, votes.get(content).size()))
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

        return votes.values()
                .stream()
                .anyMatch(users -> users.contains(user));
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

        if (!votes.containsKey(content))
            throw new IllegalArgumentException("Content not found");

        if (votes.get(content).contains(user))
            throw new IllegalArgumentException("User already voted");

        votes.get(content).add(user);
    }

    /**
     * Removes a user's vote from a content.
     *
     * @param user the user who wants to remove his vote.
     * @throws IllegalArgumentException if the user has not voted for any content.
     */
    public void removeVote(User user) {
        if (user == null)
            throw new IllegalArgumentException("Parameters must not be null");

        votes.values()
                .stream().filter(users -> users.contains(user))
                .forEach(users -> users.remove(user));
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

        if (votes.containsKey(content))
            throw new IllegalArgumentException("Content already exists");

        votes.put(content, new HashSet<>());
    }

    /**
     * Removes a content from the list of proposals.
     *
     * @param content the content to be removed.
     * @throws IllegalArgumentException if the content is not in the list.
     */
    public PointOfInterestContent removeProposal(PointOfInterestContent content) {
        if (content == null)
            throw new IllegalArgumentException("Content must not be null");

        if (!votes.containsKey(content))
            throw new IllegalArgumentException("Content not found");

        return content;
    }

    /**
     * Returns the content with the most votes.
     */
    public ContestContent getWinner(){
        return votes.keySet()
                .stream()
                .max((content, content2) -> votes.get(content).size() - votes.get(content2).size())
                .orElse(null);
    }

}
