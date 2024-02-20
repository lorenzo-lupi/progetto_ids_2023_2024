package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the timetable of an {@link Activity}.
 * A timetable stores the opening and closing time of an activity for each day of the week.
 */
public class Timetable {
    private final Map<DayOfWeek, TimeRange> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    /**
     * Sets the opening and closing time of an activity for a specific day of the week.
     *
     * @param dayOfWeek the day of the week
     * @param openingTime the opening time
     * @param closingTime the closing time
     */
    public void setOpeningHours(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        timetable.put(dayOfWeek, new TimeRange(openingTime, closingTime));
    }

    /**
     * Checks if the activity is open now.
     *
     * @return true if the activity is open now, false otherwise
     */
    public boolean isOpenNow() {
        return isOpen(LocalDate.now().getDayOfWeek(), LocalTime.now());
    }

    /**
     * Checks if the activity is open on a specific day of the week and time.
     *
     * @param dayOfWeek the day of the week
     * @param  time the time
     * @return true if the activity is open on the specified day of the week and time, false otherwise
     */
    public boolean isOpen(DayOfWeek dayOfWeek, LocalTime time) {
        TimeRange timeRange = timetable.get(dayOfWeek);
        return timeRange != null && timeRange.isWithinRange(time);
    }

    /**
     * Returns a list of {@link TimeRange}s representing the opening and closing time of the timetable
     * for each day of the week.
     * The list returned has a range of size 7, where each element represents a day of the week starting
     * from Monday.
     *
     * @return a list representing the opening and closing time of the timetable for each day of the week
     */
    public List<TimeRange> getRangesList() {
        return Arrays.stream(DayOfWeek.values())
                        .map(this.timetable::get)
                        .toList();
    }
}
