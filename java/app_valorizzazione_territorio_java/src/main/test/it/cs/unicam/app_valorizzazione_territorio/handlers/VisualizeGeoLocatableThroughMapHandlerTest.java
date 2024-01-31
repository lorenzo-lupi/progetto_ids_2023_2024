package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisualizeGeoLocatableThroughMapHandlerTest {

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void handleMap() {
        try {
           VisualizeGeoLocatableThroughMapHandler
                   .handleMap(SampleRepositoryProvider.CAMERINO.getID())
                            .points()
                            .stream()
                            .map(Identifiable::getID);

                    SampleRepositoryProvider.CAMERINO.getGeoLocatables()
                            .stream()
                            .filter(GeoLocatable::isApproved)
                            .map(GeoLocatable::getID);

        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void handleMap1() {
    }

    @Test
    void handleMap2() {
    }
}