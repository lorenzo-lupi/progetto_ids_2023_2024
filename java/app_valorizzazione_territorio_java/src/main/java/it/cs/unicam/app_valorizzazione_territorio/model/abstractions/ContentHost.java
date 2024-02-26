package it.cs.unicam.app_valorizzazione_territorio.model.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;

import java.util.Collection;

/**
 * This interface represents a content host.
 * A content host is a class that contains contents.
 *
 * @param <V> the type of the ContentHost
 */
public interface ContentHost<V extends ContentHost<V> & Visualizable> extends Identifiable {

    /**
     * Returns the contents of the content host.
     * @return the contents of the content host
     */
    Collection<? extends Content<V>> getContents();

    /**
     * Removes a content from the content host.
     * @param content the content to remove
     */
    void removeContent(Content<V> content);

    /**
     * Returns the approved contents of the content host.
     * @return the approved contents of the content host
     */
     default Collection<? extends Content<V>> getApprovedContents(){
        return getContents().stream().filter(Content::isApproved).toList();
     }

    /**
     * Returns the name of the content host.
     * @return the name of the content host
     */
    String getName();


}
