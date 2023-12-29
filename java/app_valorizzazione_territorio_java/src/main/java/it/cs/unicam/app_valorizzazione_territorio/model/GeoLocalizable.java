package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.List;

public class GeoLocalizable {
    private final Position coordinates;
    private String description;
    private List<File> images;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public void addImage(File image) {
        this.images.add(image);
    }


}
