package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Deletable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;

/**
 * Objects implementing this class encapsulate the actions to be performed on a deletable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
public class DeletionCommand<T extends Visualizable & Deletable> extends RequestCommand<T> {

    public DeletionCommand(T item) {
        super(item);
    }

    public ConfirmationType getConfirmationType() {
        if (this.getItem() instanceof PointOfInterest) {
            return ConfirmationType.CASCADE_DELETION;
        }
        else return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        super.getItem().getDeletionAction().run();
    }

    @Override
    public void reject() {
        // Do nothing
    }
}
