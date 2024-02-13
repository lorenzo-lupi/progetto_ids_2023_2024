package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteGeoLocatableHandlerTest {

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void deleteGeoLocatable() {
        long id = DeleteGeoLocatableHandler.deleteGeoLocatable(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.GAS_FACILITY.getID(),
                "message");

        RequestRepository.getInstance().getItemByID(id).approve();
        assertFalse(SampleRepositoryProvider.CAMERINO.getGeoLocatables().contains(SampleRepositoryProvider.GAS_FACILITY));

    }
}