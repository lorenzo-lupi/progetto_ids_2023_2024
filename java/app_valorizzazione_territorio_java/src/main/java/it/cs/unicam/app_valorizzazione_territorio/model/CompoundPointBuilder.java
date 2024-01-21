package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusENUM;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.CompoundPointIsNotItineraryException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.DescriptionNotSetException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.NotEnoughGeoLocalizablesException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.TypeNotSetException;

import java.io.File;
import java.util.*;

/**
 * This class represents a compound point, that is a
 * geographical point composed of two or more geo-localizable objects.
 */
public class CompoundPointBuilder {
    private CompoundPointType type;
    private String description;
    private Municipality municipality;
    private String title;
    private Collection<PointOfInterest> pointOfInterests;
    private final List<File> images;

    private User user;
    public CompoundPointBuilder() {
        this.images = new LinkedList<>();
    }

    public CompoundPointBuilder setTypeExperience() {
        this.type = CompoundPointType.EXPERIENCE;
        return this;
    }

    public CompoundPointBuilder setTypeItinerary() {
        this.type = CompoundPointType.ITINERARY;
        return this;
    }

    public CompoundPointBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public CompoundPointBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Add a GeoLocatable to the CompoundPoint.
     *
     * @param pointOfInterest the GeoLocatable to add
     * @throws TypeNotSetException if the type of the CompoundPoint has not been set
     */
    public CompoundPointBuilder addGeoLocalizable(PointOfInterest pointOfInterest) throws TypeNotSetException {
        if (this.type == null)
            throw new TypeNotSetException("Type must be set before adding a geo-localizable object");

        if (this.pointOfInterests.isEmpty())
            this.municipality = pointOfInterest.getMunicipality();

        if (!this.municipality.equals(pointOfInterest.getMunicipality())) {
            throw new IllegalArgumentException("All geo-localizable objects must belong to the same municipality");
        }

        this.pointOfInterests.add(pointOfInterest);
        return this;
    }

    public CompoundPointBuilder addImage(File image) {
        this.images.add(image);
        return this;
    }

    public boolean isTypeSet() {
        return this.type != null;
    }

    /**
     * Checks if the type of the CompoundPoint is EXPERIENCE.
     *
     * @return true if the type of the CompoundPoint is EXPERIENCE, false otherwise
     */
    public boolean isExperience() {
        return this.type == CompoundPointType.EXPERIENCE;
    }

    public boolean isItinerary() {
        return this.type == CompoundPointType.ITINERARY;
    }

    public Collection getGeoLocalizables() {
        return this.pointOfInterests;
    }

    /**
     * Inverts the position of two geo-localizable objects in the list of geo-localizable objects.
     *
     * @throws IllegalStateException if the type of the CompoundPoint is not ITINERARY
     */
    public void invertGeoLocalizables(PointOfInterest pointOfInterest1, PointOfInterest pointOfInterest2) throws CompoundPointIsNotItineraryException {
        if (this.type != CompoundPointType.ITINERARY)
            throw new CompoundPointIsNotItineraryException("Type must be set to ITINERARY before inverting geo-localizable objects");
        if (pointOfInterest1 == null || pointOfInterest2 == null)
            throw new IllegalArgumentException("GeoLocatable 1 and 2 must not be null");

        LinkedList<PointOfInterest> pointOfInterests = (LinkedList<PointOfInterest>) this.pointOfInterests;
        int index1 = pointOfInterests.indexOf(pointOfInterest1);
        if (index1 == -1)
            throw new IllegalArgumentException("GeoLocatable 1 must be in the list of geo-localizable objects");
        int index2 = pointOfInterests.indexOf(pointOfInterest2);
        if (index2 == -1)
            throw new IllegalArgumentException("GeoLocatable 2 must be in the list of geo-localizable objects");

        Collections.swap(pointOfInterests, index1, index2);
    }

    public void eliminateGeoLocalizable(PointOfInterest pointOfInterest) {
        if (pointOfInterest == null)
            throw new IllegalArgumentException("GeoLocatable must not be null");
        if (!this.pointOfInterests.remove(pointOfInterest))
            throw new IllegalArgumentException("GeoLocatable must be in the list of geo-localizable objects");
    }

    public void setUser(User user) {
        this.user = user;
    }

    //TODO: check user permissions
    public CompoundPoint build() throws TypeNotSetException, DescriptionNotSetException, NotEnoughGeoLocalizablesException {
        if (this.type == null)
            throw new TypeNotSetException("Type must be set before building the CompoundPoint");
        if (this.description == null)
            throw new DescriptionNotSetException("Description must be set before building the CompoundPoint");
        if (this.pointOfInterests.size() < 2)
            throw new NotEnoughGeoLocalizablesException("At least two geo-localizable objects must be added before building the CompoundPoint");

        CompoundPoint compoundPoint = new CompoundPoint(this.pointOfInterests.iterator().next(),
                this.title,
                this.description,
                this.type,
                ApprovalStatusENUM.PENDING,
                this.pointOfInterests);
        this.images.forEach(compoundPoint::addImage);

        return compoundPoint;
    }

    private ApprovalStatusENUM appropriateApprovalStatus(User user) {

        if(Role.isContributorForMunicipality(this.municipality)
                .or(Role.isCuratorForMunicipality(this.municipality)).test(user))
            return ApprovalStatusENUM.APPROVED;
        else
            return ApprovalStatusENUM.PENDING;
    }

}
