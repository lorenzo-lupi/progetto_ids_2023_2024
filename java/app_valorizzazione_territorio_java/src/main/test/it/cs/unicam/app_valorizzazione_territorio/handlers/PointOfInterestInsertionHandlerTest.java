package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.Timetable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;

import org.junit.jupiter.api.*;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointOfInterestInsertionHandlerTest {

    private static final PointOfInterestIF poiIFSample1 = new PointOfInterestIF(
            "Test POI",
            "Test Description",
            new Position(43.13644468556232, 13.067156069846891),
            SampleRepositoryProvider.CAMERINO.getID(),
            Collections.emptyList(),
            "Attraction",
            "Monument",
            new Date(),
            new Date(),
            new Timetable()
    );

    private static final PointOfInterestIF poiIFSample2 = new PointOfInterestIF(
            "Test POI",
            "Test Description",
            SampleRepositoryProvider.MACERATA.getPosition(),
            SampleRepositoryProvider.MACERATA.getID(),
            Collections.emptyList(),
            "Attraction",
            "Monument",
            new Date(),
            new Date(),
            new Timetable()
    );

    private static final PointOfInterestIF poiIFSample3 = new PointOfInterestIF(
            "Test POI",
            "Test Description",
            SampleRepositoryProvider.CAMERINO.getPosition(),
            SampleRepositoryProvider.MACERATA.getID(),
            Collections.emptyList(),
            "Attraction",
            "Monument",
            new Date(),
            new Date(),
            new Timetable()
    );

    @Test
    void shouldInsertPointOfInterest() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        long id = PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                poiIFSample1);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));
    }

    @Test
    void shouldInsertPointOfInterest2() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        long id = PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                poiIFSample2);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));
    }

    @Test
    void shouldInsertPointOfInterestWithWrongMunicipalityID() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        assertThrows(IllegalArgumentException.class, () -> PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                poiIFSample2));
    }

    @Test
    void shouldNotInsertPointOfInterestWithInvalidClassification() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        long id = PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                poiIFSample1);

        assertFalse(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));

        assertNotNull(MunicipalityRepository.getInstance().getGeoLocatableByID(id));
    }

    @Test
    public void shouldNotInsertPointOfInterestWithInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                poiIFSample3));
    }

    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }
}