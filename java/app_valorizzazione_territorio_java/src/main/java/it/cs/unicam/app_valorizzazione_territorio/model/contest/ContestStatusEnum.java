package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the status of a contest in a given moment.
 */
public enum ContestStatusEnum {
    PLANNED,
    OPEN,
    VOTING,
    CLOSED;

    public static final Map<String, Parameter> stringToContestStatus;

    static {
        stringToContestStatus = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToContestStatus.put(parameter.toString(), parameter);
        }
    }
}
