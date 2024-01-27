package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a content.
 */
public class PointOfInterestContentBuilder {
    private String description;
    private PointOfInterest pointOfInterest;
    private List<File> files;

    /**
     * Creates a builder for a content associated to the specified geo-localizable point.
     *
     * @param pointOfInterest the geo-localizable point associated to the content to build
     */
    public PointOfInterestContentBuilder(PointOfInterest pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
        this.files = new ArrayList<>();
        this.description = "";
    }

    /**
     * Adds a file to the content to be built.
     * @param file the file to added
     * @return the builder
     */
    public PointOfInterestContentBuilder buildFile(File file) {
        this.files.add(file);
        return this;
    }

    /**
     * Removes a file from the content to be built.
     * @param file the file to removed
     * @return the builder
     */
    public PointOfInterestContentBuilder removeFile(File file) {
        this.files.remove(file);
        return this;
    }

    /**
     * Removes a file from the content to be built.
     * @param description the file to removed
     * @return the builder
     */
    public PointOfInterestContentBuilder buildDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Builds the content.
     * @return the content built
     */
    public PointOfInterestContent build() {
        return new PointOfInterestContent(this.description, this.pointOfInterest, this.files);
    }

    /**
     * Resets the builder.
     * @return the builder
     */
    public PointOfInterestContentBuilder reset() {
        this.files = new ArrayList<>();
        this.description = "";
        return this;
    }

    /**
     * Returns the built textual description of the content.
     * @return the built textual description of the content
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the built geo-localizable point associated to the content bulder.
     * @return the built geo-localizable point associated to the content bulder
     */
    public PointOfInterest getGeoLocalizable() {
        return pointOfInterest;
    }
    /**
     * Returns the multimedia built files of the content.
     * @return the multimedia built files of the content
     */
    public List<File> getFiles() {
        return files;
    }
}
