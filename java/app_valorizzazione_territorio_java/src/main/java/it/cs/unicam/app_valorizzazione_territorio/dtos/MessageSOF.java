package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.util.Date;

/**
 * This class represents a Message Synthesized Output Format object.
 *
 * @param senderName
 * @param date
 * @param read
 * @param ID
 */
public record MessageSOF(String senderName,
                         Date date,
                         boolean read,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return ID;
    }
}
