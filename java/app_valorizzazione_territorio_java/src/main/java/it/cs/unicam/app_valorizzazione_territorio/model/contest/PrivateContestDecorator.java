package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("Private")
public class PrivateContestDecorator extends ContestDecorator {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "private_contests_participants",
            joinColumns = @JoinColumn(name = "contest_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "user_ID", referencedColumnName = "ID"))
    private final List<User> participants;

    public PrivateContestDecorator(Contest contest,
                                   List<User> participants) {
        super(contest);
        if (participants == null)
            throw new IllegalArgumentException("Participants must not be null");
        this.participants = List.copyOf(participants);
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public List<User> getParticipants() throws UnsupportedOperationException {
        return this.participants;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.THIS, this);
        parametersMapping.put(Parameter.CONTEST_TYPE, "Private");
        return parametersMapping;
    }
}
