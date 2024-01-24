package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ActivityDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

/**
 * This class represent a point of interest that is a service exercised as a private or public activity.
 * At a given moment, an activity can be in one of the states between "Open" and "Closed" depending on the
 * associated weekly timetable.
 */
public class Activity extends PointOfInterest{

    private final ActivityTypeEnum type;
    private final Timetable timetable;

    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum type) {
        super(title, description, position, municipality);
        this.type = type;
        this.timetable = new Timetable();
    }

    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum type,
                    Timetable timetable) {
        super(title, description, position, municipality);
        this.type = type;
        this.timetable = timetable;
    }

    public ActivityTypeEnum getType() {
        return type;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    @Override
    public ActivityDOF getDetailedFormat() {
        return new ActivityDOF(super.getDetailedFormat(),
                this.getType().toString(),
                this.getTimetable().getRangesList());
    }
}
