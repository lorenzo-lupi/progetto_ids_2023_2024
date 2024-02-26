package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.List;

/**
 * This class represents a VotedContent Output Format object.
 */
public record VotedContentOF(
        @JsonView(View.Synthesized.class) ContentOF content,
        @JsonView(View.Synthesized.class) int votes,
        @JsonView(View.Detailed.class)    List<UserOF> voters
)
        implements Identifiable {
    @JsonIgnore
    @Override
    public long getID() {
        return content.getID();
    }
}
