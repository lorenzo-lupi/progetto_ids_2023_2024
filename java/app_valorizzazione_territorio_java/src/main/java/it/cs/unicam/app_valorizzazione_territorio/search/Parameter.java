package it.cs.unicam.app_valorizzazione_territorio.search;

import java.util.HashMap;
import java.util.Map;

public enum Parameter {
    THIS,
    NAME,
    DESCRIPTION,
    POSITION,
    REPRESENTATIVE,
    COMPOUND_POINT_TYPE,
    MUNICIPALITY,
    APPROVAL_STATUS,
    CLASSIFICATION,
    ATTRACTION_TYPE,
    ACTIVITY_TYPE,

    START_DATE,
    END_DATE,
    CONTEST_TOPIC,
    CONTEST_STATUS,
    CONTEST_TYPE,
    VOTE_NUMBER,
    USERNAME,
    EMAIL,
    ID,
    ADD_FILE,
    REMOVE_FILE,
    ADD_POI,
    REMOVE_POI,
    ADD_ROLE,
    CLASS;

    public static final Map<String, Parameter> stringToParameter;

    static {
        stringToParameter = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToParameter.put(parameter.toString(), parameter);
        }
    }
}