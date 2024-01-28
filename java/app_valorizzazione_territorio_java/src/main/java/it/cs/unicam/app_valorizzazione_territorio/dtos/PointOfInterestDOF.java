package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.PositionParser;

import java.io.File;
import java.util.List;

/**
 * This class represents a Point of Interest Detailed Output Format object.
 *
 */
public class PointOfInterestDOF implements Identifiable, Positionable {
    private final String name;
    private final String description;
    private final String position;
    private final MunicipalitySOF municipalitySOF;
    private final String classification;
    private final List<File> images;
    private final List<ContentSOF> contents;
    private final long ID;

    public PointOfInterestDOF(String name,
                              String description,
                              String position,
                              MunicipalitySOF municipalitySOF,
                              String classification,
                              List<File> images,
                              List<ContentSOF> contents,
                              long ID) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.municipalitySOF = municipalitySOF;
        this.classification = classification;
        this.images = images;
        this.contents = contents;
        this.ID = ID;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String position() {
        return position;
    }

    public MunicipalitySOF municipalitySOF() {
        return municipalitySOF;
    }

    public String classification() {
        return classification;
    }

    public List<File> images() {
        return images;
    }

    public List<ContentSOF> contents() {
        return contents;
    }

    public long ID() {
        return ID;
    }

    @Override
    public long getID() {
        return this.ID();
    }

    @Override
    public Position getPosition() {
        return PositionParser.parse(this.position());
    }
}

