package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a pair of a content proposed for a contest and the number of its votes from users.
 *
 * @param content
 * @param votes
 */
public record VotedContent(Content content, int votes) implements Searchable, Visualizable {

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
    public VotedContentSOF getSynthesizedFormat() {
        return new VotedContentSOF(content.getSynthesizedFormat(), votes);
    }

    @Override
    public VotedContentDOF getDetailedFormat() {
        return new VotedContentDOF(content.getDetailedFormat(), votes);
    }
}
