package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ActivityDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represent a point of interest that is a service exercised as a private or public activity.
 * At a given moment, an activity can be in one of the states between "Open" and "Closed" depending on the
 * associated weekly timetable.
 */
public class Activity extends PointOfInterest{

    private ActivityTypeEnum type;
    private final Timetable timetable;

    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum type,
                    User user) {
        super(title, description, position, municipality, user);
        this.type = type;
        this.timetable = new Timetable();
    }

    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum type,
                    Timetable timetable,
                    User user) {
        super(title, description, position, municipality, user);
        this.type = type;
        this.timetable = timetable;
    }

    public ActivityTypeEnum getType() {
        return type;
    }

    public void setType(ActivityTypeEnum type) {
        this.type = type;
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

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.ACTIVITY_TYPE, getType());
        return parametersMapping;
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> settersMapping = new HashMap<>(super.getSettersMapping());
        settersMapping.put(Parameter.ACTIVITY_TYPE, toObjectSetter(this::setType, ActivityTypeEnum.class));
        return settersMapping;
    }
}
