package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.GeoLocatableNotFoundException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalCoordinatesException;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.PositionParserException;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
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
        SampleRepositoryProvider.setUpMunicipalitiesRepository();
        SampleRepositoryProvider.setUpUsersRepository();
        SampleRepositoryProvider.setUpRequest5esRepositories();
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
    
    @Test
    void eventShouldBeCreated(){
        try {
            PointOfInterestInsertionHandler handler = getPointOfInterestInsertionHandler();
            handler.createPointOfInterest();
            long id = handler.insertPointOfInterest();

            GeoLocatable poi = getPoi(id);
            assertFalse(poi.isApproved());
            assertTrue(ApprovalRequestRepository.getInstance()
                    .getAllMunicipalityRequests()
                    .anyMatch(r -> r.getItem().equals(poi)));

        }
        catch (IOException | GeoLocatableNotFoundException e ){
            fail();
        }


    }

    private PointOfInterestInsertionHandler getPointOfInterestInsertionHandler() throws IOException {
        PointOfInterestInsertionHandler handler = new PointOfInterestInsertionHandler(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        handler.insertClassification("Event");
        handler.insertName("test event");
        handler.insertDescription("test description");
        handler.insertCoordinates("Position{latitude=43.13644468556232, longitude=13.067156069846891}");
        handler.insertStartDate("2024-03-05");
        handler.insertEndDate("2024-03-07");
        return handler;
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
        long id = pointOfInterestInsertionHandler1.insertPointOfInterest();


        try {
            GeoLocatable poi = getPoi(id);
            
            assertTrue(ApprovalRequestRepository.getInstance()
                    .getAllMunicipalityRequests()
                    .anyMatch(r -> r.getItem().equals(poi)));
            assertFalse(poi.isApproved());
        }
        catch (GeoLocatableNotFoundException e){
            fail();
        }
    }

    private GeoLocatable getPoi(long id){
        return MunicipalityRepository
                .getInstance()
                .getGeoLocatableByID(id);
    }

    private void insertPOI2() {
        useGeoLocatableInsertionMethod2();
        pointOfInterestInsertionHandler2.createPointOfInterest();
        long id = pointOfInterestInsertionHandler2.insertPointOfInterest();
        
        try {
            GeoLocatable poi = getPoi(id);
            assertTrue(poi.isApproved());

        }
        catch (GeoLocatableNotFoundException e){
            fail();
        }
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