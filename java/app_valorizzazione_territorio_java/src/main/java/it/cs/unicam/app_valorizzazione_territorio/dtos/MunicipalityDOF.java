package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.io.File;
import java.util.List;

/**
 * This class represents a Municipality Detailed Output Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param files
 * @param ID
 */
public record MunicipalityDOF(String name,
                              String description,
                              Position position,
                              List<File> files,
                              long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
