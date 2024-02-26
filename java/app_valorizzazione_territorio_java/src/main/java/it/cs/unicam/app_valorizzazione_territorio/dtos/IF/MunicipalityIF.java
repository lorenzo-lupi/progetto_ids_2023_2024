package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

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
public record MunicipalityIF(
        String name,
        String description,
        Position position,
        CoordinatesBox coordinatesBox,
        List<String> files) {
}
