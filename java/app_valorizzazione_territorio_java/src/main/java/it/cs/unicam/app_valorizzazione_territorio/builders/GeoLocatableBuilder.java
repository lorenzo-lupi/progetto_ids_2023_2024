package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.DescriptionNotSetException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.TitleNotSetException;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class GeoLocatableBuilder<T extends GeoLocatable> {
    private final Municipality municipality;
    private String title;
    private String description;
    private final List<File> images;

    private final User user;


    public GeoLocatableBuilder(Municipality municipality, User user) {
        if (municipality == null || user == null)
            throw new IllegalArgumentException("User and municipality must not be null");
        this.images = new LinkedList<>();
        this.municipality = municipality;
        this.user = user;
    }

    /**
     * Get the municipality of the CompoundPoint.
     *
     * @return the municipality of the CompoundPoint
     */
    public Municipality getMunicipality() {
        return municipality;
    }

    public User getUser(){
        return this.user;
    }


    /**
     * Set the title of the CompoundPoint.
     *
     * @param title the title of the CompoundPoint
     */
    public GeoLocatableBuilder<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get the title of the CompoundPoint.
     *
     * @return the title of the CompoundPoint
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set the description of the CompoundPoint.
     *
     * @param description the description of the CompoundPoint
     */
    public GeoLocatableBuilder<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get the description of the CompoundPoint.
     *
     * @return the description of the CompoundPoint
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Add an image to the CompoundPoint.
     *
     * @param image the image to add
     */
    public GeoLocatableBuilder<T> addImage(File image) {
        this.images.add(image);
        return this;
    }

    /**
     * Add an image to the CompoundPoint.
     *
     * @param image the images to add
     */
    public GeoLocatableBuilder<T> addImage(List<File> image) {
        this.images.addAll(image);
        return this;
    }

    /**
     * Remove an image from the CompoundPoint.
     *
     * @param image the image to remove
     */
    public GeoLocatableBuilder<T> removeImage(File image) {
        this.images.remove(image);
        return this;
    }

    /**
     * Get the images of the CompoundPoint.
     *
     * @return the images of the CompoundPoint
     */

    public List<File> getImages() {
        return List.copyOf(this.images);
    }


    public  void checkArguments() throws IllegalStateException {
        if (this.getTitle() == null)
            throw new TitleNotSetException("Title must be set before building the CompoundPoint");
        if (this.getDescription() == null)
            throw new DescriptionNotSetException("Description must be set before building the CompoundPoint");
    }

    public abstract GeoLocatableBuilder<T> build() throws IllegalStateException;
    public abstract T obtainResult() throws IllegalStateException;

}
