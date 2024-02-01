package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;

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
            assertEquals(VisualizeGeoLocatableThroughMapHandler
                            .handleMap(SampleRepositoryProvider.CAMERINO.getID())
                            .points()
                            .stream()
                            .map(Identifiable::getID)
                            .toList(),

                    SampleRepositoryProvider.CAMERINO.getGeoLocatables()
                            .stream()
                            .filter(GeoLocatable::isApproved)
                            .map(GeoLocatable::getID)
                            .toList()
            );

        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void handleMap1() {
        try {
            assertEquals(1, VisualizeGeoLocatableThroughMapHandler
                    .handleFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            List.of(new SearchFilter("NAME", "CONTAINS", "facility")))
                    .points()
                    .size());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void handleMap2() {
        try {
            List<Long> ids = VisualizeGeoLocatableThroughMapHandler
                    .handleFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            List.of(new SearchFilter("NAME", "CONTAINS", "facility"),
                                    new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                                            new CoordinatesBox(PositionParser.parse("Position{latitude=43.1454, longitude=13.08843}"),
                                                    PositionParser.parse("Position{latitude=43.14453, longitude=13.0906}")))))
                    .points()
                    .stream().map(Identifiable::getID).toList();

            assertEquals(1, ids.size());
            assertEquals(ids.get(0), SampleRepositoryProvider.GAS_FACILITY.getID());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void handleMap3() {
        try {
            assertEquals(0, VisualizeGeoLocatableThroughMapHandler.handleFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            List.of(new SearchFilter("NAME", "CONTAINS", "facility"),
                                    new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                                            new CoordinatesBox(PositionParser.parse("Position{latitude=43.146, longitude=13.0629}"),
                                                    PositionParser.parse("Position{latitude=43.1411, longitude=13.0757}"))),
                                    new SearchFilter("NAME", "CONTAINS", "facility")))
                    .points()
                    .size());
        } catch (IOException e) {
            fail();
        }
    }
}