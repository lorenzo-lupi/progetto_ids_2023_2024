package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Municipality Request Detailed Output Format object.
 *
 * @param user
 * @param municipality
 * @param date
 * @param item
 * @param ID
 */
public record MunicipalityRequestOF(
        @JsonView(View.Synthesized.class)   long ID,
        @JsonView(View.Synthesized.class)   UserOF user,
        @JsonView(View.Synthesized.class)   Date date,
        @JsonView(View.Synthesized.class)   String municipalityName,
        @JsonView(View.Detailed.class)      MunicipalityOF municipality,
        @JsonView(View.Detailed.class)      Identifiable item
)
        implements Identifiable {
    @JsonIgnore
    @Override
    public long getID() {
        return this.ID();
    }
}
