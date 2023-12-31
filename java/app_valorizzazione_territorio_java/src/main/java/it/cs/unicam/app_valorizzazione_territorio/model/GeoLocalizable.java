package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a generic geo-localizable object, that is a
 * physical or logical point associated with geographical coordinates.
 * It includes fundamental details such as a name,
 * a textual description and a representative multimedia content.
 */
public class GeoLocalizable implements Approvable {
    private final Position coordinates;
    private String description;
    private final List<File> images;
    private boolean isApproved;

    /**
     * Constructor for a geo-localizable object.
     *
     * @param coordinates the geographical coordinates of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param images the representative multimedia content of the geo-localizable object
     * @throws IllegalArgumentException if coordinates, description or images are null
     */
    public GeoLocalizable(Position coordinates, String description, List<File> images) {
        if(coordinates == null || description == null || images == null)
            throw new IllegalArgumentException("Coordinates, description and images must not be null");
        this.coordinates = coordinates;
        this.description = description;
        this.images = images;
        this.isApproved = false;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

    public List<File> getImages() {
        return images;
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
     * Sets the images of the geo-localizable object.
     * @param image the image to set
     */
    public void addImage(File image) {
        this.images.add(image);
    }


    @Override
    public boolean isApproved() {
        return isApproved;
    }

    @Override
    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
