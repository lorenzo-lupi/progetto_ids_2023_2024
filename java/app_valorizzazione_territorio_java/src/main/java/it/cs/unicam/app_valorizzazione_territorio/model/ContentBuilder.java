package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a content.
 */
public class ContentBuilder {
    private String description;
    private GeoLocalizable geoLocalizable;
    private List<File> files;

    /**
     * Creates a builder for a content associated to the specified geo-localizable point.
     *
     * @param geoLocalizable the geo-localizable point associated to the content to build
     */
    public ContentBuilder(GeoLocalizable geoLocalizable) {
        this.geoLocalizable = geoLocalizable;
        this.files = new ArrayList<>();
        this.description = "";
    }

    /**
     * Adds a file to the content to be built.
     * @param file the file to added
     * @return the builder
     */
    public ContentBuilder buildFile(File file) {
        this.files.add(file);
        return this;
    }

    /**
     * Removes a file from the content to be built.
     * @param description the file to removed
     * @return the builder
     */
    public ContentBuilder buildDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Builds the content.
     * @return the content built
     */
    public Content build() {
        return new Content(this.description, this.geoLocalizable, this.files);
    }

    /**
     * Resets the builder.
     * @return the builder
     */
    public ContentBuilder reset() {
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
    public GeoLocalizable getGeoLocalizable() {
        return geoLocalizable;
    }
    /**
     * Returns the multimedia built files of the content.
     * @return the multimedia built files of the content
     */
    public List<File> getFiles() {
        return files;
    }
}
