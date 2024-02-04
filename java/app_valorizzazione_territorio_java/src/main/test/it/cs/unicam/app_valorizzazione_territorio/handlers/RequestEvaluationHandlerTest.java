package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestEvaluationHandlerTest {

    RequestEvaluationHandler handler;

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        handler = new RequestEvaluationHandler(SampleRepositoryProvider.ENTERTAINER_TEST.getID());

    }

    @Test
    void viewContestRequests() {
        assertEquals(handler.viewContestRequests()
                        .stream()
                        .filter(c -> c.contestName().equals(SampleRepositoryProvider.CONCORSO_PER_TEST.getName()))
                        .toList()
                        .size(),
                2);
        assertTrue(handler
                .viewContestRequests()
                .stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.NEG_REQUEST.getID()));

        assertTrue(handler
                .viewContestRequests()
                .stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.POS_REQUEST.getID()));

    }


    @Test
    void viewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () -> handler.viewMunicipalityRequests());
    }
    @Test
    void evaluateRequest(){
        handler.setApprovation(SampleRepositoryProvider.POS_REQUEST.getID(), true);
        handler.setApprovation(SampleRepositoryProvider.NEG_REQUEST.getID(), false);
        assertTrue(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.POS_REQUEST.getApprovableItem()));
        assertFalse(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.NEG_REQUEST.getApprovableItem()));
    }


}