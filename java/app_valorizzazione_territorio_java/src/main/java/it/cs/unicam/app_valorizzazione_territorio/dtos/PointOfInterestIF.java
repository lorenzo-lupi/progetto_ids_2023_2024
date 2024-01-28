package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.Timetable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.io.File;
import java.util.Date;
import java.util.List;

public record PointOfInterestIF(String name,
                                String description,
                                Position position,
                                long municipalityID,
                                List<File> images,
                                String classification,
                                String type,
                                Date startDate,
                                Date endDate,
                                Timetable timetable) {
}
