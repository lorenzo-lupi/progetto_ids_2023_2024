package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestHandlerTest {

    private long entertainerID;

    private RequestRepository requestRepository = RequestRepository.getInstance();

    @BeforeEach
    void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
        entertainerID = SampleRepositoryProvider.ENTERTAINER_TEST.getID();
    }
    @AfterEach
    void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldViewContestRequests() {
        assertEquals(RequestHandler.viewContestRequests(entertainerID).stream()
                        .filter(c -> c.contestName().equals(SampleRepositoryProvider.CONCORSO_PER_TEST.getName()))
                        .toList()
                        .size(),
                2);
        assertTrue(RequestHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.NEG_REQUEST.getID()));

        assertTrue(RequestHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == SampleRepositoryProvider.POS_REQUEST.getID()));

    }

    @Test
    void viewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () ->
                RequestHandler.viewMunicipalityRequests(entertainerID));
    }

    @Test
    void evaluateRequest(){
        RequestHandler.setApprovation(SampleRepositoryProvider.POS_REQUEST.getID(), true);
        RequestHandler.setApprovation(SampleRepositoryProvider.NEG_REQUEST.getID(), false);
        assertTrue(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.POS_REQUEST.getItem()));
        assertFalse(SampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(SampleRepositoryProvider.NEG_REQUEST.getItem()));
    }

    @Test
    void shouldDSendGeoLocatableDeletionRequest() {
        long userId = SampleRepositoryProvider.TURIST_1.getID();
        long objectID = SampleRepositoryProvider.PIAZZA_LIBERTA.getID();
        long requestID = RequestHandler.deleteGeoLocatable(userId, objectID, "Test message");

        assertTrue(requestRepository.getItemStream().map(Request::getID).anyMatch(id -> id == requestID));
        assertEquals(SampleRepositoryProvider.PIAZZA_LIBERTA,
                requestRepository.getItemByID(requestID).getCommand().getItem());
    }

    @Test
    void shouldInsertPromotionRequest() {
        assertTrue(SampleRepositoryProvider.TURIST_1.getRoles().isEmpty());
        long request1 = RequestHandler.insertPromotionRequest(SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.CAMERINO.getID(), AuthorizationEnum.CURATOR, "Test request");
        RequestRepository.getInstance().getItemByID(request1).approve();
        assertEquals(1, SampleRepositoryProvider.TURIST_1.getRoles().size());
    }

    @Test
    void testCreateReportForGeoLocatable() {
        long reportId = RequestHandler.insertGeoLocatableReport(
                SampleRepositoryProvider.GAS_FACILITY.getID(),
                "Gas Facility è gestita dall'ERDIS"
        );
        assertTrue(RequestRepository.getInstance().contains(reportId));
        RequestRepository.getInstance().getItemByID(reportId).approve();
        assertFalse(MunicipalityRepository.getInstance().containsGeoLocatable(SampleRepositoryProvider.GAS_FACILITY.getID()));
    }

    @Test
    void testCreateReportForPointOfInterestContent() {
        long reportId = RequestHandler.insertPointerOfInterestReport(
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
        long reportId = RequestHandler.insertContentContestReport(
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

    @Test
    void requestToModifyGeoLocatable() {
        long id = RequestHandler.modifyGeoLocatable(SampleRepositoryProvider.TURIST_1.getID(),
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
        long id = RequestHandler.modifyGeoLocatable(SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.GAS_FACILITY.getID(),
                List.of(Pair.of(Parameter.NAME, "Modificato2"),
                        Pair.of(Parameter.DESCRIPTION, "Descrizione modificata2")),
                "Modifica richiesta");

        assertEquals(Repository.NULL_ID, id);
        assertEquals("Modificato2", SampleRepositoryProvider.GAS_FACILITY.getName());
        assertEquals("Descrizione modificata2", SampleRepositoryProvider.GAS_FACILITY.getDescription());
    }
}