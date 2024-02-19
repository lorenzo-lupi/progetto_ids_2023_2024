package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Municipality Request Detailed Output Format object.
 *
 * @param userSOF
 * @param municipalitySOF
 * @param date
 * @param itemDOF
 * @param ID
 */
public record MunicipalityRequestDOF(UserSOF userSOF,
                                     MunicipalitySOF municipalitySOF,
                                     Date date,
                                     Identifiable itemDOF,
                                     long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
