package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalCoordinatesException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.PositionParserException;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.ApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointOfInterestInsertionHandlerTest {


    PointOfInterestInsertionHandler pointOfInterestInsertionHandler1;
    PointOfInterestInsertionHandler pointOfInterestInsertionHandler2;

    @BeforeAll
    void init() {
        SampleRepositoryProvider.setUpMunicipalityRepository();
        SampleRepositoryProvider.setUpUsers();
        SampleRepositoryProvider.setUpRequests();
        try {
            createHandlers();
        } catch (IOException e) {
            fail();
        }
    }

    private void createHandlers() throws IOException {
        pointOfInterestInsertionHandler1 = new PointOfInterestInsertionHandler(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());
        pointOfInterestInsertionHandler2 = new PointOfInterestInsertionHandler(SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

    }


    private void useGeoLocatableInsertionMethod1() {
        pointOfInterestInsertionHandler1.insertDescription("test description 1");
        pointOfInterestInsertionHandler1.insertName("Monumento ai test caduti");
        pointOfInterestInsertionHandler1.insertClassification("Attraction");
        pointOfInterestInsertionHandler1.insertAttractionType("Monument");
    }

    private void useGeoLocatableInsertionMethod2() {
        pointOfInterestInsertionHandler2.insertDescription("test description 2");
        pointOfInterestInsertionHandler2.insertName("Test____NOME_PER_TEST2");
        pointOfInterestInsertionHandler2.insertClassification("Attraction");
        pointOfInterestInsertionHandler2.insertAttractionType("Monument");
    }

    @AfterAll
    void insertPointOfInterest() {
        insertPOI1();
        insertPOI2();
    }

    private void insertPOI1() {
        useGeoLocatableInsertionMethod1();
        pointOfInterestInsertionHandler1.createPointOfInterest();
        pointOfInterestInsertionHandler1.insertPointOfInterest();


        ApprovalRequestRepository.getInstance().getAllMunicipalityRequests()
                .filter(request -> request.canBeApprovedBy(SampleRepositoryProvider.CURATOR_CAMERINO))
                .map(ApprovalRequest::getApprovableItem)
                .filter(appr -> appr instanceof PointOfInterest)
                .map(appr -> (PointOfInterest) appr)
                .filter(poi -> poi.getName().equals("Monumento ai test caduti"))
                .findFirst()
                .ifPresentOrElse(
                        (v ->
                            {assertNotNull(MunicipalityRepository.getInstance().getPointOfInterestByID(v.getID()));
                            assertFalse(v.isApproved());
                            assertEquals(v.getUser(), SampleRepositoryProvider.TURIST_1);
                            }),
                        Assertions::fail);
    }

    private void insertPOI2() {
        useGeoLocatableInsertionMethod2();
        pointOfInterestInsertionHandler2.createPointOfInterest();
        pointOfInterestInsertionHandler2.insertPointOfInterest();

        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(MunicipalityRepository.getInstance().getAllGeoLocatables().toList());
        searchEngine.addCriterion(Parameter.USERNAME,
                new SearchCriterion<>(SearchCriterion.USERNAME, SampleRepositoryProvider.CURATOR_CAMERINO.getUsername()));

        searchEngine.addCriterion(Parameter.NAME,
                new SearchCriterion<>(SearchCriterion.EQUALS, "Test____NOME_PER_TEST2"));

        searchEngine.search()
                .getResults()
                .stream()
                .filter(g -> g.getName().equals("Test____NOME_PER_TEST2"))
                .findFirst()
                .ifPresentOrElse(v -> assertTrue(v.isApproved()),
                        Assertions::fail);

    }


    @Test
    void insertCoordinates() {
        assertThrows(IllegalCoordinatesException.class,
                () -> pointOfInterestInsertionHandler1.insertCoordinates("Position{latitude=2.29812657107886, longitude=3.451878161920886}"));
        pointOfInterestInsertionHandler1.insertCoordinates("Position{latitude=43.13644468556232, longitude=13.067156069846891}");

        assertThrows(PositionParserException.class,
                () ->pointOfInterestInsertionHandler2.insertCoordinates("Position{latitude=43.13644468556232,  longitude=1asd67156069846891}"));
        pointOfInterestInsertionHandler2.insertCoordinates("Position{latitude=43.13641468556232, longitude=13.067156069846891}");
    }


}