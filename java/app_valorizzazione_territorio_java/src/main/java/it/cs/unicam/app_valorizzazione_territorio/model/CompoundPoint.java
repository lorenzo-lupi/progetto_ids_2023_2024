package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CompoundPoint implements Searchable, Approvable{
    private final CompoundPointType type;
    private String description;
    private final Collection<GeoLocalizable> geoLocalizables;
    private final List<File> images;

    private Municipality municipality;
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
                         List<File> images) {
        if(type == null || description == null || geoLocalizables == null || images == null)
            throw new IllegalArgumentException("Type, description, geoLocalizables and images must not be null");
        this.type = type;
        this.description = description;
        this.geoLocalizables = geoLocalizables;
        this.images = images;
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

    //TODO
    @Override
    public boolean isApproved() {
        return false;
    }

    //TODO
    @Override
    public void setApproved(boolean approved) {

    }

    //TODO
    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return null;
    }
}
