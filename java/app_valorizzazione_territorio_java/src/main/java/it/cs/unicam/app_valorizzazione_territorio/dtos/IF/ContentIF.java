package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.io.File;
import java.util.List;

/**
 * This class represents a Content Input Format object.
 *
 * @param description
 * @param files
 */
public record ContentIF(String description,
                        List<File> files) {
}
