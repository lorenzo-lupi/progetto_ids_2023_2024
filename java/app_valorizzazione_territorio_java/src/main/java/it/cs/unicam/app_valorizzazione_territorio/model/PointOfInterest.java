package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic geo-localizable object, that is a
 * physical or logical point associated with geographical coordinates.
 * It includes fundamental details such as a name,
 * a textual description and a representative multimedia content.
 */
public class PointOfInterest implements Approveable, Searchable {
    private String name;
    private String description;
    private final Position coordinates;
    private final Municipality municipality;
    private final List<File> images;
    private final List<Content> contents;
    private boolean isApproved;

    /**
     * Constructor for a geo-localizable object.
     *
     * @param coordinates the geographical position of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public PointOfInterest(Position coordinates, Municipality municipality) {
        this(null, null, coordinates, municipality, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param coordinates the geographical coordinates of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     */
    public PointOfInterest(String name, String description, Position coordinates, Municipality municipality) {
        this(name, description, coordinates, municipality, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param coordinates the geographical position of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     * @param images the representative multimedia content of the geo-localizable object
     * @param contents the contents associated to the geo-localizable object
     */
    public PointOfInterest(Position coordinates, Municipality municipality, List<File> images, List<Content> contents) {
        this(null, null, coordinates, municipality, images, contents);
    }

    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param coordinates the geographical coordinates of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     * @param images the representative multimedia content of the geo-localizable object
     * @param contents the contents associated to the geo-localizable object
     * @throws IllegalArgumentException if coordinates, description or images are null
     */
    public PointOfInterest(String name, String description, Position coordinates, Municipality municipality, List<File> images, List<Content> contents) {
        if(coordinates == null || municipality == null || images == null || contents == null)
            throw new IllegalArgumentException("Coordinates, municipality, images and contents must not be null");
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
        this.municipality = municipality;
        this.images = images;
        this.contents = contents;
        this.isApproved = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Position getCoordinates() {
        return coordinates;
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
     * Returns the contents associated to the geo-localizable object.
     *
     * @param content the contents associated to the geo-localizable object
     * @return true if the contents associated to the geo-localizable object has been added, false otherwise
     */
    public boolean addContent(Content content) {
        return this.contents.add(content);
    }

    /**
     * Removes a content from the geo-localizable object.
     *
     * @param content the content to remove
     * @return true if the content has been removed, false otherwise
     */
    public boolean removeContent(Content content) {
        return this.contents.remove(content);
    }

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
                Parameter.POSITION, this.coordinates,
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name,
                Parameter.APPROVED, this.isApproved);
    }
}
