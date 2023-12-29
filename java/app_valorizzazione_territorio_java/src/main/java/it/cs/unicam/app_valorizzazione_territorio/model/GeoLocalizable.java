package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.List;

/**
 * This class represents a generic geo-localizable object, that is a
 * physical or logical point associated with geographical coordinates.
 * It includes fundamental details such as a name,
 * a textual description and a representative multimedia content.
 */
public class GeoLocalizable {
    private final Position coordinates;
    private String description;
    private List<File> images;

    /**
     * Constructor for a geo-localizable object.
     *
     * @param coordinates
     * @param description
     * @param images
     */
    public GeoLocalizable(Position coordinates, String description, List<File> images) {
        this.coordinates = coordinates;
        this.description = description;
        this.images = images;
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


}
