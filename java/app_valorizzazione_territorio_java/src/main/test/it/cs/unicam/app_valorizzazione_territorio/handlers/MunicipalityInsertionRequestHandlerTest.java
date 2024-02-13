package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MessageRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MunicipalityInsertionRequestHandlerTest {

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

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clear() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldInsertMunicipalityRequestMessage() {
        long id = MunicipalityInsertionRequestHandler.insertMunicipalityRequestMessage(sampleMessage1);

        assertEquals(3, MessageRepository.getInstance().getItemStream().toList().size());
        assertEquals("Name Surname", MessageRepository.getInstance().getItemByID(id).getSenderName());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage() {
        assertThrows(IllegalArgumentException.class, () ->
                MunicipalityInsertionRequestHandler.insertMunicipalityRequestMessage(sampleMessage2));

        assertEquals(2, MessageRepository.getInstance().getItemStream().toList().size());
    }

    @Test
    public void shouldNotInsertMunicipalityRequestMessage2() {
        assertThrows(IllegalArgumentException.class, () ->
                MunicipalityInsertionRequestHandler.insertMunicipalityRequestMessage(sampleMessage3));

        assertEquals(2, MessageRepository.getInstance().getItemStream().toList().size());
    }

}
