package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.DayOfWeek;


@Getter
@Setter
@Entity
public class TimetableEntry {
    @EmbeddedId
    private KeyEntry keyEntry;
    @Embedded
    private TimeRange timeRange;

    public TimetableEntry() {

    }

    public TimetableEntry(DayOfWeek first, TimeRange second, Timetable timetable){
        this.keyEntry = new KeyEntry(timetable, first);
        this.timeRange = second;
    }
    @Transient
    public DayOfWeek getDayOfWeek() {
        return keyEntry.dayOfWeek;
    }
    @Transient
    public Timetable timetable() {
        return keyEntry.timetable;
    }

    @Embeddable
    class KeyEntry implements Serializable {
        @ManyToOne(fetch = FetchType.EAGER)
        Timetable timetable;
        @Enumerated(EnumType.STRING)
        DayOfWeek dayOfWeek;

        protected KeyEntry(Timetable timetable,
                           DayOfWeek dayOfWeek){
            this.timetable = timetable;
            this.dayOfWeek = dayOfWeek;
        }

        public KeyEntry() {

        }
    }
}
