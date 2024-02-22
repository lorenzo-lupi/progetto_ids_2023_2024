package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Activity;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the timetable of an {@link Activity}.
 * A timetable stores the opening and closing time of an activity for each day of the week.
 */
@Entity
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "timetable")
    private final List<TimetableEntry> timetableEntries;

    public Timetable() {
        this.timetableEntries = new LinkedList<>();
    }

    public Timetable(List<TimeRange> timeRanges) {
        this.timetableEntries = new LinkedList<>();
        for (int i = 0; i < timeRanges.size(); i++) {
            this.timetableEntries.add(new TimetableEntry(DayOfWeek.of(i + 1), timeRanges.get(i), this));
        }
    }

    /**
     * Sets the opening and closing time of an activity for a specific day of the week.
     *
     * @param dayOfWeek   the day of the week
     * @param openingTime the opening time
     * @param closingTime the closing time
     */
    public void setOpeningHours(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        if (this.timetableEntries.stream().noneMatch(entry -> entry.getDayOfWeek().equals(dayOfWeek))) {
            this.timetableEntries.add(new TimetableEntry(dayOfWeek, new TimeRange(openingTime, closingTime), this));
        }
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
     * @param time      the time
     * @return true if the activity is open on the specified day of the week and time, false otherwise
     */
    public boolean isOpen(DayOfWeek dayOfWeek, LocalTime time) {

        return timetableEntries.stream().anyMatch(p -> p.getDayOfWeek().equals(dayOfWeek))
                &&
                timetableEntries.stream().filter(p -> p.getDayOfWeek().equals(dayOfWeek))
                        .findFirst()
                        .get()
                        .getTimeRange()
                        .isWithinRange(time);
    }

    /**
     * Returns a list of {@link TimeRange}s representing the opening and closing time of the timetable
     * for each day of the week.
     * The list returned has a range of size 7, where each element represents a day of the week starting
     * from Monday.
     *
     * @return a list representing the opening and closing time of the timetable for each day of the week
     */
    @Transient
    public List<TimetableEntry> getRangesList() {
        return this.timetableEntries;
    }

}