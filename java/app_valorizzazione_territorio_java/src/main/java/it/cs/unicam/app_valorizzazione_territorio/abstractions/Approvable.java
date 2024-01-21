package it.cs.unicam.app_valorizzazione_territorio.model;

/**
 * This interface represents an object that can be in the two states Unapproved (pending) and Approved (visible).
 */
public interface Approvable {
    /**
     * Returns true if the object is approved, false otherwise.
     */
    boolean isApproved();

    /**
     * Rejects the object.
     */
    void reject();

    /**
     * Approves the object.
     */
    void approve();

    /**
     * Retrieves the current approval status of the object.
     *
     * @return ApprovalStatusENUM representing the current approval status.
     */
    ApprovalStatusENUM getApprovalStatus();
}
