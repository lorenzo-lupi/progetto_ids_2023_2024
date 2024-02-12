package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
/*
        ((PointOfInterest) CORSA_SPADA).addContent((PointOfInterestContent) MANIFESTO_CORSA_SPADA);
        MANIFESTO_CORSA_SPADA.approve();

        CONCORSO_FOTO_2024.getProposalRequests().proposeContent((ContestContent) FOTO_STRADE_MACERATA);
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportInsertionHandlerTest {
    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }
    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }
    @Test
    void testCreateReportForGeoLocatable() {
        long reportId = ReportInsertionHandler.insertGeoLocatableReport(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.GAS_FACILITY.getID(),
                "Gas Facility è gestita dall'ERDIS"
                );
        assertTrue(RequestRepository.getInstance().contains(reportId));
        RequestRepository.getInstance().getItemByID(reportId).approve();
        assertFalse(MunicipalityRepository.getInstance().containsGeoLocatable(SampleRepositoryProvider.GAS_FACILITY.getID()));
    }

    @Test
    void testCreateReportForPointOfInterestContent() {
        long reportId = ReportInsertionHandler.insertPointerOfInterestReport(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MANIFESTO_CORSA_SPADA.getID(),
                "test"
        );

        PointOfInterest poi = (PointOfInterest) SampleRepositoryProvider.MANIFESTO_CORSA_SPADA.getHost();
        assertTrue(RequestRepository.getInstance().contains(reportId));
        assertTrue(poi.getContents().contains((PointOfInterestContent) SampleRepositoryProvider.MANIFESTO_CORSA_SPADA));
        Request<?> request = RequestRepository.getInstance().getItemByID(reportId);
        request.approve();
        assertFalse(poi.getContents().contains((PointOfInterestContent) SampleRepositoryProvider.MANIFESTO_CORSA_SPADA));
    }

    @Test
    void testCreateReportForContestContent() {
        long reportId = ReportInsertionHandler.insertContentContestReport(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.FOTO_STRADE_MACERATA.getID(),
                "test"
        );
        SampleRepositoryProvider.FOTO_STRADE_MACERATA.approve();
        //CONCORSO_FOTO_2024
        assertTrue(RequestRepository.getInstance().contains(reportId));
        assertTrue(SampleRepositoryProvider.CONCORSO_FOTO_2024.getApprovedContents().contains(SampleRepositoryProvider.FOTO_STRADE_MACERATA));
        RequestRepository.getInstance().getItemByID(reportId).approve();
        assertFalse(SampleRepositoryProvider.CONCORSO_FOTO_2024.getApprovedContents().contains(SampleRepositoryProvider.FOTO_STRADE_MACERATA));
    }
}