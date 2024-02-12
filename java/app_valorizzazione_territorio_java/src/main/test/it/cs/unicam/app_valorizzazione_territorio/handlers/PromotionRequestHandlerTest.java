package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class PromotionRequestHandlerTest {

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }
    @Test
    void insertPromotionRequest() {
        assertTrue(SampleRepositoryProvider.TURIST_1.getRoles().isEmpty());
        long request1 = PromotionRequestHandler.insertPromotionRequest(SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.CAMERINO.getID(), AuthorizationEnum.CURATOR, "Test request");
        RequestRepository.getInstance().getItemByID(request1).approve();
        assertEquals(1, SampleRepositoryProvider.TURIST_1.getRoles().size());
    }
}