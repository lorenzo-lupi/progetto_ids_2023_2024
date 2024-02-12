package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a builder for a municipality.
 */
public class MunicipalityBuilder {
    private String name;
    private String description;
    private Position position;
    private CoordinatesBox coordinatesBox;
    private List<File> files;

    private  Municipality municipality;

    public MunicipalityBuilder() {
        this.files = new ArrayList<>();
    }

    public MunicipalityBuilder buildName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public MunicipalityBuilder buildDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MunicipalityBuilder buildPosition(Position position) {
        this.position = position;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public MunicipalityBuilder buildCoordinatesBox(CoordinatesBox coordinatesBox) {
        this.coordinatesBox = coordinatesBox;
        return this;
    }

    public CoordinatesBox getCoordinatesBox() {
        return coordinatesBox;
    }

    public MunicipalityBuilder buildFile(File file) {
        this.files.add(file);
        return this;
    }

    public MunicipalityBuilder removeFile(File file) {
        this.files.remove(file);
        return this;
    }

    public void build() {
        this.municipality = new Municipality(name, description, position, coordinatesBox, files);
    }

    public Municipality obtainResult() {
        return this.municipality;
    }

}
