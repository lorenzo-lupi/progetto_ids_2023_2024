package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;

import java.io.File;
import java.util.List;

/**
 * This class represents a handler for the insertion of a content.
 * @param <V> the type of the content host
 */
public abstract class ContentInsertionHandler<V extends ContentHost<V> & Visualizable,
        K extends Content<V>> {

    private K content;
    private ContentBuilder<V,
            K> builder;
    /**
     * Constructor for a ContentInsertionHandler.
     * @param builder the builder for the content to be inserted
     */
    public ContentInsertionHandler(ContentBuilder<V, K> builder) {
        if(builder == null)
            throw new IllegalArgumentException("Builder cannot be null");
        this.builder = builder;
    }

    /**
     * Inserts the description of the content.
     * @param description the description of the content
     */
    public void insertDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        builder.buildDescription(description);
    }

    /**
     * Adds a file to the content.
     * @param file the file to added
     */
    public void addFile(File file){
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");
        builder.buildFile(file);
    }
    /**
     * Removes a file from the content.
     * @param file the file to removed
     */
    public void removeFile(File file) {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");
        builder.removeFile(file);
    }

    /**
     * Returns the files added to the content.
     * @return the files added to the content
     */
    public List<File> obtainAddedFiles(){
        return builder.getFiles();
    }

    /**
     * Destroys the content.
     */
    public void destroyContent(){
        builder.reset();
    }


    /**
     * Creates the content.
     */
    public void createContent(){
        this.content = builder.build();
    }

    /**
     * Inserts the content in its Point of interest.
     */
    public abstract void insertContent();

    protected K getContent() {
        return content;
    }


}
