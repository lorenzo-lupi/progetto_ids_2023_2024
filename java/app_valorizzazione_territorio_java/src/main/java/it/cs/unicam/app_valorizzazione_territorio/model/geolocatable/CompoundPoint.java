package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.CompoundPointDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class represents a compound point, i.e. a point composed by multiple points of interest objects.
 * It includes fundamental details such as a textual description and a representative multimedia content.
 * It also includes a list of points of interest objects that compose the compound point.
 * It can be of two types: EXPERIENCE or ITINERARY.
 * An EXPERIENCE is a compound point composed by multiple points of interest objects that are not necessarily
 * connected to each other. An ITINERARY is a compound point composed by multiple points of interest objects
 * that are connected to each other.
 */
@Entity
@NoArgsConstructor(force = true)
public class CompoundPoint extends GeoLocatable {

    @Getter
    private CompoundPointTypeEnum type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "compound_point_points_of_interest",
            joinColumns = @JoinColumn(name = "compound_point_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "point_of_interest_ID", referencedColumnName = "ID"))
    private final Collection<PointOfInterest> pointsOfInterest;
    /**
     * Constructor for a compound point.
     *
     * @param type             the type of the compound point
     * @param description      the textual description of the compound point
     * @param pointsOfInterest the points of interest that compose the compound point
     * @throws IllegalArgumentException if type, description, geoLocatables or images are null
     */
    public CompoundPoint(String title,
                         String description,
                         Municipality municipality,
                         CompoundPointTypeEnum type,
                         Collection<PointOfInterest> pointsOfInterest,
                         List<File> images,
                         User user) {

        super(title, description, municipality, images, user);
        checkArguments(type, pointsOfInterest);

        this.type = type;
        this.pointsOfInterest = pointsOfInterest;
        this.setPosition(this.calculatePosition());
    }


    private void checkArguments(CompoundPointTypeEnum type,
                                Collection<PointOfInterest> pointOfInterests) {

        if (type == null)
            throw new IllegalArgumentException("type cannot be null");
        if (pointOfInterests == null)
            throw new IllegalArgumentException("pointOfInterests cannot be null");
        if (pointOfInterests.size() < 2)
            throw new IllegalArgumentException("pointOfInterests must contain at least 2 elements");
    }

    public List<PointOfInterest> getGeoLocalizablesList() {
        return pointsOfInterest.stream().toList();
    }

    public void addPointOfInterest(PointOfInterest pointOfInterest) {
        if (pointOfInterest == null)
            throw new IllegalArgumentException("pointOfInterest cannot be null");
        this.pointsOfInterest.add(pointOfInterest);
        this.setPosition(this.calculatePosition());
    }

    public void removePointOfInterest(PointOfInterest pointOfInterest) {
        if (pointOfInterest == null)
            throw new IllegalArgumentException("pointOfInterest cannot be null");
        this.pointsOfInterest.remove(pointOfInterest);
        this.setPosition(this.calculatePosition());
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parameters
                = new HashMap<>(super.getParametersMapping());
        parameters.put(Parameter.COMPOUND_POINT_TYPE, this.type);
        return parameters;
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> parameters = new HashMap<>(super.getSettersMapping());
        parameters.put(Parameter.ADD_POI, toObjectSetter(this::addPointOfInterest, PointOfInterest.class));
        parameters.put(Parameter.REMOVE_POI, toObjectSetter(this::removePointOfInterest, PointOfInterest.class));
        return parameters;
    }

    @Override
    public CompoundPointDOF getDetailedFormat() {
        return new CompoundPointDOF(this.getID(),
                this.getName(),
                this.getDescription(),
                this.type,
                this.getGeoLocatableSOFList()
                );
    }

    private List<GeoLocatableSOF> getGeoLocatableSOFList() {
        List<GeoLocatableSOF> geoLocatableSOF = new LinkedList<>();
        this.pointsOfInterest.forEach(pointOfInterest -> geoLocatableSOF.add(pointOfInterest.getSynthesizedFormat()));
        return geoLocatableSOF;
    }
    private Position calculatePosition() {
        return this.pointsOfInterest.stream()
                .map(PointOfInterest::getPosition)
                .map(position -> position.divide(this.pointsOfInterest.size()))
                .reduce(Position::sum)
                .orElseThrow();
    }
}
