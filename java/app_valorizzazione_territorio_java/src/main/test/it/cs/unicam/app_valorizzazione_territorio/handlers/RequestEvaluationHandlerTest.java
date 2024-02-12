package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestEvaluationHandlerTest {

    private long entertainerID;

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        entertainerID = SampleRepositoryProvider.ENTERTAINER_TEST.getID();
    }
    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldViewContestRequests() {
        assertEquals(RequestEvaluationHandler.viewContestRequests(entertainerID).stream()
                        .filter(c -> c.contestName().equals(SampleRepositoryProvider.CONCORSO_PER_TEST.getName()))
                        .toList()
                        .size(),
                2);
        assertTrue(RequestEvaluationHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.NEG_REQUEST.getID()));

        assertTrue(RequestEvaluationHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.POS_REQUEST.getID()));

    }

    @Test
    void viewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () ->
                RequestEvaluationHandler.viewMunicipalityRequests(entertainerID));
    }

    @Test
    void evaluateRequest(){
        RequestEvaluationHandler.setApprovation(SampleRepositoryProvider.POS_REQUEST.getID(), true);
        RequestEvaluationHandler.setApprovation(SampleRepositoryProvider.NEG_REQUEST.getID(), false);
        assertTrue(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.POS_REQUEST.getItem()));
        assertFalse(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.NEG_REQUEST.getItem()));
    }


}