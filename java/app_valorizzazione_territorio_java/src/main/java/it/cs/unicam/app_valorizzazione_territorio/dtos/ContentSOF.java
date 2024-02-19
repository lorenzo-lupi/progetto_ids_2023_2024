package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;

/**
 * This class represents a Content Synthesized Output Format object.
 *
 * @param representativeFile
 * @param ID
 */
public record ContentSOF(File representativeFile,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
