package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.PointOfInterestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.PointOfInterestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a GeoLocatable precisely attributable and traceable in the associated position that
 * represents an attraction, an event or an activity present on the territory. It can be included in a compound point.
 */
public abstract class PointOfInterest extends GeoLocatable {
    private Position position;
    private final List<Content> contents;

    /**
     * Constructor for a PointOfInterest.
     *
     * @param name the name of the PointOfInterest
     * @param coordinates the geographical coordinates of the PointOfInterest
     * @param municipality the municipality of the PointOfInterest
     */
    public PointOfInterest(String name, Position coordinates, Municipality municipality) {
        this(name, "", coordinates, municipality, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a PointOfInterest.
     *
     * @param name the name of the PointOfInterest
     * @param description the textual description of the PointOfInterest
     * @param coordinates the geographical coordinates of the PointOfInterest
     * @param municipality the municipality of the PointOfInterest
     */
    public PointOfInterest(String name, String description, Position coordinates, Municipality municipality) {
        this(name, description, coordinates, municipality, new ArrayList<>(), new ArrayList<>());
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
                                            List<Content> contents) {
        super(name, description, municipality, images);
        this.position = coordinates;
        this.contents = contents;
    }

    public List<Content> getContents() {
        return this.contents;
    }

    /**
     * Returns the contents associated to the geo-locatable object.
     *
     * @param content the contents associated to the geo-locatable object
     * @return true if the contents associated to the geo-locatable object has been added, false otherwise
     */
    public boolean addContent(Content content) {
        return this.contents.add(content);
    }

    /**
     * Removes a content from the geo-locatable object.
     *
     * @param content the content to remove
     * @return true if the content has been removed, false otherwise
     */
    public boolean removeContent(Content content) {
        return this.contents.remove(content);
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public PointOfInterestSOF getSynthesizedFormat() {
        return new PointOfInterestSOF(super.getName(),
                this.getImages().get(0),
                this.getClass().getSimpleName(),
                super.getID());
    }

    @Override
    public PointOfInterestDOF getDetailedFormat() {
        return new PointOfInterestDOF(super.getName(),
                super.getDescription(),
                this.getPosition().toString(),
                super.getMunicipality().getSynthesizedFormat(),
                this.getClass().getSimpleName(),
                super.getImages(),
                this.contents.stream().map(Content::getSynthesizedFormat).toList(),
                super.getID());
    }
}
