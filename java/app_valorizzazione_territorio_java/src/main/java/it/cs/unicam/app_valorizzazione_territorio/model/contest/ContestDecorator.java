package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("Decorator")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "decorator_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(force = true)
public abstract class ContestDecorator extends Contest{

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contest_id", referencedColumnName = "ID")
    private Contest contest;

    public ContestDecorator(Contest contest){
        super(contest.getMunicipality());
        this.contest = contest;
        this.contest.setValid(false);
    }

    @PostPersist
    public void postPersist() {
        this.setBaseContestId(contest.getBaseContestId());
    }

    @Override
    public String getName() {
        return this.contest.getName();
    }

    @Override
    public User getEntertainer() {
        return this.contest.getEntertainer();
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
    public ProposalRegister getProposalRegister() {
        if (this.contest == null) return null;
        return this.contest.getProposalRegister();
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

    @Override
    public Municipality getMunicipality() {
        return this.contest.getMunicipality();
    }

    @PreRemove
    public void preRemove() {
        super.preRemove();
        this.contest = null;
    }
}
