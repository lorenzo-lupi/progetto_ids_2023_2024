package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.TimeRange;

import java.util.List;

/**
 * This class represents an activity Detailed Output Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param municipalitySOF
 * @param classification
 * @param images
 * @param contents
 * @param type
 * @param timetable
 * @param ID
 */
public final class ActivityDOF extends PointOfInterestDOF{
    private final String type;
    private final List<TimeRange> timetable;

    public ActivityDOF(PointOfInterestDOF pointOfInterestDOF, String type, List<TimeRange> timetable) {
        super(pointOfInterestDOF.name(),
                pointOfInterestDOF.description(),
                pointOfInterestDOF.position(),
                pointOfInterestDOF.municipalitySOF(),
                pointOfInterestDOF.classification(),
                pointOfInterestDOF.images(),
                pointOfInterestDOF.contents(),
                pointOfInterestDOF.ID());
        this.type = type;
        this.timetable = timetable;
    }

    public String type() {
        return type;
    }

    public List<TimeRange> timetable() {
        return timetable;
    }
}
