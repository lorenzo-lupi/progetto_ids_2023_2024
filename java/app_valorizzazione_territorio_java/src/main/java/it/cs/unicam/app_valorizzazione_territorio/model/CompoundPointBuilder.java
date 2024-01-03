package it.cs.unicam.app_valorizzazione_territorio.model;

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
    private Collection<GeoLocalizable> geoLocalizables;
    private final List<File> images;

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

    public CompoundPointBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Add a GeoLocalizable to the CompoundPoint.
     * @param geoLocalizable the GeoLocalizable to add

     * @throws TypeNotSetException if the type of the CompoundPoint has not been set
     */
    public CompoundPointBuilder addGeoLocalizable(GeoLocalizable geoLocalizable) throws TypeNotSetException{
        if(this.type == null)
            throw new TypeNotSetException("Type must be set before adding a geo-localizable object");

        if(this.geoLocalizables.isEmpty())
            this.municipality = geoLocalizable.getMunicipality();

        if(!this.municipality.equals(geoLocalizable.getMunicipality())){
            throw new IllegalArgumentException("All geo-localizable objects must belong to the same municipality");
        }

        this.geoLocalizables.add(geoLocalizable);
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
     * @return true if the type of the CompoundPoint is EXPERIENCE, false otherwise
     */
    public boolean isExperience() {
        return this.type == CompoundPointType.EXPERIENCE;
    }

    public boolean isItinerary() {
        return this.type == CompoundPointType.ITINERARY;
    }

    public Collection getGeoLocalizables() {
        return this.geoLocalizables;
    }

    /**
     * Inverts the position of two geo-localizable objects in the list of geo-localizable objects.
     *
     * @throws IllegalStateException if the type of the CompoundPoint is not ITINERARY
     */
    public void invertGeoLocalizables(GeoLocalizable geoLocalizable1, GeoLocalizable geoLocalizable2) throws CompoundPointIsNotItineraryException {
        if(this.type != CompoundPointType.ITINERARY)
            throw new CompoundPointIsNotItineraryException("Type must be set to ITINERARY before inverting geo-localizable objects");
        if(geoLocalizable1 == null || geoLocalizable2 == null)
            throw new IllegalArgumentException("GeoLocalizable 1 and 2 must not be null");

        LinkedList<GeoLocalizable> geoLocalizables = (LinkedList<GeoLocalizable>)this.geoLocalizables;
        int index1 = geoLocalizables.indexOf(geoLocalizable1);
        if(index1 == -1)
            throw new IllegalArgumentException("GeoLocalizable 1 must be in the list of geo-localizable objects");
        int index2 = geoLocalizables.indexOf(geoLocalizable2);
        if(index2 == -1)
            throw new IllegalArgumentException("GeoLocalizable 2 must be in the list of geo-localizable objects");

        Collections.swap(geoLocalizables, index1, index2);
    }

    public void eliminateGeoLocalizable(GeoLocalizable geoLocalizable) {
        if(geoLocalizable == null)
            throw new IllegalArgumentException("GeoLocalizable must not be null");
        if(!this.geoLocalizables.remove(geoLocalizable))
            throw new IllegalArgumentException("GeoLocalizable must be in the list of geo-localizable objects");
    }


    public CompoundPoint build() throws TypeNotSetException, DescriptionNotSetException, NotEnoughGeoLocalizablesException {
        if(this.type == null)
            throw new TypeNotSetException("Type must be set before building the CompoundPoint");
        if(this.description == null)
            throw new DescriptionNotSetException("Description must be set before building the CompoundPoint");
        if(this.geoLocalizables.size() < 2)
            throw new NotEnoughGeoLocalizablesException("At least two geo-localizable objects must be added before building the CompoundPoint");

        return new CompoundPoint(this.type, this.description, this.geoLocalizables, this.images, this.municipality);
    }


}
