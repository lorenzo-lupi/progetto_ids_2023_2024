package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;

/**
 * Objects implementing this class encapsulate the actions to be performed on an approvable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
public class ApprovalCommand<T extends Approvable & Visualizable> extends RequestCommand<T> {

    public ApprovalCommand(T item) {
        super(item);
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
