package it.cs.unicam.app_valorizzazione_territorio.abstractions;

/**
 * Classes implementing this interface provide a unique identifier for their objects in the system.
 */
public interface Identifiable {
    /**
     * Returns the unique identifier of the object.
     *
     * @return the unique identifier of the object
     */
    long getID();
}
