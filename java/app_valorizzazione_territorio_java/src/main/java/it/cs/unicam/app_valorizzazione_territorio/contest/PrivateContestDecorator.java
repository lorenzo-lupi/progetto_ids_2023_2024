package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateContestDecorator extends ContestDecorator {
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
