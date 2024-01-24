package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.*;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class represents an indivisible set of logically coherent information that can contain
 * multimedia files and related textual descriptions that can be associated with a geo-localizable point.
 * It can be in the two states Unapproved (pending) and Approved (visible).
 */
public class Content implements Approvable, Searchable, Visualizable {
    private String description;
    private final PointOfInterest pointOfInterest;
    private final List<File> files;
    private ApprovalStatusEnum approvalStatus;

    private final long ID = MunicipalityRepository.getInstance().getNextContentID();

    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files the multimedia files of the content
     * @throws IllegalArgumentException if description, pointOfInterest or files are null
     */
    public Content(String description, PointOfInterest pointOfInterest, List<File> files) {
        if (description == null || pointOfInterest == null || files == null)
            throw new IllegalArgumentException("Description, point of interest and files cannot be null");

        this.description = description;
        this.pointOfInterest = pointOfInterest;
        this.files = files;
        this.approvalStatus = ApprovalStatusEnum.PENDING;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        this.description = description;
    }

    public PointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }

    /**
     * Returns the multimedia files of the content.
     * @return the multimedia files of the content
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Adds a file to the content.
     *
     * @param file the file to added
     * @return
     */
    public boolean addFile(File file) {
        return this.files.add(file);
    }

    /**
     * Removes a file from the content.
     *
     * @param file the file to removed
     * @return
     */
    public boolean removeFile(File file) {
        return this.files.remove(file);
    }


    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.DESCRIPTION, this.description,
                Parameter.APPROVAL_STATUS, this.approvalStatus);
    }

    @Override
    public boolean isApproved() {
        return this.approvalStatus == ApprovalStatusEnum.APPROVED;
    }

    @Override
    public void reject() {
        this.approvalStatus = ApprovalStatusEnum.REJECTED;
    }

    @Override
    public void approve() {
        this.approvalStatus = ApprovalStatusEnum.APPROVED;
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return this.approvalStatus;
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public ContentSOF getSynthesizedFormat() {
        return new ContentSOF(this.getFiles().get(0), this.ID);
    }

    @Override
    public ContentDOF getDetailedFormat() {
        return new ContentDOF(this.getDescription(),
                this.getPointOfInterest().getSynthesizedFormat(),
                this.getFiles(),
                this.getApprovalStatus(),
                this.getID());
    }
}
