package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * This class represents a time range composed by an opening {@link LocalTime} and a closing {@link LocalTime}.
 */
@JsonView(View.Synthesized.class)
@Embeddable
@NoArgsConstructor(force = true)
public class TimeRange implements Serializable {

    @JsonFormat(pattern = "hh:mm:ss")
    private final LocalTime openingTime;
    @JsonFormat(pattern = "hh:mm:ss")
    private final LocalTime closingTime;

    /**
     * @param openingTime the opening time
     * @param closingTime the closing time
     */
    public TimeRange(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    /**
     * This method checks if a given {@link LocalTime} is within the range of this time range
     * .
     *
     * @param time the time to check
     * @return true if the given time is within the range of this time range, false otherwise
     */
    public boolean isWithinRange(LocalTime time) {
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
}
