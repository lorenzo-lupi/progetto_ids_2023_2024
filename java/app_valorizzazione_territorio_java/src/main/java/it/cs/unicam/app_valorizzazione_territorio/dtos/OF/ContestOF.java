package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Contest Detailed Output Format object.
 *
 * @param name
 * @param animator
 * @param topic
 * @param rules
 * @param isPrivate
 * @param contestStatus
 * @param startDate
 * @param votingStartDate
 * @param endDate
 * @param ID
 */
public record ContestOF(
        @JsonView(View.Synthesized.class)   String name,
        @JsonView(View.Detailed.class)      UserOF animator,
        @JsonView(View.Detailed.class)      String topic,
        @JsonView(View.Detailed.class)      String rules,
        @JsonView(View.Detailed.class)      boolean isPrivate,
        @JsonView(View.Detailed.class)      Identifiable host,
        @JsonView(View.Synthesized.class)   String contestStatus,
        @JsonView(View.Detailed.class)      Date startDate,
        @JsonView(View.Detailed.class)      Date votingStartDate,
        @JsonView(View.Detailed.class)      Date endDate,
        @JsonView(View.Synthesized.class)   long ID
)
        implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
