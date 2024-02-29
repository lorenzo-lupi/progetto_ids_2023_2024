package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Message Output Format object.
 *
 * @param ID
 * @param senderName
 * @param date
 * @param read
 * @param senderEmail
 * @param text
 * @param attachments
 */
public record MessageOF(
        @JsonView(View.Synthesized.class)
        long ID,
        @JsonView(View.Synthesized.class)
        String senderName,
        @JsonView(View.Synthesized.class)
        Date date,
        @JsonView(View.Synthesized.class)
        boolean read,
        @JsonView(View.Detailed.class)
        String senderEmail,
        @JsonView(View.Detailed.class)
        String text,
        @JsonView(View.Detailed.class)
        List<String> attachments
)
        implements Identifiable {
    @JsonIgnore
    @Override
    public long getID() {
        return ID;
    }
}
