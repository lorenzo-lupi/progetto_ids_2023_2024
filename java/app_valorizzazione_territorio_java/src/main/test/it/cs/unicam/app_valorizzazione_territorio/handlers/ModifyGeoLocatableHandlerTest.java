package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModifyGeoLocatableHandlerTest {

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }
    @AfterAll
    void tearDown() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void requestToModifyGeoLocatable() {
        long id = ModifyGeoLocatableHandler.modifyGeoLocatable(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
                List.of(Pair.of(Parameter.NAME, "Modificato"),
                        Pair.of(Parameter.DESCRIPTION, "Descrizione modificata")),
                "Modifica richiesta");

        assertNotEquals(id, Repository.NULL_ID);
        assertNotEquals("Modificato", SampleRepositoryProvider.PIAZZA_LIBERTA.getName());
        assertNotEquals("Descrizione modificata", SampleRepositoryProvider.PIAZZA_LIBERTA.getDescription());


        RequestRepository.getInstance().getItemByID(id).approve();

        assertEquals("Modificato", SampleRepositoryProvider.PIAZZA_LIBERTA.getName());
        assertEquals("Descrizione modificata", SampleRepositoryProvider.PIAZZA_LIBERTA.getDescription());
    }

    @Test
    void modifyGeoLocatable() {
        long id = ModifyGeoLocatableHandler.modifyGeoLocatable(SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.GAS_FACILITY.getID(),
                List.of(Pair.of(Parameter.NAME, "Modificato2"),
                        Pair.of(Parameter.DESCRIPTION, "Descrizione modificata2")),
                "Modifica richiesta");

        assertEquals(Repository.NULL_ID, id);
        assertEquals("Modificato2", SampleRepositoryProvider.GAS_FACILITY.getName());
        assertEquals("Descrizione modificata2", SampleRepositoryProvider.GAS_FACILITY.getDescription());
    }
}