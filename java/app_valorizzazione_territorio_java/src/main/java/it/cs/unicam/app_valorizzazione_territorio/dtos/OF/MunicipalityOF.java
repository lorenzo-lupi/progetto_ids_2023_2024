package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a Municipality Detailed Output Format object.
 *
 * @param ID
 * @param name
 * @param representativeFile
 * @param description
 * @param position
 * @param files
 */
public record MunicipalityOF(
        @JsonView(View.Synthesized.class)
        long ID,
        @JsonView(View.Synthesized.class)
        String name,
        @JsonView(View.Synthesized.class)
        String representativeFile,
        @JsonView(View.Detailed.class)
        String description,
        @JsonView(View.Detailed.class)
        Position position,
        @JsonView(View.Detailed.class)
        List<String> files
)
        implements Identifiable, Positionable {
    @JsonIgnore
    @Override
    public long getID() {
        return this.ID();
    }

    @JsonIgnore
    @Override
    public Position getPosition() {
        return this.position();
    }
}
