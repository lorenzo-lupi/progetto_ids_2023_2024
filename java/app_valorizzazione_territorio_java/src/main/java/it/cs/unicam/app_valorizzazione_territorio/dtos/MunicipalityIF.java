package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a Municipality Input Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param coordinatesBox
 * @param files
 */
public record MunicipalityIF(String name,
                            String description,
                            Position position,
                            CoordinatesBox coordinatesBox,
                            List<File> files) {
    public Municipality toMunicipality() {
        return new Municipality(this.name, this.description, this.position, this.coordinatesBox, this.files);
    }
}
