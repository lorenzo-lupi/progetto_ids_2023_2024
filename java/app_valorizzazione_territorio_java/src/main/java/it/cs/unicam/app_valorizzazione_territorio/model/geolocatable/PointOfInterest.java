package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
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
import lombok.Getter;
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

    ///// FOR DELETION PURPOSES /////////
    @Getter
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "pointsOfInterest")
    private List<CompoundPoint> compoundPoints;
    @Getter
    @OneToMany(fetch = FetchType.EAGER)
    private List<Notification> notifications;
    ////////////////////////////////////

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
        this.setPosition(coordinates);
        this.contents = contents;
        this.compoundPoints = new ArrayList<>();
        this.notifications = new ArrayList<>();
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
    public void removeContent(Content<PointOfInterest> content) {
        if (content instanceof PointOfInterestContent c) removeContent(c);
    }

    public void removeContent(PointOfInterestContent content) {
        content.setPoi(null);
        this.contents.remove(content);
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.CLASSIFICATION, this.getClass().getSimpleName());
        return parametersMapping;
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> parametersSetterMapping = new HashMap<>(super.getSettersMapping());
        parametersSetterMapping.put(Parameter.POSITION, toObjectSetter(this::setPosition, Position.class));
        return parametersSetterMapping;
    }

    @Override
    public PointOfInterestOF getOutputFormat() {
        return new PointOfInterestOF(super.getName(),
                super.getDescription(),
                this.getPosition(),
                super.getMunicipality().getName(),
                this.getClass().getSimpleName(),
                super.getFiles().isEmpty() ? null : super.getFiles().get(0).getName(),
                super.getFiles().stream().map(File::getName).toList(),
                this.contents.stream().map(PointOfInterestContent::getOutputFormat).toList(),
                super.getID());
    }

    @Override
    public Collection<PointOfInterestContent> getContents() {
        return this.contents;
    }

    @PreRemove
    public void preRemove() {
        super.preRemove();
        this.contents.forEach(content -> content.setPoi(null));
        new ArrayList<>(compoundPoints).forEach(compoundPoint -> compoundPoint.removePointOfInterest(this));
        this.notifications.forEach(Notification::setVisualizableNull);
    }
}
