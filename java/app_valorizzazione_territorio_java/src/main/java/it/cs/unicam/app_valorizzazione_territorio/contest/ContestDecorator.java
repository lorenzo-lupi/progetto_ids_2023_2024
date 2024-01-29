package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ContestDecorator implements Contest{
    private Contest contest;

    public ContestDecorator(Contest contest){
        this.contest = contest;
    }

    @Override
    public String getName() {
        return this.contest.getName();
    }

    @Override
    public User getAnimator() {
        return this.contest.getAnimator();
    }

    @Override
    public String getTopic() {
        return this.contest.getTopic();
    }

    @Override
    public String getRules() {
        return this.contest.getRules();
    }

    @Override
    public Date getStartDate() {
        return this.contest.getStartDate();
    }

    @Override
    public Date getVotingStartDate() {
        return this.contest.getVotingStartDate();
    }

    @Override
    public Date getEndDate() {
        return this.contest.getEndDate();
    }

    @Override
    public ContestStatusEnum getStatus() {
        return this.contest.getStatus();
    }

    @Override
    public ProposalRequests getProposalRequests() {
        return this.contest.getProposalRequests();
    }

    @Override
    public boolean hasGeoLocation() {
        return this.contest.hasGeoLocation();
    }

    @Override
    public GeoLocatable getGeoLocation() throws UnsupportedOperationException {
        return this.contest.getGeoLocation();
    }

    @Override
    public boolean isPrivate() {
        return this.contest.isPrivate();
    }

    @Override
    public List<User> getParticipants() throws UnsupportedOperationException {
        return this.contest.getParticipants();
    }
    @Override
    public long getID() {
        return this.contest.getID();
    }

    @Override
    public ContestSOF getSynthesizedFormat() {
        return this.contest.getSynthesizedFormat();
    }

    @Override
    public ContestDOF getDetailedFormat() {
        return this.contest.getDetailedFormat();
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return this.contest.getParametersMapping();
    }

    @Override
    public Collection<? extends Content<Contest>> getContents() {
        return this.contest.getContents();
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }

}
