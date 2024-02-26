package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.List;

/**
 * This class is a DTO used to represent a CompoundPoint in the application.
 */
public final class CompoundPointOF extends GeoLocatableOF {
    @JsonView(View.Synthesized.class)
    private final CompoundPointTypeEnum compoundPointType;
    @JsonView(View.Detailed.class)
    private final List<PointOfInterestOF> pointsOfInterest;

    public CompoundPointOF(String name,
                           String description,
                           Position position,
                           String municipalityName,
                           CompoundPointTypeEnum compoundPointType,
                           File representativeImage,
                           List<File> images,
                           List<PointOfInterestOF> pointsOfInterest,
                           long ID) {
        super(name, "CompoundPoint", representativeImage, municipalityName,
                position, description, images, ID);
        this.compoundPointType = compoundPointType;
        this.pointsOfInterest = pointsOfInterest;
    }

    public CompoundPointTypeEnum type() {
        return compoundPointType;
    }

    public List<PointOfInterestOF> pointsOfInterest() {
        return pointsOfInterest;
    }
}
