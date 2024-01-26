package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the types of activities that can be performed in a municipality.
 */
public enum ActivityTypeEnum {
    RESTAURANT,
    BAR,
    HOTEL,
    MALL,
    SUPERMARKET,
    SHOP,
    PHARMACY,
    HOSPITAL,
    MUSEUM,
    LIBRARY,
    CINEMA,
    THEATER,
    CHURCH,
    SPORTS_CENTER,
    AMUSEMENT_PARK,
    OTHER;

    public static final Map<String, Parameter> stringToActivityType;

    static {
        stringToActivityType = new HashMap<>();
        for (Parameter parameter : Parameter.values()) {
            stringToActivityType.put(parameter.toString(), parameter);
        }
    }

}