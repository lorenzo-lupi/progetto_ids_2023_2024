package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.MessageBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MessageOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Message;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MessageRepository;

import java.util.List;

/**
 * This class represents a handler for the messages in the system that provides methods
 * to insert and view messages.
 */
public class MessageHandler {

    private static final MessageRepository messageRepository = MessageRepository.getInstance();

    /**
     * Inserts a new message in the system for a municipality request.
     * @param messageIF the message to insert
     * @return the id of the inserted message
     */
    public static long insertMunicipalityRequestMessage(MessageIF messageIF) {
        MessageBuilder builder = new MessageBuilder();
        builder.buildSenderName(messageIF.senderName())
                .buildSenderEmail(messageIF.senderEmail())
                .buildText(messageIF.text())
                .buildAttachments(messageIF.attachments().stream().toList())
                .build();

        messageRepository.add(builder.getResult());
        return builder.getResult().getID();
    }

    /**
     * Returns a list of all the messages in the system in a synthesized format.
     * @return a list of all the messages in the system
     */
    public static List<MessageOF> viewMessages() {
        return messageRepository
                .getItemStream()
                .map(Message::getOutputFormat)
                .toList();
    }

    /**
     * Returns a message in a detailed format.
     * If the message with the given id exist in the system, it is marked as read.
     *
     * @param id the id of the message to view
     * @return the message in a detailed format
     * @throws IllegalArgumentException if the message with the given id does not exist
     */
    public static MessageOF viewMessage(long id) {
        Message message = messageRepository.getItemByID(id);
        message.setRead(true);
        return message.getOutputFormat();
    }
}
