package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.model.Message;

public class MessageRepository extends Repository<Message> {
    private static MessageRepository instance;

    private MessageRepository() {
        super();
    }

    public static MessageRepository getInstance() {
        if (instance == null) instance = new MessageRepository();
        return instance;
    }
}
