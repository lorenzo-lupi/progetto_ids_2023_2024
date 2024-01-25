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
    CONTEST_TOPIC,
    CONTEST_STATUS,
    CONTEST_TYPE,
    VOTE_NUMBER,
    ID;

    public static final Map<String, Parameter> stringToParameter;

    static {
        stringToParameter = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToParameter.put(parameter.toString(), parameter);
        }
    }
}