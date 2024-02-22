package it.cs.unicam.app_valorizzazione_territorio.model.contents;


import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a builder for a content.
 * @param <V> the type of the content host
 * @param <K> the type of the content that will be created and hosted in a V object
 */
public abstract class ContentBuilder<V extends ContentHost<V> & Visualizable
        , K extends Content<V>> {

    protected K result;
    private String description;
    private List<File> files;
    private User user;

    /**
     * Creates a builder for a content associated to the specified geo-localizable point.
     */
    public ContentBuilder() {
        this.files = new ArrayList<>();
        this.description = "";
    }

    public ContentBuilder<V, K> buildUser(User user){
        this.user = user;
        return this;
    }

    /**
     * Adds a file to the content to be built.
     * @param file the file to added
     * @return the builder
     */
    public ContentBuilder<V, K> buildFile(File file) {
        this.files.add(file);
        return this;
    }

    /**
     * Removes a file from the content to be built.
     * @param file the file to removed
     * @return the builder
     */
    public ContentBuilder<V, K> removeFile(File file) {
        this.files.remove(file);
        return this;
    }

    public ContentBuilder<V, K> buildDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Resets the builder.
     * @return the builder
     */
    public ContentBuilder<V, K> reset() {
        this.files = new ArrayList<>();
        this.description = "";
        return this;
    }

    /**
     * Builds the content.
     */
    public abstract ContentBuilder<V, K> build();

    /**
     * Returns the content host.
     * @return the content host
     */
    public K getResult() {
        return result;
    }

    /**
     * Returns the built textual description of the content.
     * @return the built textual description of the content
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the multimedia built files of the content.
     * @return the multimedia built files of the content
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Returns the built geo-localizable point associated to the content bulder.
     * @return the built geo-localizable point associated to the content bulder
     */
    public User getUser() {
        return user;
    }
}