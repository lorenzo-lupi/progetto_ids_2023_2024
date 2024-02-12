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

    @Test
    void shouldInsertPointOfInterest() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        PointOfInterestIF poiIF = new PointOfInterestIF(
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

        long id = PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                poiIF);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));
    }

    @Test
    void shouldNotInsertPointOfInterestWithInvalidClassification() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        PointOfInterestIF poiIF = new PointOfInterestIF(
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

        long id = PointOfInterestInsertionHandler.insertPointOfInterest(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                poiIF);

        assertFalse(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));

        assertNotNull(MunicipalityRepository.getInstance().getGeoLocatableByID(id));
    }

    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }
}