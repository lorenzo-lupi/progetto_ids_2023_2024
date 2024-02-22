package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;
import java.util.List;

/**
 * This class represents a Content Output Format object.
 *
 * @param description
 * @param host
 * @param files
 * @param approvalStatus
 * @param ID
 */
public record ContentOF(
        @JsonView(View.Detailed.class)      String description,
        @JsonView(View.Detailed.class)      Identifiable host,
        @JsonView(View.Synthesized.class)   File representativeFile,
        @JsonView(View.Detailed.class)      List<File> files,
        @JsonView(View.Detailed.class)      ApprovalStatusEnum approvalStatus,
        @JsonView(View.Synthesized.class)   long ID
)
        implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
