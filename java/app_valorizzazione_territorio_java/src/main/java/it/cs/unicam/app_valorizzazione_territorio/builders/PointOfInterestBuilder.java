package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Date;
import java.util.LinkedList;

public class PointOfInterestBuilder extends GeoLocatableBuilder<PointOfInterest> {
    private Position position;
    private Class<? extends PointOfInterest> classification;
    private AttractionTypeEnum attractionType;
    private Date startDate;
    private Date endDate;
    private ActivityTypeEnum activityType;
    private Timetable timetable;
    private PointOfInterest pointOfInterest;

    public PointOfInterestBuilder(Municipality municipality) {
        super(municipality);
    }

    public GeoLocatableBuilder<PointOfInterest> setPosition(Position position) {
        if(position == null)
            throw new IllegalArgumentException("Position must not be null");
        if(!this.getMunicipality().getCoordinatesBox().contains(position))
            throw new IllegalArgumentException("Position must be inside the municipality");

        this.position = position;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setClassification(Class<? extends PointOfInterest> classification) {
        this.classification = classification;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setAttractionType(AttractionTypeEnum attractionType) {
        this.attractionType = attractionType;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setActivityType(ActivityTypeEnum activityType) {
        this.activityType = activityType;
        return this;
    }

    public GeoLocatableBuilder<PointOfInterest> setTimetable(Timetable timetable) {
        this.timetable = timetable;
        return this;
    }

    @Override
    public void build() throws IllegalStateException {
        this.checkArguments();
        this.pointOfInterest = switch (this.classification.getSimpleName()) {
                    case "Attraction" -> new Attraction(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.attractionType);
                    case "Event" -> new Event(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.startDate,
                            this.endDate);
                    case "Activity" -> new Activity(this.getTitle(),
                            this.getDescription(),
                            this.position,
                            this.getMunicipality(),
                            this.activityType,
                            this.timetable);
                    default -> throw new IllegalStateException("Unexpected value: " + this.classification);
                };
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
        if (this.classification == Activity.class && this.activityType == null || this.timetable == null)
            throw new IllegalStateException("Activity type and timetable must be set");
    }
}
