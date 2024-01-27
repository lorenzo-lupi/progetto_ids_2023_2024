package it.cs.unicam.app_valorizzazione_territorio.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.model.Content;

import java.util.Collection;

public interface ContentHost<V extends Visualizable>{
    Collection<Content<V>> getContests();
}
