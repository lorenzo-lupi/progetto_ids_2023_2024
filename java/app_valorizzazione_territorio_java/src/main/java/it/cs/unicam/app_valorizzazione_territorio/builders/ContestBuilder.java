package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Contest;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.*;
import it.cs.unicam.app_valorizzazione_territorio.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ContestBuilder {
    private boolean isPrivate;
    private List<User> participants;
    private boolean hasGeoLocation;
    private GeoLocatable geoLocatable;
    private String topic;
    private String rules;
    private Date startDate;
    private Date votingStartDate;
    private Date endDate;
    private User animator;
    public ContestBuilder(User animator){
        if (animator == null)
            throw new IllegalArgumentException("Animator must not be null");
        this.animator = animator;
        this.hasGeoLocation = false;
        this.isPrivate = false;
    }
    public void setPrivate() {
        this.isPrivate = true;
        this.participants = new LinkedList<>();
    }
    public void setPublic() {
        this.isPrivate = false;
        this.participants = null;
    }
    public void setGeoLocation(GeoLocatable geoLocatable) {
        this.hasGeoLocation = true;
        this.geoLocatable = geoLocatable;
    }

    public void removeGeoLocation() {
        this.hasGeoLocation = false;
        this.geoLocatable = null;
    }

    public void addParticipant(User participant) throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Participant must not be null");
        if(participant == null)
            throw new IllegalArgumentException("Participant must not be null");

        this.participants.add(participant);
    }

    public List<User> getParticipants() throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Contest is not private");
        return participants;
    }

    public void removeParticipant(User participant) throws IllegalStateException{
        if (!isPrivate)
            throw new ContestNotPrivateException("Contest is not private");
        this.participants.remove(participant);
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean hasGeoLocation() {
        return hasGeoLocation;
    }

    public Contest getResult(){
        checkBaseContestParameters();
        Contest contest = new ContestBase(animator, topic, rules, startDate, votingStartDate, endDate);
        if(isPrivate) {
            contest = new PrivateContestDecorator(contest, participants);
        }
        if(hasGeoLocation){
            contest = new GeoLocatableContestDecorator(contest, geoLocatable);
        }
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

    public void setStartDate(Date startDate) {
        if (startDate == null)
            throw new IllegalArgumentException("Start date must not be null");
        if(startDate.before(new Date()))
            throw new IllegalDateException("Start date must be after current date");
        if(this.votingStartDate != null && startDate.after(this.votingStartDate))
            throw new IllegalDateException("Start date must be before voting start date");
        if(this.endDate != null && startDate.after(this.endDate))
            throw new IllegalDateException("Start date must be before end date");

        this.startDate = startDate;
    }

    public void setVotingStartDate(Date votingStartDate) {
        if (votingStartDate == null)
            throw new IllegalArgumentException("Voting start date must not be null");
        if(votingStartDate.before(new Date()))
            throw new IllegalDateException("Voting start date must be after current date");
        if(this.startDate != null && votingStartDate.before(this.startDate))
            throw new IllegalDateException("Voting start date must be after start date");
        if(this.endDate != null && votingStartDate.after(this.endDate))
            throw new IllegalDateException("Voting start date must be before end date");
        this.votingStartDate = votingStartDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null)
            throw new IllegalArgumentException("End date must not be null");
        if(endDate.before(new Date()))
            throw new IllegalDateException("End date must be after current date");
        if(this.startDate != null && endDate.before(this.startDate))
            throw new IllegalDateException("End date must be after start date");
        if(this.votingStartDate != null && endDate.before(this.votingStartDate))
            throw new IllegalDateException("End date must be after voting start date");
        this.endDate = endDate;
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
