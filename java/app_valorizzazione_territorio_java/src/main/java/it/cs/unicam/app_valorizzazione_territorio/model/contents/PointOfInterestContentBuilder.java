package it.cs.unicam.app_valorizzazione_territorio.model.contents;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
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
    public PointOfInterestContentBuilder(PointOfInterest pointOfInterest) {
        super();
        if (pointOfInterest == null)
            throw new IllegalArgumentException("Point of interest cannot be null");
        this.pointOfInterest = pointOfInterest;
    }


    /**
     * Builds the content.
     * @return the content built
     */
    public ContentBuilder<PointOfInterest, PointOfInterestContent> build() {
        this.result = new PointOfInterestContent(super.getDescription(), this.pointOfInterest, super.getFiles(), super.getUser());
        return this;
    }


    /**
     * Returns the built point of interest associated to the content bulder.
     * @return the built point of interest associated to the content bulder
     */
    public PointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }
}
