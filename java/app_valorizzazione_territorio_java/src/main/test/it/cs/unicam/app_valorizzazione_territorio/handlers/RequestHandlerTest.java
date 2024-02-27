package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.ModificationCommand;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.ModificationSetting;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.RequestJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class, RequestHandler.class})
@DataJpaTest
class RequestHandlerTest {
    
    private long entertainerID;
    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    RequestHandler requestHandler;
    @Autowired
    private RequestJpaRepository requestJpaRepository;
    @Autowired
    private MunicipalityJpaRepository municipalityJpaRepository;

    @BeforeEach
    void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
        entertainerID = sampleRepositoryProvider.ENTERTAINER_TEST.getID();
    }
    @AfterEach
    void clearRepositories() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldViewContestRequests() {
        assertEquals(requestHandler.viewContestRequests(entertainerID).stream()
                        .filter(c -> c.contestName().equals(sampleRepositoryProvider.CONCORSO_PER_TEST.getName()))
                        .toList()
                        .size(),
                2);
        assertTrue(requestHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == sampleRepositoryProvider.NEG_REQUEST.getID()));

        assertTrue(requestHandler.viewContestRequests(entertainerID).stream()
                .map(r -> r.getID())
                .anyMatch(id -> id == sampleRepositoryProvider.POS_REQUEST.getID()));

    }

    @Test
    void viewMunicipalityRequests() {
        assertThrows(UnsupportedOperationException.class, () ->
                requestHandler.viewMunicipalityRequests(entertainerID));
    }

    @Test
    void evaluateRequest(){
        requestHandler.setApprovation(sampleRepositoryProvider.POS_REQUEST.getID(), true);
        requestHandler.setApprovation(sampleRepositoryProvider.NEG_REQUEST.getID(), false);
        assertTrue(sampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(sampleRepositoryProvider.POS_REQUEST.getItem()));
        assertFalse(sampleRepositoryProvider.CONCORSO_PER_TEST.getApprovedContents().contains(sampleRepositoryProvider.NEG_REQUEST.getItem()));
    }

    @Test
    void shouldDSendGeoLocatableDeletionRequest() {
        long userId = sampleRepositoryProvider.TURIST_1.getID();
        long objectID = sampleRepositoryProvider.PIAZZA_LIBERTA.getID();
        long requestID = requestHandler.deleteGeoLocatable(userId, objectID, "Test message");

        assertTrue(requestJpaRepository.findAll().stream().map(Request::getID).anyMatch(id -> id == requestID));
        assertEquals(sampleRepositoryProvider.PIAZZA_LIBERTA,
                requestJpaRepository.findById(requestID).get().getCommand().getItem());
    }

    @Test
    void shouldInsertPromotionRequest() {
        assertTrue(sampleRepositoryProvider.TURIST_1.getRoles().isEmpty());
        long request1 = requestHandler.insertPromotionRequest(sampleRepositoryProvider.TURIST_1.getID(), sampleRepositoryProvider.CAMERINO.getID(), AuthorizationEnum.CURATOR, "Test request");
        requestJpaRepository.findById(request1).get().approve();
        assertEquals(1, sampleRepositoryProvider.TURIST_1.getRoles().size());
    }

    @Test
    void testCreateReportForGeoLocatable() {
        long reportId = requestHandler.reportGeoLocatable(
                sampleRepositoryProvider.GAS_FACILITY.getID(),
                "Gas Facility è gestita dall'ERDIS"
        );
        assertTrue(requestJpaRepository.existsById(reportId));
        requestJpaRepository.findById(reportId).get().approve();
        assertFalse(municipalityJpaRepository.existsById(sampleRepositoryProvider.GAS_FACILITY.getID()));
    }

    @Test
    void testCreateReportForPointOfInterestContent() {
        long reportId = requestHandler.reportContent(
                sampleRepositoryProvider.MANIFESTO_CORSA_SPADA.getID(),
                "test"
        );

        PointOfInterest poi = (PointOfInterest) sampleRepositoryProvider.MANIFESTO_CORSA_SPADA.getHost();
        assertTrue(requestJpaRepository.existsById(reportId));
        assertTrue(poi.getContents().contains((PointOfInterestContent) sampleRepositoryProvider.MANIFESTO_CORSA_SPADA));
        Request<?> request = requestJpaRepository.findById(reportId).get();
        request.approve();
        assertFalse(poi.getContents().contains((PointOfInterestContent) sampleRepositoryProvider.MANIFESTO_CORSA_SPADA));
    }

    @Test
    void testCreateReportForContestContent() {
        long reportId = requestHandler.reportContent(
                sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID(),
                "test"
        );
        sampleRepositoryProvider.FOTO_STRADE_MACERATA.approve();
        //CONCORSO_FOTO_2024
        assertTrue(requestJpaRepository.existsById(reportId));
        assertTrue(sampleRepositoryProvider.CONCORSO_FOTO_2024.getApprovedContents().contains(sampleRepositoryProvider.FOTO_STRADE_MACERATA));
        requestJpaRepository.findById(reportId).get().approve();
        assertFalse(sampleRepositoryProvider.CONCORSO_FOTO_2024.getApprovedContents().contains(sampleRepositoryProvider.FOTO_STRADE_MACERATA));
    }

    @Test
    void requestToModifyGeoLocatable() {
        long id = requestHandler.modifyGeoLocatable(sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
                List.of(new ModificationSetting(Parameter.NAME.toString(), "Modificato"),
                        new ModificationSetting(Parameter.DESCRIPTION.toString(), "Descrizione modificata")),
                "Modifica richiesta");

        assertNotEquals(0L, id);
        assertNotEquals("Modificato", sampleRepositoryProvider.PIAZZA_LIBERTA.getName());
        assertNotEquals("Descrizione modificata", sampleRepositoryProvider.PIAZZA_LIBERTA.getDescription());


        requestJpaRepository.findById(id).get().approve();

        assertEquals("Modificato", sampleRepositoryProvider.PIAZZA_LIBERTA.getName());
        assertEquals("Descrizione modificata", sampleRepositoryProvider.PIAZZA_LIBERTA.getDescription());
    }

    @Test
    void modifyGeoLocatable() {
        long id = requestHandler.modifyGeoLocatable(sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                sampleRepositoryProvider.GAS_FACILITY.getID(),
                List.of(new ModificationSetting(Parameter.NAME.toString(), "Modificato2"),
                        new ModificationSetting(Parameter.DESCRIPTION.toString(), "Descrizione modificata2")),
                "Modifica richiesta");

        assertEquals(0L, id);
        assertEquals("Modificato2", sampleRepositoryProvider.GAS_FACILITY.getName());
        assertEquals("Descrizione modificata2", sampleRepositoryProvider.GAS_FACILITY.getDescription());
    }

}