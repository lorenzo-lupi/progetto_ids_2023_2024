package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.Content;

import java.io.File;
import java.util.List;

public abstract class ContentInsertionHandler<V extends ContentHost<V> & Visualizable> {

    private Content<V> content;
    private ContentBuilder<V, ? extends Content<V>> builder;
    /**
     * Constructor for a ContentInsertionHandler.
     * @param builder the builder for the content to be inserted
     */
    public ContentInsertionHandler(ContentBuilder<V, ? extends Content<V>> builder) {
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

    protected Content<V> getContent() {
        return content;
    }


}
