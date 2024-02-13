package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.model.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageBuilder {
    private String senderName;
    private String senderEmail;
    private String text;
    private Date date;
    private List<File> attachments;
    private Message message;

    public MessageBuilder() {
        this.attachments = new ArrayList<>();
    }

    public MessageBuilder buildSenderName(String senderName) {
        this.senderName = senderName;
        this.date = new Date();
        return this;
    }

    public MessageBuilder buildSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public MessageBuilder buildText(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder buildDate(Date date) {
        this.date = date;
        return this;
    }

    public MessageBuilder buildAttachments(List<File> attachments) {
        this.attachments = attachments;
        return this;
    }

    public MessageBuilder addAttachment(File attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public MessageBuilder removeAttachment(File attachment) {
        this.attachments.remove(attachment);
        return this;
    }

    public void build() {
        this.message = new Message(senderName, senderEmail, text, date, attachments);
    }

    public Message getResult() {
        return message;
    }
}
