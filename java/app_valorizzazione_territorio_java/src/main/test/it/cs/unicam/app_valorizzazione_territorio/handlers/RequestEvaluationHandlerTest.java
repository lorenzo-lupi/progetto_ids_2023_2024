package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestEvaluationHandlerTest {

    @BeforeAll
    static void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void viewContestRequests() {
        assertEquals(RequestEvaluationHandler.viewContestRequests(SampleRepositoryProvider.ENTERTAINER_TEST.getID())
                        .stream()
                        .filter(c -> c.contestName().equals(SampleRepositoryProvider.CONCORSO_PER_TEST.getName()))
                        .toList()
                        .size(),
                2);
        assertTrue(RequestEvaluationHandler
                .viewContestRequests(SampleRepositoryProvider.ENTERTAINER_TEST.getID())
                .stream()
                .map(ContestRequestSOF::getID)
                .anyMatch(id -> id == SampleRepositoryProvider.NEG_REQUEST.getID()));

        assertTrue(RequestEvaluationHandler
                .viewContestRequests(SampleRepositoryProvider.ENTERTAINER_TEST.getID())
                .stream()
                .map(ContestRequestSOF::getID)
                .anyMatch(id -> id == SampleRepositoryProvider.POS_REQUEST.getID()));

    }

    @Test
    void viewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () -> RequestEvaluationHandler.viewMunicipalityRequests(SampleRepositoryProvider.ENTERTAINER_TEST.getID()));
    }

    @Test
    void evaluateRequest(){
        RequestEvaluationHandler.setApprovation(SampleRepositoryProvider.ENTERTAINER_TEST.getID(), SampleRepositoryProvider.POS_REQUEST.getID(), true);
        RequestEvaluationHandler.setApprovation(SampleRepositoryProvider.ENTERTAINER_TEST.getID(), SampleRepositoryProvider.NEG_REQUEST.getID(), false);
        assertTrue(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.POS_REQUEST.getItem()));
        assertFalse(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.NEG_REQUEST.getItem()));
    }
    @Test
    void shouldNotThrowExceptionWhenUserWithoutPermissionTriesToViewContestRequests() {
        assertDoesNotThrow(() -> RequestEvaluationHandler.viewContestRequests(SampleRepositoryProvider.ENTERTAINER_CAMERINO.getID()));
    }

    @Test
    void shouldThrowExceptionWhenUserWithoutPermissionTriesToViewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () -> RequestEvaluationHandler.viewMunicipalityRequests(SampleRepositoryProvider.TURIST_1.getID()));
    }

    @Test
    void shouldThrowExceptionWhenUserWithoutPermissionTriesToApproveOrDisapproveRequest() {
        SampleRepositoryProvider.clearRequestsRepositories();
        ApprovalRequestRepository.getInstance().add(SampleRepositoryProvider.POS_REQUEST);
        assertThrows(UnsupportedOperationException.class, () -> RequestEvaluationHandler.setApprovation(SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.POS_REQUEST.getID(), true));
    }

}