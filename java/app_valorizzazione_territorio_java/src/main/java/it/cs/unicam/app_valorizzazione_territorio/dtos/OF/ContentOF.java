package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;
import java.util.List;

/**
 * This class represents a Content Output Format object.
 *
 * @param ID
 * @param representativeFile
 * @param description
 * @param hostName
 * @param files
 * @param approvalStatus
 */
public record ContentOF(
        @JsonView(View.Synthesized.class)   long ID,
        @JsonView(View.Synthesized.class)   File representativeFile,
        @JsonView(View.Detailed.class)      String description,
        @JsonView(View.Detailed.class)      String hostName,
        @JsonView(View.Detailed.class)      List<File> files,
        @JsonView(View.Detailed.class)      ApprovalStatusEnum approvalStatus
)
        implements Identifiable {
    @JsonIgnore
    @Override
    public long getID() {
        return this.ID();
    }
}
