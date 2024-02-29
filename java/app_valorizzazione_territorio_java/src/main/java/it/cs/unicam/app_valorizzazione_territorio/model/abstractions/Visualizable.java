package it.cs.unicam.app_valorizzazione_territorio.model.abstractions;

import java.io.Serializable;

/**
 * Classes implementing this interface can provide specific objects for their synthetic
 * and detailed visualization.
 * They also provide a unique identifier in order to be identified in the system from their
 * synthetic or detailed representation.
 */
public interface Visualizable extends Identifiable {

    /**
     * Returns the output representation of the object.
     *
     * @return the output representation of the object
     */
    Identifiable getOutputFormat();

}
