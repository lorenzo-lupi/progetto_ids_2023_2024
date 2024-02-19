package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Contest Request Data Output Format object.
 *
 * @param userSOF
 * @param contestSOF
 * @param date
 * @param contentDOF
 * @param ID
 */
public record ContestRequestDOF(UserSOF userSOF,
                                ContestSOF contestSOF,
                                Date date,
                                ContentDOF contentDOF,
                                long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
