package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.VotedContentOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a pair of a content proposed for a contest and the number of its votes from users.
 *
 * @param content
 * @param votes
 */
public record VotedContent(ContestContent content, int votes) implements Searchable, Visualizable {

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping =
                new HashMap<>(content.getParametersMapping());
        parametersMapping.put(Parameter.VOTE_NUMBER, votes);
        return parametersMapping;
    }

    @Override
    public long getID() {
        return content.getID();
    }

    @Override
    public VotedContentOF getOutputFormat() {
        return new VotedContentOF(
                content.getOutputFormat(),
                votes,
                content.getVoters().stream().map(User::getOutputFormat).toList()
        );
    }
}
