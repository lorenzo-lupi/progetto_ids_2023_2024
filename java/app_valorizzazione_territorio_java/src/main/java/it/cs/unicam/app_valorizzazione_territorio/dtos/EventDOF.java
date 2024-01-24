package it.cs.unicam.app_valorizzazione_territorio.dtos;

import java.util.Date;

/**
 * This class represents an event Detailed Output Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param municipalitySOF
 * @param classification
 * @param images
 * @param contents
 * @param startDate
 * @param endDate
 * @param ID
 */
public final class EventDOF extends PointOfInterestDOF{
    private final Date startDate;
    private final Date endDate;

    public EventDOF(PointOfInterestDOF pointOfInterestDOF, Date startDate, Date endDate) {
        super(pointOfInterestDOF.name(),
                pointOfInterestDOF.description(),
                pointOfInterestDOF.position(),
                pointOfInterestDOF.municipalitySOF(),
                pointOfInterestDOF.classification(),
                pointOfInterestDOF.images(),
                pointOfInterestDOF.contents(),
                pointOfInterestDOF.ID());
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }
}
