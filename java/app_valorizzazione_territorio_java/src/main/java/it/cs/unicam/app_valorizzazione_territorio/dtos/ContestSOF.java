package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.contest.ContestStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

/**
 * This class represents a Contest Synthesized Output Format object.
 *
 * @param name
 * @param status
 * @param ID
 */
public record ContestSOF(String name,
                         ContestStatusEnum status,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
