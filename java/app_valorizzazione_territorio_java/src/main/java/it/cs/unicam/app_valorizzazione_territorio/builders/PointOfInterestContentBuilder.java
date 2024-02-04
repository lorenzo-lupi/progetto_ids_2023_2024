package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

/**
 * Builder for a content.
 */
public class PointOfInterestContentBuilder extends ContentBuilder<PointOfInterest, PointOfInterestContent>{

    private final PointOfInterest pointOfInterest;


    /**
     * Creates a builder for a content associated to the specified geo-localizable point.
     *
     * @param pointOfInterest the geo-localizable point associated to the content to build
     */
    public PointOfInterestContentBuilder(PointOfInterest pointOfInterest, User user) {
        super(user);
        if (pointOfInterest == null)
            throw new IllegalArgumentException("Point of interest cannot be null");
        this.pointOfInterest = pointOfInterest;
    }


    /**
     * Builds the content.
     * @return the content built
     */
    public PointOfInterestContent build() {
        return new PointOfInterestContent(super.getDescription(), this.pointOfInterest, super.getFiles(), super.getUser());
    }


    /**
     * Returns the built geo-localizable point associated to the content bulder.
     * @return the built geo-localizable point associated to the content bulder
     */
    public PointOfInterest getGeoLocalizable() {
        return pointOfInterest;
    }
}
