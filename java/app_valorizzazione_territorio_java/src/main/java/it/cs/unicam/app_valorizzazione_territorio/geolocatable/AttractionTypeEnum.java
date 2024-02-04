package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

public enum AttractionTypeEnum {
    MONUMENT,
    BUILDING,
    RUINS,
    SQUARE,
    PARK,
    PANORAMIC_POINT,
    STATION,
    OTHER;

    private static final Map<String, AttractionTypeEnum> stringToAttractionType;

    static {
        stringToAttractionType = new HashMap<>();
        for (AttractionTypeEnum type : AttractionTypeEnum.values()) {
            stringToAttractionType.put(type.toString().toLowerCase(), type);
        }
    }

    /**
     * Returns the AttractionTypeEnum corresponding to the given string.
     *
     * @param string the string to be converted
     * @return the corresponding AttractionTypeEnum
     * @throws IllegalArgumentException if the string does not correspond to any AttractionTypeEnum
     */
    public static AttractionTypeEnum fromString(String string) {
        AttractionTypeEnum type = stringToAttractionType.get(string.toLowerCase());
        if(type == null)
            throw new IllegalArgumentException("Attraction type not found");
        return type;
    }
}
