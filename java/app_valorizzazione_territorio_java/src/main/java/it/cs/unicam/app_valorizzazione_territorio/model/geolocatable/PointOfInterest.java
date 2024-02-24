package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.PointOfInterestOF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalCoordinatesException;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * This class represents a GeoLocatable precisely attributable and traceable in the associated position that
 * represents an attraction, an event or an activity present on the territory. It can be included in a compound point.
 */
@Entity
@DiscriminatorValue("PointOfInterest")
@NoArgsConstructor(force = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PointOfInterest extends GeoLocatable implements ContentHost<PointOfInterest> {

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "poi",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private final List<PointOfInterestContent> contents;


    public static final Map<String, Class<? extends PointOfInterest>> stringToClass = Map.of(
            Attraction.class.getSimpleName(), Attraction.class,
            Event.class.getSimpleName(), Event.class,
            Activity.class.getSimpleName(), Activity.class
    );

    /**
     * Constructor for a PointOfInterest.
     *
     * @param name the name of the PointOfInterest
     * @param coordinates the geographical coordinates of the PointOfInterest
     * @param municipality the municipality of the PointOfInterest
     */
    public PointOfInterest(String name, Position coordinates, Municipality municipality, User user) {
        this(name, "", coordinates, municipality, new ArrayList<>(), new ArrayList<>(), user);
    }

    /**
     * Constructor for a PointOfInterest.
     *
     * @param name the name of the PointOfInterest
     * @param description the textual description of the PointOfInterest
     * @param coordinates the geographical coordinates of the PointOfInterest
     * @param municipality the municipality of the PointOfInterest
     */
    public PointOfInterest(String name, String description, Position coordinates, Municipality municipality, User user) {
        this(name, description, coordinates, municipality, new ArrayList<>(), new ArrayList<>(), user);
    }

    /**
     * Constructor for a PointOfInterest.
     *
     * @param name the name of the PointOfInterest
     * @param description the textual description of the PointOfInterest
     * @param coordinates the geographical coordinates of the PointOfInterest
     * @param municipality the municipality of the PointOfInterest
     * @param images the representative multimedia content of the PointOfInterest
     * @param contents the contents associated to the PointOfInterest
     */
    public PointOfInterest(String name, String description,
                                            Position coordinates,
                                            Municipality municipality,
                                            List<File> images,
                                            List<PointOfInterestContent> contents,
                                            User user) {
        super(name, description, municipality, images, user);
        if(!municipality.getCoordinatesBox().contains(coordinates))
            throw new IllegalCoordinatesException("Position must be inside the municipality");
        this.setPosition(coordinates);
        this.contents = contents;
    }

    /**
     * Returns the contents associated to the geo-locatable object.
     *
     * @param content the contents associated to the geo-locatable object
     * @return true if the contents associated to the geo-locatable object has been added, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addContent(PointOfInterestContent content) {
        return this.contents.add(content);
    }

    @Override
    public boolean removeContent(Content<PointOfInterest> content) {
        if (content instanceof PointOfInterestContent c)
            return this.contents.remove(c);
        return false;
    }

    /**
     * Removes a content from the geo-locatable object.
     *
     * @param content the content to remove
     * @return true if the content has been removed, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean removeContent(PointOfInterestContent content) {
        return this.contents.remove(content);
    }

    @Override
    @Transient
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.CLASSIFICATION, this.getClass().getSimpleName());
        return parametersMapping;
    }

    @Override
    @Transient
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> parametersSetterMapping = new HashMap<>(super.getSettersMapping());
        parametersSetterMapping.put(Parameter.POSITION, toObjectSetter(this::setPosition, Position.class));
        return parametersSetterMapping;
    }

    @Override
    @Transient
    public PointOfInterestOF getOutputFormat() {
        return new PointOfInterestOF(super.getName(),
                super.getDescription(),
                this.getPosition(),
                super.getMunicipality().getOutputFormat(),
                this.getClass().getSimpleName(),
                super.getImages().isEmpty() ? null : super.getImages().get(0),
                super.getImages(),
                this.contents.stream().map(PointOfInterestContent::getOutputFormat).toList(),
                super.getID());
    }

    @Override
    @Transient
    public Collection<PointOfInterestContent> getContents() {
        return this.contents;
    }
}
