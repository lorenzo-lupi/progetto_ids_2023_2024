package it.cs.unicam.app_valorizzazione_territorio.abstractions;

/**
 * This interface represents an object that can be in the two states Unapproved (pending) and Approved (visible).
 */
public interface Approvable {
    /**
     * Returns true if the object is approved, false otherwise.
     */
    boolean isApproved();

    /**
     * Sets the object as approved or not.
     *
     * @param approved
     */
    void setApproved(boolean approved);
}
