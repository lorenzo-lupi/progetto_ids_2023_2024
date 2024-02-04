package it.cs.unicam.app_valorizzazione_territorio.contest;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;

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
     * Returns the approved contents of the content host.
     * @return the approved contents of the content host
     */
     default Collection<? extends Content<V>> getApprovedContents(){
        return getContents().stream().filter(Content::isApproved).toList();
     }
}
