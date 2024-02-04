package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import java.time.LocalTime;

/**
 * This class represents a time range composed by an opening {@link LocalTime} and a closing {@link LocalTime}.
 *
 * @param openingTime
 * @param closingTime
 */
public record TimeRange(LocalTime openingTime, LocalTime closingTime) {

    /**
     * This method checks if a given {@link LocalTime} is within the range of this time range
     * .
     * @param time the time to check
     * @return true if the given time is within the range of this time range, false otherwise
     */
    public boolean isWithinRange(LocalTime time) {
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
}
