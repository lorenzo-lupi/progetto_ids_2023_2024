package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;

import java.io.File;
import java.util.List;

/**
 * This class represents a Content Detailed Output Format object.
 *
 * @param description
 * @param pointOfInterest
 * @param files
 * @param approvalStatus
 * @param ID
 */
public record ContentDOF(String description,
                         String pointOfInterestName,
                         List<File> files,
                         ApprovalStatusEnum approvalStatus,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
