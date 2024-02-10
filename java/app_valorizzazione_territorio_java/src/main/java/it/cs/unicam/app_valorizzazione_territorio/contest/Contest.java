package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This interface represents a contest.
 * A contest can be private or public.
 * A contest can have a geolocation or not.
 * A contest can have participants or not.
 */
public interface Contest extends Identifiable, Searchable, Visualizable, ContentHost<Contest> {

    /**
     * Returns true if the contest is private, false otherwise.
     *
     * @return true if the contest is private, false otherwise.
     */
    Municipality getMunicipality();

    boolean isPrivate();

    /**
     * Returns the list of participants of the contest.
     *
     * @return the list of participants of the contest.
     * @throws UnsupportedOperationException if the contest is not private.
     */
    List<User> getParticipants() throws UnsupportedOperationException;

    /**
     * Checks if the given user is permitted to participate in the contest.
     * A user is permitted to participate in a contest if the contest is public or
     * if it private and the user is a participant.
     *
     * @param user the user to check
     * @return true if the given user is permitted to participate in the contest, false otherwise.
     */
    default boolean permitsUser(User user) {
        return !this.isPrivate() || this.getParticipants().contains(user);
    }

    /**
     * Returns true if the contest has a geolocation, false otherwise.
     *
     * @return true if the contest has a geolocation, false otherwise.
     */
    boolean hasGeoLocation();

    /**
     * Returns the geolocation of the contest.
     *
     * @return the geolocation of the contest.
     * @throws UnsupportedOperationException if the contest has no geolocation.
     */
    GeoLocatable getGeoLocation() throws UnsupportedOperationException;

    String getName();

    /**
     * Returns the animator of the contest.
     * @return the animator of the contest.
     */
    User getEntertainer();
    String getTopic();
    String getRules();
    Date getStartDate();
    Date getVotingStartDate();
    Date getEndDate();
    default ContestStatusEnum getStatus() {
        if ((new Date()).before(getStartDate()))
            return ContestStatusEnum.PLANNED;
        else if ((new Date()).before(getVotingStartDate()))
            return ContestStatusEnum.OPEN;
        else if ((new Date()).before(getEndDate()))
            return ContestStatusEnum.VOTING;
        else
            return ContestStatusEnum.CLOSED;
    }

    /**
     * Returns the proposal requests of the contest.
     * @return the proposal requests of the contest.
     */
    ProposalRequests getProposalRequests();

    default ContestSOF getSynthesizedFormat() {
        return new ContestSOF(this.getName(),
                this.getStatus().toString(),
                this.getID());
    }

    @Override
    default ContestDOF getDetailedFormat() {
        return new ContestDOF(this.getName(),
                this.getEntertainer().getSynthesizedFormat(),
                this.getTopic(),
                this.getRules(),
                this.isPrivate(),
                (this.hasGeoLocation() ? this.getGeoLocation() : null),
                this.getStatus().toString(),
                this.getStartDate(),
                this.getVotingStartDate(),
                this.getEndDate(),
                this.getID());
    }

    @Override
    default Map<Parameter, Object> getParametersMapping() {
        return Map.of(
                Parameter.THIS, this,
                Parameter.NAME, this.getName(),
                Parameter.CONTEST_TOPIC, this.getTopic(),
                Parameter.CONTEST_STATUS, this.getStatus());
    }
}
