package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This interface represents a contest.
 * A contest can be private or public.
 * A contest can have a geolocation or not.
 * A contest can have participants or not.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contest_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(force = true)
public abstract class Contest implements Searchable, Visualizable, ContentHost<Contest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    //ID of the decorated base contest.
    @Getter
    @Setter
    private long baseContestId;

    //Determines if this object is the last added object in the chain of decorators.
    private boolean valid;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    /////// FOR DELETION PURPOSES /////////
    @Getter
    @OneToMany(fetch = FetchType.EAGER)
    private List<Notification> notifications;
    //////////////////////////////////////

    public Contest(Municipality municipality) {
        if (municipality == null)
            throw new IllegalArgumentException("Municipality must not be null");
        this.municipality = municipality;
        this.valid = true;
        this.notifications = new ArrayList<>();
    }

    public boolean isValid() {
        return valid;
    }

    public boolean setValid(boolean valid) {
        this.valid = valid;
        return valid;
    }

    /**
     * Returns true if the contest is private, false otherwise.
     *
     * @return true if the contest is private, false otherwise.
     */
    public abstract boolean isPrivate();

    public Municipality getMunicipality() {
        return this.municipality;
    }

    /**
     * Returns the list of participants of the contest.
     *
     * @return the list of participants of the contest.
     * @throws UnsupportedOperationException if the contest is not private.
     */
    public abstract List<User> getParticipants() throws UnsupportedOperationException;

    /**
     * Checks if the given user is permitted to participate in the contest.
     * A user is permitted to participate in a contest if the contest is public or
     * if it private and the user is a participant.
     *
     * @param user the user to check
     * @return true if the given user is permitted to participate in the contest, false otherwise.
     */
    public boolean permitsUser(User user) {
        return !this.isPrivate() || this.getParticipants().contains(user);
    }

    /**
     * Returns true if the contest has a geolocation, false otherwise.
     *
     * @return true if the contest has a geolocation, false otherwise.
     */
    public abstract boolean hasGeoLocation();

    /**
     * Returns the geolocation of the contest.
     *
     * @return the geolocation of the contest.
     * @throws UnsupportedOperationException if the contest has no geolocation.
     */
    public abstract GeoLocatable getGeoLocation() throws UnsupportedOperationException;

    public abstract String getName();

    /**
     * Returns the animator of the contest.
     * @return the animator of the contest.
     */
    public abstract User getEntertainer();
    public abstract String getTopic();
    public abstract String getRules();
    public abstract Date getStartDate();
    public abstract Date getVotingStartDate();
    public abstract Date getEndDate();
    public ContestStatusEnum getStatus() {
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
    public abstract ProposalRegister getProposalRegister();

    @Override
    public void removeContent(Content<Contest> content) {
        if (content instanceof ContestContent c && this.getProposalRegister() != null)
            this.getProposalRegister().removeProposal(c);
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public ContestOF getOutputFormat() {
        return new ContestOF(
                this.getBaseContestId(),
                this.getName(),
                this.getStatus().toString(),
                this.getEntertainer().getOutputFormat(),
                this.getTopic(),
                this.getRules(),
                this.isPrivate(),
                (this.hasGeoLocation() ? this.getGeoLocation().getOutputFormat() : null),
                this.getStartDate(),
                this.getVotingStartDate(),
                this.getEndDate()
        );
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(
                Parameter.THIS, this,
                Parameter.NAME, this.getName(),
                Parameter.CONTEST_TOPIC, this.getTopic(),
                Parameter.CONTEST_STATUS, this.getStatus());
    }

    @PreRemove
    public void preRemove() {
        if (this.municipality != null) this.municipality.removeContest(this);
        this.notifications.forEach(Notification::setVisualizableNull);
    }
}
