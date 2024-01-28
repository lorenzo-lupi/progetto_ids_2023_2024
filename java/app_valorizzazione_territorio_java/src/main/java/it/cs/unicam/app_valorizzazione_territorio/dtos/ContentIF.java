package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;

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
    public PointOfInterestContent toContent(PointOfInterest pointOfInterest) {
        return new PointOfInterestContent(this.description, pointOfInterest, this.files);
    }
}
