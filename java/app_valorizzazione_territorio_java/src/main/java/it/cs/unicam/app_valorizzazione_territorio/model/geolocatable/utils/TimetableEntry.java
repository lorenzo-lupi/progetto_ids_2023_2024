package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.DayOfWeek;

@Getter
@Setter
@Entity
@IdClass(TimetableEntry.EntryKey.class)
public class TimetableEntry {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    private Timetable timetable;
    @JsonView(View.Synthesized.class)
    @Id
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @JsonView(View.Synthesized.class)
    @Embedded
    private TimeRange timeRange;

    public TimetableEntry() {}

    public TimetableEntry(DayOfWeek dayOfWeek, TimeRange timeRange, Timetable timetable){
        this.dayOfWeek = dayOfWeek;
        this.timetable = timetable;
        this.timeRange = timeRange;
    }

    public Timetable getTimetable() {
        return this.timetable;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public TimeRange getTimeRange() {
        return this.timeRange;
    }
    @Getter
    @Setter
    protected static class EntryKey implements Serializable {
        private Timetable timetable;
        private DayOfWeek dayOfWeek;

        protected EntryKey(Timetable timetable,
                           DayOfWeek dayOfWeek){
            this.timetable = timetable;
            this.dayOfWeek = dayOfWeek;
        }

        public EntryKey() {}
    }
}
