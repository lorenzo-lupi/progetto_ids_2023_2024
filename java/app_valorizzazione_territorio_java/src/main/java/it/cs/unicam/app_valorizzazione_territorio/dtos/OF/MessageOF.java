package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Message Output Format object.
 *
 * @param senderName
 * @param senderEmail
 * @param text
 * @param date
 * @param attachments
 * @param read
 * @param ID
 */
public record MessageOF(
        @JsonView(View.Synthesized.class)   String senderName,
        @JsonView(View.Detailed.class)      String senderEmail,
        @JsonView(View.Detailed.class)      String text,
        @JsonView(View.Synthesized.class)   Date date,
        @JsonView(View.Detailed.class)      List<File> attachments,
        @JsonView(View.Synthesized.class)   boolean read,
        @JsonView(View.Synthesized.class)   long ID
)
        implements Identifiable {
    @Override
    public long getID() {
        return ID;
    }
}
