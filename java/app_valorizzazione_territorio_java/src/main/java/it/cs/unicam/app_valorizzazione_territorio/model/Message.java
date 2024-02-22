package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MessageOF;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

@Entity
@NoArgsConstructor(force = true)
public class Message implements Visualizable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private final String senderName;
    private final String senderEmail;
    private final String text;
    @Temporal(TemporalType.DATE)
    private final Date date;
    @ElementCollection
    private final List<File> attachments;
    private boolean read;

    /**
     * Constructor for a message.
     *
     * @param senderName the name of the sender
     * @param senderEmail the email of the sender
     * @param text the text of the message
     * @param attachments the attachments of the message
     */
    public Message(String senderName, String senderEmail, String text, List<File> attachments) {
        this(senderName, senderEmail, text, Calendar.getInstance().getTime(), attachments);
    }

    /**
     * Constructor for a message.
     *
     * @param senderName the name of the sender
     * @param senderEmail the email of the sender
     * @param text the text of the message
     * @param date the date of the message
     * @param attachments the attachments of the message
     */
    public Message(String senderName, String senderEmail, String text, Date date, List<File> attachments) {
        if (senderName == null || senderEmail == null || text == null || date == null || attachments == null)
            throw new IllegalArgumentException("Parameters cannot be null");
        if (!CredentialsUtils.isEmailValid(senderEmail))
            throw new IllegalArgumentException("Invalid email");
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.text = text;
        this.date = date;
        this.attachments = attachments;
        this.read = false;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public List<File> getAttachments() {
        return attachments.stream().toList();
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public MessageOF getOutputFormat() {
        return new MessageOF(
                this.getSenderName(),
                this.getSenderEmail(),
                this.getText(),
                this.getDate(),
                this.getAttachments(),
                this.isRead(),
                this.getID()
        );
    }
}
