package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils.TimeRange;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils.TimetableEntry;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * This class represents an activity Detailed Output Format object.
 */
public final class ActivityOF extends PointOfInterestOF {
    @JsonView(View.Detailed.class)
    private final String type;
    @JsonView(View.Detailed.class)
    private final Map<DayOfWeek, TimeRange> timetable;

    public ActivityOF(PointOfInterestOF pointOfInterestOF,
                      String type,
                      Map<DayOfWeek, TimeRange> timetable) {
        super(pointOfInterestOF.name(),
                pointOfInterestOF.description(),
                pointOfInterestOF.position(),
                pointOfInterestOF.municipalityName(),
                pointOfInterestOF.classification(),
                pointOfInterestOF.representativeFile(),
                pointOfInterestOF.files(),
                pointOfInterestOF.contents(),
                pointOfInterestOF.ID());
        this.type = type;
        this.timetable = timetable;
    }

    public String type() {
        return type;
    }

    public Map<DayOfWeek, TimeRange> timetable() {
        return timetable;
    }
}

