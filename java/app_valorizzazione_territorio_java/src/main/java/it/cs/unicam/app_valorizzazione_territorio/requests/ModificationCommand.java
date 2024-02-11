package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

/**
 * Objects implementing this class encapsulate the actions to be performed on a modifiable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
public class ModificationCommand<T extends Visualizable & Modifiable> extends RequestCommand<T>{

    private final Parameter parameter;

    private final Object value;

    public ModificationCommand(T item, Parameter parameter, Object value) {
        super(item);
        this.parameter = parameter;
        this.value = value;
    }

    public ConfirmationType getConfirmationType() {
        if (parameter == Parameter.ADD_ROLE && value instanceof Role r &&
                r.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR) {
            return ConfirmationType.PROMOTION_TO_ADMIN;
        }
        else return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        super.getItem().getSettersMapping().get(parameter).accept(value);
    }

    @Override
    public void reject() {
        // Do nothing
    }
}
