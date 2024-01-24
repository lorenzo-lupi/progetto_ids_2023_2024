package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the authorizations that a user can have in a municipality.
 */
public enum RoleTypeEnum {
    CONTRIBUTOR,
    ENTERTAINER,
    CURATOR,
    ADMINISTRATOR;

    public static final Map<String, Parameter> stringToRoleType;

    static {
        stringToRoleType = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToRoleType.put(parameter.toString(), parameter);
        }
    }
}
