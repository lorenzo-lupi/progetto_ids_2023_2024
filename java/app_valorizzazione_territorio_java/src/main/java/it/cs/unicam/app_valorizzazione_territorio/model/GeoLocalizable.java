package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class represents a generic geo-localizable object, that is a
 * physical or logical point associated with geographical coordinates.
 * It includes fundamental details such as a name,
 * a textual description and a representative multimedia content.
 */
public class GeoLocalizable implements Approvable, Searchable {
    private final Position coordinates;
    private final Municipality municipality;
    private String name;
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
    public GeoLocalizable(Position coordinates, String description, List<File> images, Municipality municipality) {
        if(coordinates == null || description == null || images == null || municipality == null)
            throw new IllegalArgumentException("Coordinates, description and images must not be null");
        this.coordinates = coordinates;
        this.description = description;
        this.municipality = municipality;
        this.images = images;
        this.isApproved = false;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public Municipality getMunicipality() {
        return municipality;
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

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.MUNICIPALITY, this.municipality,
                Parameter.POSITION, this.coordinates,
                Parameter.DESCRIPTION, this.description,
                Parameter.NAME, this.name,
                Parameter.APPROVED, this.isApproved);
    }
}
