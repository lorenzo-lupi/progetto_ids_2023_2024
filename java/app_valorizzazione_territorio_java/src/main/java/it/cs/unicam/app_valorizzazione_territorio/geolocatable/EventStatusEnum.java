package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the status of an event in a given moment.
 */
public enum EventStatusEnum {
    PLANNED,
    OPEN,
    CLOSED;

    public static final Map<String, Parameter> stringToEventState;

    static {
        stringToEventState = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToEventState.put(parameter.toString(), parameter);
        }
    }
}
