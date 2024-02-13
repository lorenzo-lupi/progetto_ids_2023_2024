package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.MessageBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MessageRepository;

public class MunicipalityInsertionRequestHandler {

    public static long insertMunicipalityRequestMessage(MessageIF messageIF) {
        MessageBuilder builder = new MessageBuilder();
        builder.buildSenderName(messageIF.senderName())
                .buildSenderEmail(messageIF.senderEmail())
                .buildText(messageIF.text())
                .buildAttachments(messageIF.attachments().stream().toList())
                .build();

        MessageRepository.getInstance().add(builder.getResult());
        return builder.getResult().getID();
    }
}
