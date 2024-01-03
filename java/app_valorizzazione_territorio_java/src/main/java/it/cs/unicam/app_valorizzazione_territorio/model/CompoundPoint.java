package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class represents a compound point, i.e. a point composed by multiple geo-localizable objects.
 * It includes fundamental details such as a textual description and a representative multimedia content.
 * It also includes a list of geo-localizable objects that compose the compound point.
 * It can be of two types: EXPERIENCE or ITINERARY.
 * An EXPERIENCE is a compound point composed by multiple geo-localizable objects that are not necessarily
 * connected to each other. An ITINERARY is a compound point composed by multiple geo-localizable objects
 * that are connected to each other.
 */
public class CompoundPoint implements Searchable, Approvable{
    private final CompoundPointType type;
    private String description;
    private final Collection<GeoLocalizable> geoLocalizables;
    private final List<File> images;
    private boolean approved;

    private final Municipality municipality;
    private GeoLocalizable representative;

    /**
     * Constructor for a compound point.
     * @param type the type of the compound point
     * @param description the textual description of the compound point
     * @param geoLocalizables the geo-localizable objects that compose the compound point
     * @param images the representative multimedia content of the compound point
     * @throws IllegalArgumentException if type, description, geoLocalizables or images are null
     */
    public CompoundPoint(CompoundPointType type,
                         String description,
                         Collection<GeoLocalizable> geoLocalizables,
                         List<File> images,
                         Municipality municipality) {
        if(type == null || description == null || geoLocalizables == null || images == null || municipality == null)
            throw new IllegalArgumentException("Type, description, geoLocalizables and images must not be null");
        this.type = type;
        this.description = description;
        this.geoLocalizables = geoLocalizables;
        this.images = images;
        this.municipality = municipality;
        this.approved = false;
        setRepresentative(geoLocalizables);
    }

    private void setRepresentative(Collection<GeoLocalizable> geoLocalizables) {
        geoLocalizables.stream()
                .findAny()
                .ifPresentOrElse((geoLocalizable ->  this.representative = geoLocalizable),
                (() -> {throw new IllegalArgumentException("GeoLocalizables must not be empty");}));

    }
    public CompoundPointType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<GeoLocalizable> getGeoLocalizablesList() {
        return geoLocalizables.stream().toList();
    }

    public void setDescription(String description) {
        if(description != null)
            this.description = description;
    }

    public void addFile(File file) {
        if(file != null)
            this.images.add(file);
    }

    @Override
    public boolean isApproved() {
        return this.approved;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(
                Parameter.COMPUND_TYPE, this.type,
                Parameter.DESCRIPTION, this.description,
                Parameter.MUNICIPALITY, this.municipality,
                Parameter.REPRESENTATIVE, this.representative,
                Parameter.POSITION, this.geoLocalizables
        );
    }
}
