package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MessageHandlerTest {

    private static final MessageRepository messageRepository = MessageRepository.getInstance();

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clear() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    private static final MessageIF sampleMessage1 = new MessageIF("Name Surname",
            "sample.mail@email.com",
            "sample text",
            new ArrayList<>());
    private static final MessageIF sampleMessage2 = new MessageIF("Name Surname",
            "invalidEmail",
            "sample text",
            new ArrayList<>());

    private static final MessageIF sampleMessage3 = new MessageIF("Name Surname",
            "sample.mail@email.com",
            null,
            new ArrayList<>());

    @Test
    public void shouldInsertMunicipalityRequestMessage() {
        long id = MessageHandler.insertMunicipalityRequestMessage(sampleMessage1);

        assertEquals(3, messageRepository.getItemStream().toList().size());
        assertEquals("Name Surname", MessageRepository.getInstance().getItemByID(id).getSenderName());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage() {
        assertThrows(IllegalArgumentException.class, () ->
                MessageHandler.insertMunicipalityRequestMessage(sampleMessage2));

        assertEquals(2, messageRepository.getItemStream().toList().size());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage2() {
        assertThrows(IllegalArgumentException.class, () ->
                MessageHandler.insertMunicipalityRequestMessage(sampleMessage3));

        assertEquals(2, messageRepository.getItemStream().toList().size());
    }

    @Test
    public void shouldViewMessages() {
        assertEquals(2, MessageHandler.viewMessages().size());
        assertTrue(MessageHandler.viewMessages().stream()
                .anyMatch(messageSOF -> messageSOF.senderName().equals("Mario Rossi")));
    }

    @Test
    public void shouldViewMessage() {
        long id = SampleRepositoryProvider.messages.get(0).getID();
        assertEquals("Mario Rossi", MessageHandler.viewMessage(id).senderName());
    }

    @Test
    public void shouldBeReadMessage() {
        long id = SampleRepositoryProvider.messages.get(1).getID();
        assertFalse(messageRepository.getItemByID(id).isRead());
        MessageHandler.viewMessage(id);
        assertTrue(messageRepository.getItemByID(id).isRead());
    }

}
