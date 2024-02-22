package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a Municipality Detailed Output Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param files
 * @param ID
 */
public record MunicipalityOF(
        @JsonView(View.Synthesized.class)  String name,
        @JsonView(View.Detailed.class)     String description,
        @JsonView(View.Detailed.class)     Position position,
        @JsonView(View.Synthesized.class)  File representativeFile,
        @JsonView(View.Detailed.class)     List<File> files,
        @JsonView(View.Synthesized.class)  long ID
)
        implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
