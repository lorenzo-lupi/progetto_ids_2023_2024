package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils.TimetableEntry;

import java.util.List;

/**
 * This class represents an activity Detailed Output Format object.
 */
@JsonView(View.Detailed.class)
public final class ActivityOF extends PointOfInterestOF {
    private final String type;
    private final List<TimetableEntry> timetable;

    public ActivityOF(PointOfInterestOF pointOfInterestOF,
                      String type,
                      List<TimetableEntry> timetable) {
        super(pointOfInterestOF.name(),
                pointOfInterestOF.description(),
                pointOfInterestOF.position(),
                pointOfInterestOF.municipality(),
                pointOfInterestOF.classification(),
                pointOfInterestOF.representativeImage(),
                pointOfInterestOF.images(),
                pointOfInterestOF.contents(),
                pointOfInterestOF.ID());
        this.type = type;
        this.timetable = timetable;
    }

    public String type() {
        return type;
    }

    public List<TimetableEntry> timetable() {
        return timetable;
    }
}

