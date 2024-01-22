package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Contest;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContestBase implements Contest {
    private User animator;
    private ProposalRequests proposalRequests;



    private String topic;


    private String rules;



    private Date startDate;
    private Date votingStartDate;
    private Date endDate;

    private ApprovalStatusEnum approvalStatus;

    public ContestBase(User animator,
                       String topic,
                       String rules,
                       Date startDate,
                       Date votingStartDate,
                       Date endDate) {

        if (animator == null || topic == null || startDate == null || votingStartDate == null || endDate == null || rules == null)
            throw new IllegalArgumentException("All parameters must not be null");
        if (!checkDates(startDate, votingStartDate, endDate))
            throw new IllegalArgumentException("Dates must be in the correct order");
        this.proposalRequests = new ProposalRequests();
        this.animator = animator;
        this.topic = topic;
        this.rules = rules;
        this.startDate = startDate;
        this.votingStartDate = votingStartDate;
        this.endDate = endDate;
        this.approvalStatus = ApprovalStatusEnum.PENDING;

    }

    private boolean checkDates(Date startDate, Date votingStartDate, Date endDate) {
        return (new Date()).before(startDate)
                &&
                startDate.before(votingStartDate)
                &&
                votingStartDate.before(endDate);
    }


    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getRules() {
        return rules;
    }
    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getVotingStartDate() {
        return votingStartDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public boolean isApproved() {
        return approvalStatus == ApprovalStatusEnum.APPROVED;
    }

    @Override
    public void reject() {
        this.approvalStatus = ApprovalStatusEnum.REJECTED;
    }

    @Override
    public void approve() {
        this.approvalStatus = ApprovalStatusEnum.APPROVED;
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return this.approvalStatus;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public List<User> getParticipants() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("ContestBase has no participants");
    }

    @Override
    public boolean hasGeoLocation() {
        return false;
    }

    @Override
    public GeoLocatable getGeoLocation() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("ContestBase has no geo location");
    }

    @Override
    public User getAnimator() {
        return this.animator;
    }

    @Override
    public ProposalRequests getProposalRequests() {
        return this.proposalRequests;
    }

    //TODO
    @Override
    public long getID() {
        return 0;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(
                Parameter.CONTEST_TOPIC, this.topic,
                Parameter.CONTEST_STATUS, this.approvalStatus);
    }

    //TODO
    @Override
    public Identifiable getSynthesizedFormat() {
        return null;
    }

    //TODO
    @Override
    public Identifiable getDetailedFormat() {
        return null;
    }
}
