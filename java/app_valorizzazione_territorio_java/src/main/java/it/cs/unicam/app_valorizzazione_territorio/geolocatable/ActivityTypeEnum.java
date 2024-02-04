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

    private static final Map<String, ActivityTypeEnum> stringToActivityType;

    static {
        stringToActivityType = new HashMap<>();
        for (ActivityTypeEnum type : ActivityTypeEnum.values()) {
            stringToActivityType.put(type.toString().toLowerCase(), type);
        }
    }

    /**
     * Returns the ActivityTypeEnum corresponding to the given string.
     *
     * @param string the string to be converted
     * @return the corresponding ActivityTypeEnum
     * @throws IllegalArgumentException if the string does not correspond to any ActivityTypeEnum
     */
    public static ActivityTypeEnum fromString(String string) {
        ActivityTypeEnum type = stringToActivityType.get(string.toLowerCase());
        if (type == null)
            throw new IllegalArgumentException("Activity type not found");
        return type;
    }

}
