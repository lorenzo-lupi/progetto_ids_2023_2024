package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic geo-localizable object, that is a physical point associated
 * with geographical coordinates.
 * It includes fundamental details such as a name, a textual description and a representative
 * multimedia content.
 */
public abstract class GeoLocatable implements Approvable, Searchable, Visualizable, Identifiable, Positionable {
    private String name;
    private String description;
    private final Municipality municipality;
    private final List<File> images;
    private ApprovalStatusENUM approvalStatus;
    private final long ID = MunicipalityRepository.getInstance().getNextGeoLocalizableID();

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public GeoLocatable(String name, Municipality municipality) {
        this(name, null, municipality, new ArrayList<>());
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public GeoLocatable(String name, String description, Municipality municipality) {
        this(name, description, municipality, new ArrayList<>());
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     * @param images the representative multimedia content of the geo-localizable object
     * @throws IllegalArgumentException if coordinates, description or images are null
     */
    public GeoLocatable(String name, String description, Municipality municipality, List<File> images) {
        if(name == null || description == null)
            throw new IllegalArgumentException("title and description cannot be null");
        if(municipality == null || images == null)
            throw new IllegalArgumentException("Municipality and images must not be null");

        this.name = name;
        this.description = description;
        this.municipality = municipality;
        this.images = images;
        this.approvalStatus = ApprovalStatusENUM.PENDING;
    }

    public String getName() {
        return name;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public List<File> getImages() {
        return images;
    }

    /**
     * Sets the name of the geo-localizable object.
     *
     * @param name the name to set
     * @throws IllegalArgumentException if name is null
     */
    public void setName (String name) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");
        this.name = name;
    }

    /**
     * Sets the description of the geo-localizable object.
     *
     * @param description the description to set
     * @throws IllegalArgumentException if description is null
     */
    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("description cannot be null");
        this.description = description;
    }

    /**
     * Adds a representative multimedia content to the geo-localizable object.
     *
     * @param image the representative multimedia content to add
     * @return true if the representative multimedia content has been added, false otherwise
     * @throws IllegalArgumentException if image is null
     */
    public boolean addImage(File image) {
        if (image == null)
            throw new IllegalArgumentException("image cannot be null");
        return this.images.add(image);
    }

    /**
     * Removes a representative multimedia content from the geo-localizable object.
     *
     * @param image the representative multimedia content to remove
     * @return true if the representative multimedia content has been removed, false otherwise
     */
    public boolean removeImage(File image) {
        return this.images.remove(image);
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

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.MUNICIPALITY, this.municipality,
                Parameter.POSITION, this.getPosition(),
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name,
                Parameter.APPROVAL_STATUS, this.approvalStatus);
    }

    @Override
    public Identifiable getSynthesizedFormat() {
        //TODO
        return null;
    }

    @Override
    public Identifiable getDetailedFormat() {
        //TODO
        return null;
    }

    @Override
    public long getID() {
        return this.ID;
    }
}