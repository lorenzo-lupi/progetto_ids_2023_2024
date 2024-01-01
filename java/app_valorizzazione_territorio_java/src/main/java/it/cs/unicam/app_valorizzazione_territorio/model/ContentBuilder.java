package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for a content.
 */
public class ContentBuilder {
    private List<File> files;
    private String description;

    public ContentBuilder() {
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
        return new Content(this.description, this.files);
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
     * Returns the multimedia built files of the content.
     * @return the multimedia built files of the content
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Returns the built textual description of the content.
     * @return the built textual description of the content
     */
    public String getDescription() {
        return description;
    }
}
