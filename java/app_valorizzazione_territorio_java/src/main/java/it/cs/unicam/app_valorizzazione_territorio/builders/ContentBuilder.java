package it.cs.unicam.app_valorizzazione_territorio.builders;


import it.cs.unicam.app_valorizzazione_territorio.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ContentBuilder<V extends ContentHost<V> & Visualizable
        , K extends Content<V>> {

    private K contentHost;
    private String description;
    private List<File> files;
    private final User user;

    /**
     * Creates a builder for a content associated to the specified geo-localizable point.
     */
    public ContentBuilder(User user) {
        this.files = new ArrayList<>();
        this.description = "";
        this.user = user;
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

    /**
     * Removes a file from the content to be built.
     * @param description the file to removed
     * @return the builder
     */
    public ContentBuilder<V, K> buildDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the content host.
     * @param contentHost  the host in which the content will be inserted
     * @return the builder
     */
    public ContentBuilder<V, K> buildContentHost(K contentHost) {
        this.contentHost = contentHost;
        return this;
    }

    /**
     * Returns the content host.
     * @return the content host
     */
    public K getContentHost() {
        return contentHost;
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
    /**
     * Builds the content.
     */
    public abstract K build();
}
