package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.*;

/**
 * This class represents a compound point, i.e. a point composed by multiple geo-localizable objects.
 * It includes fundamental details such as a textual description and a representative multimedia content.
 * It also includes a list of geo-localizable objects that compose the compound point.
 * It can be of two types: EXPERIENCE or ITINERARY.
 * An EXPERIENCE is a compound point composed by multiple geo-localizable objects that are not necessarily
 * connected to each other. An ITINERARY is a compound point composed by multiple geo-localizable objects
 * that are connected to each other.
 */
public class CompoundPoint extends GeoLocatable {
    private final CompoundPointType type;
    private final Collection<PointOfInterest> pointOfInterests;
    private final PointOfInterest representative;

    /**
     * Constructor for a compound point.
     *
     * @param type             the type of the compound point
     * @param description      the textual description of the compound point
     * @param pointOfInterests the geo-localizable objects that compose the compound point
     * @throws IllegalArgumentException if type, description, geoLocalizables or images are null
     */
    public CompoundPoint(PointOfInterest representative,
                         String title,
                         String description,
                         CompoundPointType type,
                         ApprovalStatusENUM approvalStatus,
                         Collection<PointOfInterest> pointOfInterests) {

        super(representative.getCoordinates(), title, description, approvalStatus);
        checkArguments(type, pointOfInterests, representative);
        this.type = type;
        this.pointOfInterests = pointOfInterests;
        this.representative = representative;
    }

    private void checkArguments(CompoundPointType type,
                                Collection<PointOfInterest> pointOfInterests,
                                PointOfInterest representative) {

        if (type == null)
            throw new IllegalArgumentException("type cannot be null");
        if (pointOfInterests == null)
            throw new IllegalArgumentException("pointOfInterests cannot be null");
        if (representative == null)
            throw new IllegalArgumentException("representative cannot be null");
        if (pointOfInterests.size() < 2)
            throw new IllegalArgumentException("pointOfInterests must contain at least 2 elements");
        if (!pointOfInterests.contains(representative))
            throw new IllegalArgumentException("pointOfInterests doesn't contain the representative point");
    }

    public CompoundPointType getType() {
        return type;
    }


    public List<PointOfInterest> getGeoLocalizablesList() {
        return pointOfInterests.stream().toList();
    }


    @Override
    public Map<Parameter, Object> getParametersMapping() {
        HashMap<Parameter, Object> parameters
                = new HashMap<>(super.getParametersMapping());
        parameters.put(Parameter.COMPOUND_POINT_TYPE, this.type);
        return parameters;
    }
}
