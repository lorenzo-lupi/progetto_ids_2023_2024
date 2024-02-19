package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Objects implementing this class encapsulate the actions to be performed on a modifiable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
public class ModificationCommand<T extends Visualizable & Modifiable> extends RequestCommand<T>{

    List<Pair<Parameter, Object>> modifications;

    /**
     * Constructor for a modification command.
     * @param item the item to be modified
     * @param modifications the modifications to be performed
     */
    public ModificationCommand(T item, List<Pair<Parameter, Object>> modifications) {
        super(item);
        this.modifications = modifications;
    }

    /**
     * Constructor for a modification command.
     * @param item the item to be modified
     * @param parameter the parameter to be modified
     * @param value the new value of the parameter
     */
    public ModificationCommand(T item, Parameter parameter, Object value) {
        super(item);
        this.modifications = new LinkedList<>();
        this.modifications.add(Pair.of(parameter, value));
    }

    public ConfirmationType getConfirmationType() {
        if(modifications.stream().anyMatch(p -> p.getKey() == Parameter.ADD_ROLE && p.getValue() instanceof Role r &&
                r.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)) {
            return ConfirmationType.PROMOTION_TO_ADMIN;
        }
        else return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        modifications
                .forEach(
                        p -> super.getItem().getSettersMapping().get(p.getLeft()).accept(p.getRight())
                );
    }

    @Override
    public void reject() {
        // Do nothing
    }
}
