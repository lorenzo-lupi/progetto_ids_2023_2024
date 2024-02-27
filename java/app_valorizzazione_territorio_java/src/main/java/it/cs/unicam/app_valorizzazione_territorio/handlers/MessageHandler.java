package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.MessageBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MessageOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Message;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MessageJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class represents a handler for the messages in the system that provides methods
 * to insert and view messages.
 */
@Service
public class MessageHandler {
    private final MessageJpaRepository messageJpaRepository;

    @Autowired
    public MessageHandler(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    /**
     * Inserts a new message in the system for a municipality request.
     * @param messageIF the message to insert
     * @return the id of the inserted message
     */
    public long insertMunicipalityRequestMessage(MessageIF messageIF) {
        MessageBuilder builder = new MessageBuilder();
        builder.buildSenderName(messageIF.senderName())
                .buildSenderEmail(messageIF.senderEmail())
                .buildText(messageIF.text())
                .buildAttachments(messageIF.attachments().stream().toList())
                .build();

        return messageJpaRepository.save(builder.getResult()).getID();
    }

    /**
     * Returns a list of all the messages in the system in a synthesized format.
     * @return a list of all the messages in the system
     */
    public List<MessageOF> viewMessages() {
        return messageJpaRepository
                .findAll()
                .stream()
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
    public MessageOF viewMessage(long id) {
        Optional<Message> message = messageJpaRepository.findByID(id);
        if (message.isEmpty()) throw new IllegalArgumentException("Message not found");

        message.get().setRead(true);
        messageJpaRepository.saveAndFlush(message.get());
        return message.get().getOutputFormat();
    }
}
