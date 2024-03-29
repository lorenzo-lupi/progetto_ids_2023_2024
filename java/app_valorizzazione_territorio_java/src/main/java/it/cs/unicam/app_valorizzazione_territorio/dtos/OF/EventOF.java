package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;

import java.util.Date;

/**
 * This class represents an event Detailed Output Format object.
 */
public final class EventOF extends PointOfInterestOF {
    @JsonView(View.Detailed.class)
    private final Date startDate;
    @JsonView(View.Detailed.class)
    private final Date endDate;

    public EventOF(PointOfInterestOF pointOfInterestOF, Date startDate, Date endDate) {
        super(pointOfInterestOF.name(),
                pointOfInterestOF.description(),
                pointOfInterestOF.position(),
                pointOfInterestOF.municipalityName(),
                pointOfInterestOF.classification(),
                pointOfInterestOF.representativeFile(),
                pointOfInterestOF.files(),
                pointOfInterestOF.contents(),
                pointOfInterestOF.ID());
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
