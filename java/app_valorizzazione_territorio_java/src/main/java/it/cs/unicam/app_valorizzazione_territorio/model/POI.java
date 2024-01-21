package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class POI extends GeoLocalizable{
    private final List<Content> contents;
    private final Position position;

    /**
     * Constructor for a POI.
     *
     * @param name the name of the POI
     * @param coordinates the geographical coordinates of the POI
     * @param municipality the municipality of the POI
     */
    public POI(String name, Position coordinates, Municipality municipality) {
        this(name, "", coordinates, municipality, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a POI.
     *
     * @param name the name of the POI
     * @param description the textual description of the POI
     * @param coordinates the geographical coordinates of the POI
     * @param municipality the municipality of the POI
     */
    public POI(String name, String description, Position coordinates, Municipality municipality) {
        this(name, description, coordinates, municipality, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for a POI.
     *
     * @param name the name of the POI
     * @param description the textual description of the POI
     * @param coordinates the geographical coordinates of the POI
     * @param municipality the municipality of the POI
     * @param images the representative multimedia content of the POI
     * @param contents the contents associated to the POI
     */
    public POI(String name, String description, Position coordinates, Municipality municipality, List<File> images, List<Content> contents) {
        super(name, description, municipality, images);
        this.position = coordinates;
        this.contents = contents;
    }

    /**
     * Returns the contents associated to the geo-localizable object.
     *
     * @param content the contents associated to the geo-localizable object
     * @return true if the contents associated to the geo-localizable object has been added, false otherwise
     */
    public boolean addContent(Content content) {
        return this.contents.add(content);
    }

    /**
     * Removes a content from the geo-localizable object.
     *
     * @param content the content to remove
     * @return true if the content has been removed, false otherwise
     */
    public boolean removeContent(Content content) {
        return this.contents.remove(content);
    }

    @Override
    public Position getPosition() {
        return this.position;
    }
}
