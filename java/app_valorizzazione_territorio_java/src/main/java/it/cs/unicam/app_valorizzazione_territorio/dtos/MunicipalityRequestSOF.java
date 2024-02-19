package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Municipality Request Synthesized Output Format object.
 *
 *  @param municipalityName
 * @param date
 * @param ID
 */
public record MunicipalityRequestSOF(String userName,
                                     String municipalityName,
                                     Date date,
                                     long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }

}
