package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import java.io.File;
import java.util.List;

/**
 * This class represents a Message Input Format object.
 *
 * @param senderName
 * @param senderEmail
 * @param text
 * @param attachments
 */
public record MessageIF(String senderName,
                        String senderEmail,
                        String text,
                        List<File> attachments) {
}
