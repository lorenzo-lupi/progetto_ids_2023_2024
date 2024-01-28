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

    public static final Map<String, AttractionTypeEnum> stringToAttractionType;

    static {
        stringToAttractionType = new HashMap<>();
        for (AttractionTypeEnum type : AttractionTypeEnum.values()) {
            stringToAttractionType.put(type.toString(), type);
        }
    }
}
