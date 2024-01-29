package it.cs.unicam.app_valorizzazione_territorio.model.utils;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.PositionParserException;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class represents utility class for the top-down parsing of a Position.
 */
public class PositionParser {

    /**
     * Parses a string to a Position. Format: Position{latitude=DOUBLE, longitude=DOUBLE}
     * @param string the string to parse
     * @return the parsed Position
     * @throws IllegalArgumentException if the string is null
     * @throws PositionParserException if the string is not in the right format
     */
    public static Position parse(String string){
        if(string == null)
            throw new IllegalArgumentException("String must not be null");

        String[] values = parsePositionWrapper(string.trim()).split(",");
        if(values.length != 2)
            throw new PositionParserException("wrong format");
        return new Position(parseLatitude(values[0].trim()), parseLongitude(values[1].trim()));
    }

    private static String parsePositionWrapper(String string){
        string = string.trim();
        if(!string.startsWith("Position{") && !string.endsWith("}"))
            throw new IllegalArgumentException("wrong format");

        return string.substring("Position{".length(), string.length() - 1);
    }

    private static double parseGeoMeasure(String string, String format) {
        if (!string.startsWith(format))
            throw new PositionParserException("wrong format");

        string = string.substring(format.length());

        if (NumberUtils.isCreatable(string))
            return Double.parseDouble(string);
        else
            throw new PositionParserException("wrong format");
    }

    private static double parseLatitude(String string) {
        return parseGeoMeasure(string, "latitude=");
    }

    private static double parseLongitude(String string) {
        return parseGeoMeasure(string, "longitude=");
    }

}
