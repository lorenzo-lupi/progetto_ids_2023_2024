package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a Point of Interest Output Format object.
 */
public class PointOfInterestOF extends GeoLocatableOF {
    @JsonView(View.Synthesized.class)
    private final String classification;
    @JsonView(View.Detailed.class)
    private final List<ContentOF> contents;

    public PointOfInterestOF(String name,
                             String description,
                             Position position,
                             String municipalityName,
                             String classification,
                             String representativeFile,
                             List<String> files,
                             List<ContentOF> contents,
                             long ID) {
        super(name, "PointOfInterest", representativeFile, municipalityName,
                position, description, files, ID);
        this.classification = classification;
        this.contents = contents;
    }

    public String classification() {
        return classification;
    }

    public List<ContentOF> contents() {
        return contents;
    }
}

