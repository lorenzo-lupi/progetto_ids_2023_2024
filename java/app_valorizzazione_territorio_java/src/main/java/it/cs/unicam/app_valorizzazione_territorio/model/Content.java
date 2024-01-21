package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusENUM;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class represents an indivisible set of logically coherent information that can contain
 * multimedia files and related textual descriptions that can be associated with a geo-localizable point.
 * It can be in the two states Unapproved (pending) and Approved (visible).
 */
public class Content implements Approvable, Searchable {
    private String description;
    private final PointOfInterest pointOfInterest;
    private final List<File> files;
    private ApprovalStatusENUM approvalStatus;

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
        this.approvalStatus = ApprovalStatusENUM.PENDING;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        this.description = description;
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
        return this.approvalStatus == ApprovalStatusENUM.APPROVED;
    }

    @Override
    public void reject() {
        this.approvalStatus = ApprovalStatusENUM.REJECTED;
    }

    @Override
    public void approve() {
        this.approvalStatus = ApprovalStatusENUM.APPROVED;
    }

    @Override
    public ApprovalStatusENUM getApprovalStatus() {
        return this.approvalStatus;
    }
}
