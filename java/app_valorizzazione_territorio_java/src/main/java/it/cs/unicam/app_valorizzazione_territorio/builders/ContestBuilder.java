package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.contest.ContestBase;
import it.cs.unicam.app_valorizzazione_territorio.contest.GeoLocatableContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.contest.PrivateContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.*;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * this class represents a builder for Contest objects
 * @see Contest
 */
public class ContestBuilder {
    private boolean isPrivate;
    private List<User> participants;
    private boolean hasGeoLocation;
    private GeoLocatable geoLocatable;
    private String name;
    private String topic;
    private String rules;
    private Date startDate;
    private Date votingStartDate;
    private Date endDate;
    private final User animator;
    private Contest contest;
    private Municipality municipality;

    /**
     * Creates a new ContestBuilder
     * @param animator the animator of the contest
     */
    public ContestBuilder(User animator, Municipality municipality){
        if (animator == null || municipality == null)
            throw new IllegalArgumentException("Animator and municipality must not be null");
        this.animator = animator;
        this.hasGeoLocation = false;
        this.isPrivate = false;
        this.municipality = municipality;
    }

    /**
     * Sets the contest as private
     * @return the ContestBuilder
     */
    public ContestBuilder setPrivate() {
        this.isPrivate = true;
        this.participants = new LinkedList<>();
        return this;
    }
    /**
     * Sets the contest as public
     * @return the ContestBuilder
     */
    public ContestBuilder setPublic() {
        this.isPrivate = false;
        this.participants = null;
        return this;
    }

    /**
     * Sets the geo-location of the contest
     * @param geoLocatable the geo-location of the contest
     * @return the ContestBuilder
     */
    public ContestBuilder setGeoLocation(GeoLocatable geoLocatable) {
        this.hasGeoLocation = true;
        this.geoLocatable = geoLocatable;
        return this;
    }

    /**
     * Removes the geo-location of the contest
     * @return the ContestBuilder
     */
    public ContestBuilder removeGeoLocation() {
        this.hasGeoLocation = false;
        this.geoLocatable = null;
        return this;
    }

    /**
     * Adds a participant to the contest
     * @param participant the participant to add
     * @return the ContestBuilder
     * @throws ContestNotPrivateException if the contest is not private
     * @throws IllegalArgumentException if the participant is null
     */
    public ContestBuilder addParticipant(User participant) throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Participant must not be null");
        if(participant == null)
            throw new IllegalArgumentException("Participant must not be null");

        this.participants.add(participant);
        return this;
    }

    /**
     * Returns the participants of the contest
     * @return the participants of the contest
     * @throws ContestNotPrivateException if the contest is not private
     */
    public List<User> getParticipants() throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Contest is not private");
        return participants;

    }

    /**
     * Removes a participant from the contest
     * @param participant the participant to remove
     * @return the ContestBuilder
     * @throws ContestNotPrivateException if the contest is not private
     * @throws IllegalArgumentException if the participant is null
     */
    public ContestBuilder removeParticipant(User participant) throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Contest is not private");
        this.participants.remove(participant);
        return this;
    }

    /**
     * Returns true if the contest is private
     * @return true if the contest is private
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Returns true if the contest has a geo-location
     * @return true if the contest has a geo-location
     */
    public boolean hasGeoLocation() {
        return hasGeoLocation;
    }

    public void build(){
        checkBaseContestParameters();
        Contest contest = new ContestBase(name, animator, topic, rules, startDate, votingStartDate, endDate, municipality);
        if(isPrivate) {
            contest = new PrivateContestDecorator(contest, participants);
        }
        if(hasGeoLocation){
            contest = new GeoLocatableContestDecorator(contest, geoLocatable);
        }
        this.contest = contest;
    }

    /**
     * Returns the geo-location of the contest
     * @return the geo-location of the contest
     * @throws UnsupportedOperationException if the contest has no geo-location
     */
    public Contest getResult(){
        if(contest == null)
            this.build();
        return contest;
    }

    private void checkBaseContestParameters(){
        if(topic == null)
            throw new TopicNotSetException("Topic must not be null");
        if(rules == null)
            throw new TitleNotSetException("Rules must not be null");
        if(startDate == null || votingStartDate == null || endDate == null)
            throw new DateNotSetException("Dates must not be null");

    }

    public ContestBuilder setStartDate(Date startDate) {
        if (startDate == null)
            throw new IllegalArgumentException("Start date must not be null");
        if(startDate.before(new Date()))
            throw new IllegalDateException("Start date must be after current date");
        if(this.votingStartDate != null && startDate.after(this.votingStartDate))
            throw new IllegalDateException("Start date must be before voting start date");
        if(this.endDate != null && startDate.after(this.endDate))
            throw new IllegalDateException("Start date must be before end date");

        this.startDate = startDate;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ContestBuilder setVotingStartDate(Date votingStartDate) {
        if (votingStartDate == null)
            throw new IllegalArgumentException("Voting start date must not be null");
        if(votingStartDate.before(new Date()))
            throw new IllegalDateException("Voting start date must be after current date");
        if(this.startDate != null && votingStartDate.before(this.startDate))
            throw new IllegalDateException("Voting start date must be after start date");
        if(this.endDate != null && votingStartDate.after(this.endDate))
            throw new IllegalDateException("Voting start date must be before end date");
        this.votingStartDate = votingStartDate;
        return this;
    }

    public Date getVotingStartDate() {
        return votingStartDate;
    }

    public ContestBuilder setEndDate(Date endDate) {
        if (endDate == null)
            throw new IllegalArgumentException("End date must not be null");
        if(endDate.before(new Date()))
            throw new IllegalDateException("End date must be after current date");
        if(this.startDate != null && endDate.before(this.startDate))
            throw new IllegalDateException("End date must be after start date");
        if(this.votingStartDate != null && endDate.before(this.votingStartDate))
            throw new IllegalDateException("End date must be after voting start date");
        this.endDate = endDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ContestBuilder setName(String name) {
        this.name = name;
        return this;
    }
    public ContestBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }
    public String getTopic() {
        return topic;
    }

    public ContestBuilder setRules(String rules) {
        this.rules = rules;
        return this;
    }

    public String getRules() {
        return rules;
    }
}
