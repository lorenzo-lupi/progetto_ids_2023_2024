package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.EventDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.util.Date;

/**
 * This class represents an event, that is a particular point of interest associated
 * with a date of start and a date of end.
 */
public class Event extends PointOfInterest{
    private Date startDate;
    private Date endDate;

    public Event(String title, String description, Position position, Municipality municipality, Date startDate, Date endDate) {
        super(title, description, position, municipality);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
    	this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
    	this.endDate = endDate;
    }

    /**
     * This method returns the current status of the event, calculated from the current
     * date and the start and end dates of the event.
     *
     * @return the current status of the event
     */
    public EventStatusEnum getStatus() {
    	Date now = new Date();
    	if (now.before(this.getStartDate())) {
    		return EventStatusEnum.PLANNED;
    	} else if (now.after(this.endDate)) {
    		return EventStatusEnum.OPEN;
    	} else {
    		return EventStatusEnum.CLOSED;
    	}
    }

    @Override
    public EventDOF getDetailedFormat() {
        return new EventDOF(super.getDetailedFormat(), this.getStartDate(), this.getEndDate());
    }

}