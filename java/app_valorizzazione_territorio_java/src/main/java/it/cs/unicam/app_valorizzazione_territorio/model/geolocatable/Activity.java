package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ActivityOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils.Timetable;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represent a point of interest that is a service exercised as a private or public activity.
 * At a given moment, an activity can be in one of the states between "Open" and "Closed" depending on the
 * associated weekly timetable.
 */
@Entity
@DiscriminatorValue("Activity")
@NoArgsConstructor(force = true)
public class Activity extends PointOfInterest{

    @Setter
    @Enumerated(EnumType.STRING)
    private ActivityTypeEnum activityType;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Getter
    private final Timetable timetable;
    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum activityType,
                    User user) {
        super(title, description, position, municipality, user);
        this.activityType = activityType;
        this.timetable = new Timetable();
    }

    public Activity(String title,
                    String description,
                    Position position,
                    Municipality municipality,
                    ActivityTypeEnum activityType,
                    Timetable timetable,
                    User user) {
        super(title, description, position, municipality, user);
        this.activityType = activityType;
        this.timetable = timetable;
    }

    public ActivityTypeEnum getType() {
        return activityType;
    }

    public void setType(ActivityTypeEnum activityType) {
        this.activityType  = activityType;
    }

    @Override
    @Transient
    //TODO: remove List.of
    public ActivityOF getOutputFormat() {
        return new ActivityOF(super.getOutputFormat(),
                this.getType().toString(),
               List.of());
    }

    @Override
    @Transient
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.ACTIVITY_TYPE, getType());
        return parametersMapping;
    }

    @Override
    @Transient
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> settersMapping = new HashMap<>(super.getSettersMapping());
        settersMapping.put(Parameter.ACTIVITY_TYPE, toObjectSetter(this::setType, ActivityTypeEnum.class));
        return settersMapping;
    }
}
