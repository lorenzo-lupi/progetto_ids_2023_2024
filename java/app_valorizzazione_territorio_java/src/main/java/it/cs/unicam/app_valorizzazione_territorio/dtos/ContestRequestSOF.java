package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Contest Request Synthesized Output Format object.
 *
 * @param userName
 * @param contestName
 * @param date
 * @param ID
 */
public record ContestRequestSOF(String userName,
                                String contestName,
                                Date date,
                                long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
