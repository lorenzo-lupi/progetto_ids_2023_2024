package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a GeoLocatable Output Format object.
 */
public class GeoLocatableOF implements Identifiable, Positionable {
    @JsonView(View.Synthesized.class)
    private final String name;
    @JsonView(View.Synthesized.class)
    private final String geoLocatableType;
    @JsonView(View.Synthesized.class)
    private final File representativeImage;
    @JsonView(View.Detailed.class)
    private final MunicipalityOF municipality;
    @JsonView(View.Detailed.class)
    private final Position position;
    @JsonView(View.Detailed.class)
    private final String description;
    @JsonView(View.Detailed.class)
    private final List<File> images;
    @JsonView(View.Synthesized.class)
    private final long ID;

    public GeoLocatableOF(String name,
                          String geoLocatableType,
                          File representativeImage,
                          MunicipalityOF municipality,
                          Position position,
                          String description,
                          List<File> images,
                          long ID) {
        this.name = name;
        this.description = description;
        this.geoLocatableType = geoLocatableType;
        this.municipality = municipality;
        this.position = position;
        this.representativeImage = representativeImage;
        this.images = images;
        this.ID = ID;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public String geoLocatableType() {
        return this.geoLocatableType;
    }

    public MunicipalityOF municipality() {
        return this.municipality;
    }

    public Position position() {
        return this.position;
    }

    public File representativeImage() {
        return this.representativeImage;
    }

    public List<File> images() {
        return this.images;
    }

    public long ID() {
        return this.ID;
    }

    @Override
    public long getID() {
        return this.ID();
    }

    @Override
    public Position getPosition() {
        return this.position();
    }
}
