package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic geo-localizable object, that is a physical point associated
 * with geographical coordinates.
 * It includes fundamental details such as a name, a textual description and a representative
 * multimedia content.
 */
public abstract class GeoLocalizable implements Approvable, Searchable, Visualizable {
    private String name;
    private String description;
    private final Municipality municipality;
    private final List<File> images;
    private boolean isApproved;
    private final long ID = MunicipalityRepository.getInstance().getNextGeoLocalizableID();

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public GeoLocalizable (String name, Municipality municipality) {
        this(name, null, municipality, new ArrayList<>());
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public GeoLocalizable (String name, String description, Municipality municipality) {
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
    public GeoLocalizable(String name, String description, Municipality municipality, List<File> images) {
        if(municipality == null || images == null)
            throw new IllegalArgumentException("Coordinates, municipality, images and contents must not be null");
        this.name = name;
        this.description = description;
        this.municipality = municipality;
        this.images = images;
        this.isApproved = false;
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

    public void setName (String name) {
        this.name = name;
    }

    /**
     * Sets the description of the geo-localizable object.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adds a representative multimedia content to the geo-localizable object.
     *
     * @param image the representative multimedia content to add
     * @return true if the representative multimedia content has been added, false otherwise
     */
    public boolean addImage(File image) {
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

    /**
     * Returns the geographical position associated to the geo-localizable object.
     *
     * @return the geographical position associated to the geo-localizable object
     */
    public abstract Position getPosition();

    @Override
    public boolean isApproved() {
        return isApproved;
    }

    @Override
    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.MUNICIPALITY, this.municipality,
                Parameter.POSITION, this.getPosition(),
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name,
                Parameter.APPROVED, this.isApproved);
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
