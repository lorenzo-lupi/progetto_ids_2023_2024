package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProposalRequests {
    private Map<Content, Collection<User>> votes;

    public ProposalRequests() {
        votes = new HashMap<>();
    }

    /**
     * Checks if a user has voted for a content.
     * @param user
     * @return
     */
    public boolean hasVoted(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");

        return votes.values()
                .stream()
                .anyMatch(users -> users.contains(user));
    }

    /**
     * Checks if a content has been proposed.
     * @param content
     * @return
     */
    public void addVote(Content content, User user) {
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
     * @param content the content to be added.
     * @throws IllegalArgumentException if the content is already in the list.
     */
    public void proposeContent(Content content) {
        if (content == null)
            throw new IllegalArgumentException("Content must not be null");

        if (votes.containsKey(content))
            throw new IllegalArgumentException("Content already exists");

        votes.put(content, new HashSet<>());
    }

    /**
     * Removes a content from the list of proposals.
     * @param content the content to be removed.
     * @throws IllegalArgumentException if the content is not in the list.
     */
    public Content removeProposal(Content content) {
        if (content == null)
            throw new IllegalArgumentException("Content must not be null");

        if (!votes.containsKey(content))
            throw new IllegalArgumentException("Content not found");

        return content;
    }

    /**
     * Returns the content with the most votes.
     */
    public Content getWinner(){
        return votes.keySet()
                .stream()
                .max((content, content2) -> votes.get(content).size() - votes.get(content2).size())
                .orElse(null);
    }

}
