package it.cs.unicam.app_valorizzazione_territorio.abstractions;

/**
 * Classes implementing this interface provide a deletion action to be performed on their objects.
 */
public interface Deletable {

    /**
     * Returns the action to be performed to delete the object from its containers and the system.
     * @return the action to be performed to delete the object
     */
    Runnable getDeletionAction();
}
