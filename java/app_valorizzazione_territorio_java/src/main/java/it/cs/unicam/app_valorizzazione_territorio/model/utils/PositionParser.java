package it.cs.unicam.app_valorizzazione_territorio.model.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class represents utility class for the top-down parsing of a Position.
 */
public class PositionParser {

    /**
     * Parses a string to a Position
     * @param string the string to parse
     * @return the parsed Position
     */
    public static Position parse(String string){
        if(string == null)
            throw new IllegalArgumentException("String must not be null");

        String[] values = parsePositionWrapper(string).split(",");
        if(values.length != 2)
            throw new IllegalArgumentException("wrong format");
        return new Position(parseLatitude(values[0]), parseLongitude(values[1]));
    }

    private static String parsePositionWrapper(String string){
        string = string.trim();
        if(!string.startsWith("Position{latitude=") && !string.endsWith("}"))
            throw new IllegalArgumentException("wrong format");

        return string.substring("Position{latitude=".length(), string.length() - 1);
    }

    private static double parseGeoMeasure(String string, String format) {
        if (!string.startsWith(format))
            throw new IllegalArgumentException("wrong format");

        string = string.substring(format.length());


        if (NumberUtils.isCreatable(string))
            return Double.parseDouble(string);
        else
            throw new IllegalArgumentException("wrong format");
    }

    private static double parseLatitude(String string) {
        return parseGeoMeasure(string, "latitude=");
    }

    private static double parseLongitude(String string) {
        return parseGeoMeasure(string, "longitude=");
    }

}
