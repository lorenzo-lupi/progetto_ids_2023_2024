package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Contest Request Data Output Format object.
 *
 * @param ID
 * @param user
 * @param contestName
 * @param contest
 * @param date
 * @param content
 */
public record ContestRequestOF(
        @JsonView(View.Synthesized.class)   long ID,
        @JsonView(View.Synthesized.class)   UserOF user,
        @JsonView(View.Synthesized.class)   String contestName,
        @JsonView(View.Synthesized.class)   Date date,
        @JsonView(View.Detailed.class)      ContestOF contest,
        @JsonView(View.Detailed.class)      ContentOF content
)
        implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
