package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

/**
 * This class represents a User Synthesized Output Format object.
 *
 * @param username
 * @param ID
 */
public record UserSOF(String username,
                      long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
