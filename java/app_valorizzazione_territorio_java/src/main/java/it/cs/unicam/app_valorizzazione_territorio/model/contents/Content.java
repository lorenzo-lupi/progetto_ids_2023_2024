package it.cs.unicam.app_valorizzazione_territorio.model.contents;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Requestable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represent a set of logically coherent information that can contain multimedia files and
 * their textual description. It is associated to a content host object.
 *
 * @param <V> the type of the content host
 */
@MappedSuperclass
@NoArgsConstructor(force = true)
public abstract class Content<V extends ContentHost<V> & Visualizable>  implements Requestable, Searchable {
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private final User user;
    @Getter
    private String description;
    @Getter
    @ElementCollection
    private final List<File> files;
    @Enumerated(EnumType.STRING)
    private ApprovalStatusEnum approvalStatus;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
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

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        this.description = description;
    }

    /**
     * Adds a file to the content.
     *
     * @param file the file to added
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addFile(File file) {
        return this.files.add(file);
    }

    @Transient
    public abstract V getHost();
    /**
     * Removes a file from the content.
     *
     * @param file the file to removed
     * @return true if the file was removed, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue")
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
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        return Map.of(Parameter.DESCRIPTION, toObjectSetter(this::setDescription, String.class),
                Parameter.ADD_FILE, toObjectSetter(this::addFile, File.class),
                Parameter.REMOVE_FILE, toObjectSetter(this::removeFile, File.class));
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
