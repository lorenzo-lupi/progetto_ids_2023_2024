package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Message Detailed Output Format object.
 *
 * @param senderName
 * @param senderEmail
 * @param text
 * @param date
 * @param attachments
 * @param read
 * @param ID
 */
public record MessageDOF(String senderName,
                         String senderEmail,
                         String text,
                         Date date,
                         List<File> attachments,
                         boolean read,
                         long ID) implements Identifiable {
    @Override
    public long getID() {
        return ID;
    }
}
