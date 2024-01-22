package it.cs.unicam.app_valorizzazione_territorio.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.ProposalRequests;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.List;

/**
 * This interface represents a contest.
 * A contest can be private or public.
 * A contest can be approved or rejected.
 * A contest can have a geo-location or not.
 * A contest can have participants or not.
 */
public interface Contest extends Identifiable, Approvable, Searchable, Visualizable {

    /**
     * Returns true if the contest is private, false otherwise.
     *
     * @return true if the contest is private, false otherwise.
     */
    boolean isPrivate();

    /**
     * Returns the list of participants of the contest.
     *
     * @return the list of participants of the contest.
     * @throws UnsupportedOperationException if the contest is not private.
     */
    List<User> getParticipants() throws UnsupportedOperationException;

    /**
     * Returns true if the contest has a geo-location, false otherwise.
     *
     * @return true if the contest has a geo-location, false otherwise.
     */
    boolean hasGeoLocation();

    /**
     * Returns the geo-location of the contest.
     *
     * @return the geo-location of the contest.
     * @throws UnsupportedOperationException if the contest has no geo-location.
     */
    GeoLocatable getGeoLocation() throws UnsupportedOperationException;

    /**
     * Returns the animator of the contest.
     * @return the animator of the contest.
     */
    User getAnimator();

    /**
     * Returns the proposal requests of the contest.
     * @return the proposal requests of the contest.
     */
    ProposalRequests getProposalRequests();
}
