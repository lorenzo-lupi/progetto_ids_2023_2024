package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("Base")
public class ContestBase extends Contest {

    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_user_id")
    private final User animator;
    private String topic;
    private String rules;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date votingStartDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ProposalRegister proposalRegister;

    public ContestBase(String name,
                       User animator,
                       String topic,
                       String rules,
                       Date startDate,
                       Date votingStartDate,
                       Date endDate,
                       Municipality municipality) {

        super(municipality);

        if (name == null || animator == null || topic == null || startDate == null || votingStartDate == null || endDate == null || rules == null )
            throw new IllegalArgumentException("All parameters must not be null");
        if (!checkDates(startDate, votingStartDate, endDate))
            throw new IllegalArgumentException("Dates must be in the correct order");
        if (animator.getAuthorizations(municipality).stream().noneMatch(a -> a.equals(AuthorizationEnum.ENTERTAINER)))
            throw new IllegalArgumentException("User must be animator of the municipality");

        this.name = name;
        this.animator = animator;
        this.topic = topic;
        this.rules = rules;
        this.startDate = startDate;
        this.votingStartDate = votingStartDate;
        this.endDate = endDate;
        this.proposalRegister = new ProposalRegister();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name must not be null");
        this.name = name;
    }

    public User getEntertainer() {
        return animator;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        if (topic == null)
            throw new IllegalArgumentException("Topic must not be null");
        this.topic = topic;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        if (rules == null)
            throw new IllegalArgumentException("Rules must not be null");
        this.rules = rules;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (!(new Date()).before(startDate))
            throw new IllegalArgumentException("Dates must be in the correct order");
        this.startDate = startDate;
    }

    public Date getVotingStartDate() {
        return votingStartDate;
    }

    public void setVotingStartDate(Date votingStartDate) {
        if (!(this.startDate.before(votingStartDate) && (votingStartDate.before(endDate))))
            throw new IllegalArgumentException("Dates must be in the correct order");
        this.votingStartDate = votingStartDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!this.votingStartDate.before(endDate))
            throw new IllegalArgumentException("Dates must be in the correct order");
        this.endDate = endDate;
    }

    @Override
    public ProposalRegister getProposalRequests() {
        return this.proposalRegister;
    }

    private boolean checkDates(Date startDate, Date votingStartDate, Date endDate) {
        return (startDate.before(votingStartDate) && votingStartDate.before(endDate));
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
    public Collection<ContestContent> getContents() {
        return this.proposalRegister
                .getProposals()
                .stream()
                .map(VotedContent::content)
                .toList();
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }
}
