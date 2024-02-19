package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.EventDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents an event, that is a particular point of interest associated
 * with a date of start and a date of end.
 */
public class Event extends PointOfInterest{
    private Date startDate;
    private Date endDate;

    public Event(String title, String description, Position position, Municipality municipality, Date startDate, Date endDate, User user) {
        super(title, description, position, municipality, user);
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
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> settersMapping = new HashMap<>(super.getSettersMapping());
        settersMapping.put(Parameter.START_DATE, toObjectSetter(this::setStartDate, Date.class));
        settersMapping.put(Parameter.END_DATE, toObjectSetter(this::setEndDate, Date.class));
        return settersMapping;
    }

    @Override
    public EventDOF getDetailedFormat() {
        return new EventDOF(super.getDetailedFormat(), this.getStartDate(), this.getEndDate());
    }

}
