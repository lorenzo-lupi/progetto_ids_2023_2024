package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.utils.TimeRange;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.Date;
import java.util.List;

public record PointOfInterestIF(String name,
                                String description,
                                Position position,
                                long municipalityID,
                                List<String> images,
                                String classification,
                                String type,
                                Date startDate,
                                Date endDate,
                                List<TimeRange> timetable) {
}
