package it.cs.unicam.app_valorizzazione_territorio.search;

import java.util.Map;
import java.util.Set;

/**
 * Classes implementing this interface can be searched and filtered by a dedicated {@link SearchEngine}.
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
