package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.NotEnoughGeoLocatablesException;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class, GeoLocatableHandler.class})
@DataJpaTest
public class GeoLocatableHandlerTest {
    @Autowired
    private SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    private GeoLocatableHandler geoLocatableHandler;
    private PointOfInterestIF poiIFSample1;
    private PointOfInterestIF poiIFSample2;
    private PointOfInterestIF poiIFSample3;
    private CompoundPointIF cpointInsertableSample;
    private CompoundPointIF cpointNonInsertableSample;
    private CompoundPointIF cpointNonInsertableSample2;

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();;


        poiIFSample1 = new PointOfInterestIF(
                "Test POI",
                "Test Description",
                sampleRepositoryProvider.CAMERINO.getPosition(),
                sampleRepositoryProvider.CAMERINO.getID(),
                Collections.emptyList(),
                "Attraction",
                "Monument",
                new Date(),
                new Date(),
                new ArrayList<>()
        );

        poiIFSample2 = new PointOfInterestIF(
                "Test POI",
                "Test Description",
                sampleRepositoryProvider.MACERATA.getPosition(),
                sampleRepositoryProvider.MACERATA.getID(),
                Collections.emptyList(),
                "Attraction",
                "Monument",
                new Date(),
                new Date(),
                new ArrayList<>()
        );

        poiIFSample3 = new PointOfInterestIF(
                "Test POI",
                "Test Description",
                sampleRepositoryProvider.CAMERINO.getPosition(),
                sampleRepositoryProvider.MACERATA.getID(),
                Collections.emptyList(),
                "Attraction",
                "Monument",
                new Date(),
                new Date(),
                new ArrayList<>()
        );

        cpointInsertableSample = new CompoundPointIF(
                "Test Compound Point",
                "Test Description",
                CompoundPointTypeEnum.EXPERIENCE.toString(),
                List.of(sampleRepositoryProvider.UNIVERSITY_CAMERINO.getID(),
                        sampleRepositoryProvider.PIZZERIA_ENJOY.getID()),
                Collections.emptyList()
        );

        cpointNonInsertableSample = new CompoundPointIF(
                "Test Compound Point",
                "Test Description",
                CompoundPointTypeEnum.EXPERIENCE.toString(),
                List.of(sampleRepositoryProvider.PIZZERIA_ENJOY.getID(),
                        sampleRepositoryProvider.PIAZZA_LIBERTA.getID()),
                Collections.emptyList()
        );

        cpointNonInsertableSample2 = new CompoundPointIF(
                "Test Compound Point",
                "Test Description",
                CompoundPointTypeEnum.EXPERIENCE.toString(),
                List.of(sampleRepositoryProvider.UNIVERSITY_CAMERINO.getID()),
                Collections.emptyList()
        );

    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldInsertPointOfInterest() {

        long id = geoLocatableHandler.insertPointOfInterest(
                sampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample1);

        assertTrue(sampleRepositoryProvider.getRequestJpaRepository()
                .findAll()
                .stream()
                .anyMatch(request -> request.getItem().getID() == id));
    }

    @Test
    void shouldInsertPointOfInterest2() {

        long id = geoLocatableHandler.insertPointOfInterest(
                sampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample2);

        assertTrue(sampleRepositoryProvider.getRequestJpaRepository()
                .findAll()
                .stream()
                .anyMatch(request -> request.getItem().getID() == id));
    }


    @Test
    void shouldNotInsertPointOfInterestWithInvalidClassification() {
        long id = geoLocatableHandler.insertPointOfInterest(
                sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                poiIFSample1);

        assertFalse(sampleRepositoryProvider.getRequestJpaRepository()
                .findAll()
                .stream()
                .anyMatch(request -> request.getItem().getID() == id));

        assertTrue(sampleRepositoryProvider.getGeoLocatableJpaRepository().findById(id).isPresent());
    }

