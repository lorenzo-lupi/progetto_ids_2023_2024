package it.cs.unicam.app_valorizzazione_territorio.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;

import java.util.Map;
import java.util.Set;

/**
 * Classes implementing this interface provide a set of descriptive parameters and their associated
 * values in order for their objects to be searched and filtered.
 */
public interface Searchable {

    /**
     * Returns the mapping between search parameters and their values for this object.
     *
     * @return the mapping between parameters and values
     */
    Map<Parameter, Object> getParametersMapping();

    /**
     * Returns the set of search parameters defined for this object.
     *
     * @return the set of search parameters defined for this object
     */
    default Set<Parameter> getParameters() {
        return getParametersMapping().keySet();
    }
}
