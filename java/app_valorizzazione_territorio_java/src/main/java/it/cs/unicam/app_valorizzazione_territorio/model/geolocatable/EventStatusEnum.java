package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the status of an event in a given moment.
 */
public enum EventStatusEnum {
    PLANNED,
    OPEN,
    CLOSED;

    public static final Map<String, EventStatusEnum> stringToEventStatus;

    static {
        stringToEventStatus = new HashMap<>();
        for (EventStatusEnum status : EventStatusEnum.values()) {
            stringToEventStatus.put(status.toString(), status);
        }
    }
}
