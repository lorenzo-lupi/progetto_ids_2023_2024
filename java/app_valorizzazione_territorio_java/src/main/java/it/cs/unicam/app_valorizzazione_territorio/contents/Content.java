package it.cs.unicam.app_valorizzazione_territorio.contents;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.*;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class represent a set of logically coherent information that can contain multimedia files and
 * their textual description. It is associated to a content host object.
 *
 * @param <V> the type of the content host
 */
public abstract class Content<V extends ContentHost<V> & Visualizable>  implements Approvable, Searchable, Visualizable{
    private String description;
    private final List<File> files;
    private ApprovalStatusEnum approvalStatus;
    private final User user;

    private final long ID = MunicipalityRepository.getInstance().getNextContentID();

    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files the multimedia files of the content
     * @throws IllegalArgumentException if description, pointOfInterest or files are null
     */
    public Content(String description, List<File> files, User user)   {
        if (description == null || files == null)
            throw new IllegalArgumentException("Description, point of interest and files cannot be null");

        this.description = description;
        this.files = files;
        this.user = user;
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

    public abstract V getHost();
    /**
     * Removes a file from the content.
     *
     * @param file the file to removed
     * @return
     */
    public boolean removeFile(File file) {
        return this.files.remove(file);
    }

    /**
     * Returns the user who created the content.
     * @return the user who created the content
     */
    public User getUser() {
        return this.user;
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
        return new ContentSOF(
                this.getFiles().isEmpty() ? null : this.getFiles().get(0),
                this.ID);
    }

    @Override
    public ContentDOF getDetailedFormat() {
        return new ContentDOF(this.getDescription(),
                this.getHost().getSynthesizedFormat(),
                this.getFiles(),
                this.getApprovalStatus(),
                this.getID());
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }
}
