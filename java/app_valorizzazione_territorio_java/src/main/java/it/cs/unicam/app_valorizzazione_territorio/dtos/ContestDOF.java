package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.contest.ContestStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Contest Detailed Output Format object.
 *
 * @param name
 * @param animator
 * @param topic
 * @param rules
 * @param isPrivate
 * @param geoLocatable
 * @param status
 * @param startDate
 * @param cotingStartDate
 * @param endDate
 * @param ID
 */
public record ContestDOF(String name,
                         UserSOF animator,
                         String topic,
                         String rules,
                         boolean isPrivate,
                         Identifiable geoLocatable,
                         String contestStatus,
                         Date startDate,
                         Date cotingStartDate,
                         Date endDate,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
