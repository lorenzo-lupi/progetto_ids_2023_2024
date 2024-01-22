package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.*;
import it.cs.unicam.app_valorizzazione_territorio.model.*;

import java.util.*;

/**
 * This class represents a compound point, that is a
 * geographical point composed of two or more geo-localizable objects.
 */
public class CompoundPointBuilder extends GeoLocatableBuilder<CompoundPoint> {
    private final CompoundPointType type;
    private final Collection<PointOfInterest> pointOfInterests;



    /**
     * Constructor for a CompoundPointBuilder.
     *
     * @param compoundPointType the type of the CompoundPoint
     * @param municipality      the municipality of the CompoundPoint
     * @throws IllegalArgumentException if type, municipality or user are null
     */
    public CompoundPointBuilder(CompoundPointType compoundPointType,
                                Municipality municipality) {

        super(municipality);
        if (compoundPointType == null)
            throw new IllegalArgumentException("CompoundPointType must not be nul");

        this.type = compoundPointType;
        this.pointOfInterests = this.type.getCollection();
    }



    /**
     * Add a GeoLocatable to the CompoundPoint.
     *
     * @param pointOfInterest the GeoLocatable to add
     */
    public void addPointOfInterest(PointOfInterest pointOfInterest) throws WrongMunicipalityException {
        if (pointOfInterest == null)
            throw new IllegalArgumentException("GeoLocatable must not be null");
        if (!pointOfInterest.getMunicipality().equals(this.getMunicipality()))
            throw new WrongMunicipalityException("GeoLocatable must have the same municipality of the CompoundPoint");

        this.pointOfInterests.add(pointOfInterest);
    }


    /**
     * Returns the type of the CompoundPoint.
     *
     * @return the type of the CompoundPoint
     */
    public CompoundPointType getType() {
        return this.type;
    }

    /**
     * Returns the title of the CompoundPoint.
     *
     * @return the title of the CompoundPoint
     */
    public Collection<PointOfInterest> getPointOfInterests() {
        return Collections
                .unmodifiableCollection(this.pointOfInterests);
    }

    /**
     * Inverts the position of two geo-localizable objects in the list of geo-localizable objects.
     *
     * @throws IllegalStateException if the type of the CompoundPoint is not ITINERARY
     */
    public void invertPointOfInterest(PointOfInterest pointOfInterest1, PointOfInterest pointOfInterest2) throws CompoundPointIsNotItineraryException {
        if (this.type != CompoundPointType.ITINERARY)
            throw new CompoundPointIsNotItineraryException("Type must be set to ITINERARY before inverting geo-localizable objects");
        if (pointOfInterest1 == null || pointOfInterest2 == null)
            throw new IllegalArgumentException("GeoLocatable 1 and 2 must not be null");
        List<PointOfInterest> pointOfInterests = (List<PointOfInterest>) (this.pointOfInterests);

        int index1 = pointOfInterests.indexOf(pointOfInterest1);
        int index2 = pointOfInterests.indexOf(pointOfInterest2);

        if (index1 == -1 || index2 == -1)
            throw new IllegalArgumentException("GeoLocatable 1 and 2 must be in the list of geo-localizable objects");

        Collections.swap(pointOfInterests, index1, index2);
    }

    /**
     * Eliminates a pointOfInterest object from the collection of PointOfInterest objects.
     *
     * @param pointOfInterest the PointOfInterest object to eliminate
     */
    public void eliminatePointOfInterest(PointOfInterest pointOfInterest) {
        if (pointOfInterest == null)
            throw new IllegalArgumentException("GeoLocatable must not be null");
        if (!this.pointOfInterests.remove(pointOfInterest))
            throw new IllegalArgumentException("GeoLocatable must be in the list of  point of interests");
    }

    /**
     * Returns the CompoundPoint built.
     *
     * @return the CompoundPoint built
     * @throws IllegalStateException if the CompoundPoint is not ready to be built
     */
    public CompoundPoint obtainResult() throws IllegalStateException{
        this.checkArguments();

        return new CompoundPoint(
                this.getTitle(),
                this.getDescription(),
                this.getMunicipality(),
                this.getRepresentative(),
                this.type,
                this.pointOfInterests,
                this.getImages());
    }

    public void checkArguments() throws IllegalStateException {
        super.checkArguments();
        if(this.pointOfInterests.size() < 2)
            throw new NotEnoughGeoLocatablesException("pointOfInterests must contain at least 2 elements");
    }

    private Position getRepresentative() {
        return this.pointOfInterests.stream()
                .map(PointOfInterest::getPosition)
                .reduce(Position::sum)
                .map(position -> position.divide(this.pointOfInterests.size()))
                .orElseThrow();
    }

}