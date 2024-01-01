package it.cs.unicam.app_valorizzazione_territorio.model;

import java.io.File;
import java.util.List;

/**
 * This class represents an indivisible set of logically coherent information that can contain
 * multimedia files and related textual descriptions that can be associated with a geo-localizable point.
 * It can be in the two states Unapproved (pending) and Approved (visible).
 */

public class Content implements Approvable {
    private String description;
    private final List<File> files;
    private boolean isApproved;

    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files the multimedia files of the content
     */
    public Content(String description, List<File> files) {
        this.description = description;
        this.files = files;
        this.isApproved = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the multimedia files of the content.
     * @return the multimedia files of the content
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Adds a file to the content.
     * @param file the file to added
     * @return
     */
    public boolean addFile(File file) {
        return this.files.add(file);
    }

    /**
     * Removes a file from the content.
     * @param file the file to removed
     * @return
     */
    public boolean removeFile(File file) {
        return this.files.remove(file);
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
