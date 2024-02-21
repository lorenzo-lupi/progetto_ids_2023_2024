package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalCoordinatesException;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Date;

public class PointOfInterestBuilder extends GeoLocatableBuilder<PointOfInterest> {
    private Position position;
    private Class<? extends PointOfInterest> classification;
    private AttractionTypeEnum attractionType;
    private Date startDate;
    private Date endDate;
    private ActivityTypeEnum activityType;
    private Timetable timetable;
    private PointOfInterest pointOfInterest;

    public PointOfInterestBuilder(Municipality municipality, User user) {
        super(municipality, user);
    }

    public PointOfInterestBuilder setPosition(Position position) {
        if(position == null)
            throw new IllegalArgumentException("Position must not be null");
        if(!this.getMunicipality().getCoordinatesBox().contains(position))
            throw new IllegalCoordinatesException("Position must be inside the municipality");

        this.position = position;
        return this;
    }

    public PointOfInterestBuilder setClassification(Class<? extends PointOfInterest> classification) {
        this.classification = classification;
        return this;
    }

    public Class getClassification() {
        return this.classification;
    }

    public PointOfInterestBuilder setAttractionType(AttractionTypeEnum attractionType) {
        if(this.classification != Attraction.class)
            throw new IllegalStateException("Classification must be Attraction");

        this.attractionType = attractionType;

        return this;
    }

    public PointOfInterestBuilder setStartDate(Date startDate) {
        if(this.classification != Event.class)
            throw new IllegalStateException("Classification must be Event");

        this.startDate = startDate;
        return this;
    }

    public PointOfInterestBuilder setEndDate(Date endDate) {
        if(this.classification != Event.class)
            throw new IllegalStateException("Classification must be Event");
        this.endDate = endDate;
        return this;
    }

    public PointOfInterestBuilder setActivityType(ActivityTypeEnum activityType) {
        if(this.classification != Activity.class)
            throw new IllegalStateException("Classification must be Activity");
        this.activityType = activityType;
        return this;
    }

    public PointOfInterestBuilder setTimetable(Timetable timetable) {
        if(this.classification != Activity.class)
            throw new IllegalStateException("Classification must be Activity");
        this.timetable = timetable;
        return this;
    }

    @Override
    public PointOfInterestBuilder build() throws IllegalStateException {
        this.checkArguments();
        this.pointOfInterest = switch (this.classification.getSimpleName()) {
                    case "Attraction" -> new Attraction(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.attractionType,
                            this.getUser());
                    case "Event" -> new Event(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.startDate,
                            this.endDate,
                            this.getUser());
                    case "Activity" -> new Activity(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.activityType,
                            this.timetable,
                            this.getUser());
                    default -> throw new IllegalStateException("Unexpected value: " + this.classification);
                };
        return this;
    }

    @Override
    public PointOfInterest obtainResult() throws IllegalStateException {
        if (pointOfInterest == null)
            throw new IllegalStateException("PointOfInterest not built");
        return this.pointOfInterest;
    }

    @Override
    public void checkArguments(){
        super.checkArguments();
        if(this.position == null)
            throw new IllegalStateException("Position not set");
        if (this.classification == null)
            throw new IllegalStateException("Classification not set");
        if (this.classification == Attraction.class && this.attractionType == null)
            throw new IllegalStateException("Attraction type not set");
        if (this.classification == Event.class && (this.startDate == null || this.endDate == null))
            throw new IllegalStateException("Start date and end date must be set");
        if(this.classification == Activity.class && (this.activityType == null || this.timetable == null))
            throw new IllegalStateException("Activity type and timetable must be set");
    }
}
