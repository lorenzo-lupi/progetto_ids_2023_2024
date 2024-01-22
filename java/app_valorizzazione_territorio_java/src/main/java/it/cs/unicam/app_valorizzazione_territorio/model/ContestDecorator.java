package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Contest;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ContestDecorator implements Contest{
    private Contest contest;

    public ContestDecorator(Contest contest){
        this.contest = contest;
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
    public boolean isApproved() {
        return contest.isApproved();
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
    public User getAnimator() {
        return this.contest.getAnimator();
    }

    @Override
    public ProposalRequests getProposalRequests() {
        return this.contest.getProposalRequests();
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
    public void reject() {
        this.contest.reject();
    }

    @Override
    public void approve() {
        this.contest.approve();
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return this.contest.getApprovalStatus();
    }

    //TODO
    @Override
    public long getID() {
        return 0;
    }

    @Override
    public Identifiable getSynthesizedFormat() {
        return null;
    }

    @Override
    public Identifiable getDetailedFormat() {
        return null;
    }



    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return this.contest.getParametersMapping();
    }

}
