package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;

/**
 * Objects implementing this class encapsulate the actions to be performed on an approvable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
public class ApprovalCommand<T extends Approvable & Visualizable> extends RequestCommand<T> {

    public ApprovalCommand(T item) {
        super(item);
    }

    public ConfirmationType getConfirmationType() {
        return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        super.getItem().approve();
    }

    @Override
    public void reject() {
        super.getItem().reject();
    }
}