    @Test
    public void shouldNotInsertPointOfInterestWithInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> geoLocatableHandler.insertPointOfInterest(
                sampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample3));
    }

    @Test
    public void shouldInsertCompoundPoint() {
        long id = geoLocatableHandler.insertCompoundPoint(
                sampleRepositoryProvider.CAMERINO.getID(),
                sampleRepositoryProvider.TURIST_1.getID(),
                cpointInsertableSample);

        assertTrue(sampleRepositoryProvider.getRequestJpaRepository()
                .findAll()
                .stream()
                .anyMatch(request -> request.getItem().getID() == id));

    }

    @Test
    public void shouldInsertCompoundPointWithNoRequest() {
        long id = geoLocatableHandler.insertCompoundPoint(
                sampleRepositoryProvider.CAMERINO.getID(),
                sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                cpointInsertableSample);

        assertTrue(sampleRepositoryProvider.getGeoLocatableJpaRepository().findById(id).get().isApproved());
    }

    @Test
    public void shouldNotInsertCompoundPoint1() {
        assertThrows(IllegalArgumentException.class, () -> geoLocatableHandler.insertCompoundPoint(
                sampleRepositoryProvider.CAMERINO.getID(),
                sampleRepositoryProvider.TURIST_1.getID(),
                cpointNonInsertableSample));


    }

    @Test
    public void shouldObtainOnlyPointOfInterests() {
        geoLocatableHandler.insertCompoundPoint(
                sampleRepositoryProvider.CAMERINO.getID(),
                sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                cpointInsertableSample);

        List<GeoLocatableOF> vals = geoLocatableHandler.getFilteredPointOfInterests(
                sampleRepositoryProvider.CAMERINO.getID(),
                List.of());
        assertTrue(vals
                .stream()
                .map(GeoLocatableOF::getID)
                .map(id -> sampleRepositoryProvider.getGeoLocatableJpaRepository().findById(id).get())
                .allMatch(poi -> poi instanceof PointOfInterest));
    }

    @Test
    public void shouldNotInsertCompoundPoint2() {
        assertThrows(NotEnoughGeoLocatablesException.class, () -> geoLocatableHandler.insertCompoundPoint(
                sampleRepositoryProvider.CAMERINO.getID(),
                sampleRepositoryProvider.TURIST_1.getID(),
                cpointNonInsertableSample2));


    }

    @Test
    void handleMap() {
        try {
            assertEquals(geoLocatableHandler
                            .visualizeMap(sampleRepositoryProvider.CAMERINO.getID(), sampleRepositoryProvider
                                    .getMunicipalityJpaRepository()
                                    .getByID(sampleRepositoryProvider.CAMERINO.getID()).get().getCoordinatesBox())
                            .points()
                            .stream()
                            .map(Identifiable::getID)
                            .toList(),

                    sampleRepositoryProvider
                            .getMunicipalityJpaRepository()
                            .getByID(sampleRepositoryProvider.CAMERINO.getID()).get()
                            .getGeoLocatables()
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
            assertEquals(1, geoLocatableHandler
                    .visualizeFilteredMap(sampleRepositoryProvider.CAMERINO.getID(),
                            sampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
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
            List<Long> ids = geoLocatableHandler
                    .visualizeFilteredMap(sampleRepositoryProvider.CAMERINO.getID(),
                            sampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
                            List.of(new SearchFilter("NAME", "CONTAINS", "facility"),
                                    new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                                            new CoordinatesBox(PositionParser.parse("Position{latitude=43.1454, longitude=13.08843}"),
                                                    PositionParser.parse("Position{latitude=43.14453, longitude=13.0906}")))))
                    .points()
                    .stream().map(Identifiable::getID).toList();

            assertEquals(1, ids.size());
            assertEquals(ids.get(0), sampleRepositoryProvider.GAS_FACILITY.getID());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void handleMap3() {
        try {
            assertEquals(0, geoLocatableHandler.visualizeFilteredMap(sampleRepositoryProvider.CAMERINO.getID(),
                            sampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
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
