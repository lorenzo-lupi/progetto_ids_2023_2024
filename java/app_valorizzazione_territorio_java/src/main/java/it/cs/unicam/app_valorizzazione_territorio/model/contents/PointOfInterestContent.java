package it.cs.unicam.app_valorizzazione_territorio.model.contents;


import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * This class represents an indivisible set of logically coherent information that can contain
 * multimedia files and related textual descriptions that can be associated with a geo-localizable point.
 * It can be in the two states Unapproved (pending) and Approved (visible).
 *
 * A PointOfInterestContent is hosted in a PointOfInterest.
 */
@Entity
@DiscriminatorValue("PointOfInterestContent")
@NoArgsConstructor(force = true)
public class PointOfInterestContent extends Content<PointOfInterest>{
    @ManyToOne(fetch = FetchType.EAGER)
    private PointOfInterest poi;
    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files the multimedia files of the content
     * @throws IllegalArgumentException if description, pointOfInterest or files are null
     */
    public PointOfInterestContent(String description, PointOfInterest poi, List<File> files, User user) {
        super(description, files, user);
        if (poi == null)
            throw new IllegalArgumentException("Point of interest cannot be null");

        this.poi = poi;
    }



    @Override
    public PointOfInterest getHost() {
        return this.poi;
    }


    @Override
    public Runnable getDeletionAction() {
        return () -> {this.poi.removeContent(this);
            MunicipalityRepository.getInstance().removeContent(this);};
    }
}
