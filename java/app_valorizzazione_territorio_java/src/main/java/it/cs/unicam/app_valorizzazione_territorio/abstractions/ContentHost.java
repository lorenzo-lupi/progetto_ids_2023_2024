package it.cs.unicam.app_valorizzazione_territorio.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.model.Content;

import java.util.Collection;

public interface ContentHost<V extends ContentHost<V> & Visualizable> extends Identifiable {
    Collection<Content<V>> getContents();
     default Collection<? extends Content<V>> getApprovedContents(){
        return getContents().stream().filter(Content::isApproved).toList();
     }
}
