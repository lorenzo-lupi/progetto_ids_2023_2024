package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;

/**
 * This class represents a Municipality Synthesized Output Format object.
 *
 * @param name
 * @param representativeFile
 * @param ID
 */
public record MunicipalitySOF(String name,
                              File representativeFile,
                              long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
