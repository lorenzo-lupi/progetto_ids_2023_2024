package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;

import java.util.List;

/**
 * This class represents a User Data Output Format object.
 *
 * @param ID
 * @param username
 * @param email
 * @param roles
 */
public record UserOF(
        @JsonView(View.Synthesized.class)  long ID,
        @JsonView(View.Synthesized.class)  String username,
        @JsonView(View.Detailed.class)     String email,
        @JsonView(View.Detailed.class)     List<String> roles
)
        implements Identifiable {
    @JsonIgnore
    @Override
    public long getID() {
        return this.ID();
    }
}
