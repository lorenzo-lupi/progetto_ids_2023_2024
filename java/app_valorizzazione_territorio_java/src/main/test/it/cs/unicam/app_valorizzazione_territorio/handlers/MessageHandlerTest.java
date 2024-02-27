package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MessageJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class, MessageHandler.class})
@DataJpaTest
public class MessageHandlerTest {

    @Autowired
    private SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    private MessageJpaRepository messageJpaRepository;
    @Autowired
    private MessageHandler messageHandler;

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpMessages();
    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearMessages();
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
        long id = messageHandler.insertMunicipalityRequestMessage(sampleMessage1);

        assertEquals(3, messageJpaRepository.count());
        assertEquals("Name Surname", messageJpaRepository.findByID(id).get().getSenderName());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage() {
        assertThrows(IllegalArgumentException.class, () ->
                messageHandler.insertMunicipalityRequestMessage(sampleMessage2));

        assertEquals(2, messageJpaRepository.count());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage2() {
        assertThrows(IllegalArgumentException.class, () ->
                messageHandler.insertMunicipalityRequestMessage(sampleMessage3));

        assertEquals(2, messageJpaRepository.count());
    }

    @Test
    public void shouldViewMessages() {
        assertEquals(2, messageHandler.viewMessages().size());
        assertTrue(messageHandler.viewMessages().stream()
                .anyMatch(messageSOF -> messageSOF.senderName().equals("Mario Rossi")));
    }

    @Test
    public void shouldViewMessage() {
        long id = sampleRepositoryProvider.MESSAGGIO_1.getID();
        assertEquals("Mario Rossi", messageHandler.viewMessage(id).senderName());
    }

    @Test
    public void shouldBeReadMessage() {
        long id = sampleRepositoryProvider.MESSAGGIO_2.getID();
        assertFalse(messageJpaRepository.findByID(id).get().isRead());
        messageHandler.viewMessage(id);
        assertTrue(messageJpaRepository.findByID(id).get().isRead());
    }
}
