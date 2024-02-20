package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.NotEnoughGeoLocatablesException;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Timetable;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoLocatableHandlerTest {

    private static final PointOfInterestIF poiIFSample1 = new PointOfInterestIF(
            "Test POI",
            "Test Description",
            SampleRepositoryProvider.CAMERINO.getPosition(),
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

    private static final CompoundPointIF cpointInsertableSample = new CompoundPointIF(
            "Test Compound Point",
            "Test Description",
            CompoundPointTypeEnum.EXPERIENCE.toString(),
            List.of(SampleRepositoryProvider.UNIVERSITY_CAMERINO.getID(),
                    SampleRepositoryProvider.PIZZERIA_ENJOY.getID()),
            Collections.emptyList()
    );

    private static final CompoundPointIF cpointNonInsertableSample = new CompoundPointIF(
            "Test Compound Point",
            "Test Description",
            CompoundPointTypeEnum.EXPERIENCE.toString(),
            List.of(SampleRepositoryProvider.PIZZERIA_ENJOY.getID(),
                    SampleRepositoryProvider.PIAZZA_LIBERTA.getID()),
            Collections.emptyList()
    );

    private static final CompoundPointIF cpointNonInsertableSample2 = new CompoundPointIF(
            "Test Compound Point",
            "Test Description",
            CompoundPointTypeEnum.EXPERIENCE.toString(),
            List.of(SampleRepositoryProvider.UNIVERSITY_CAMERINO.getID()),
            Collections.emptyList()
    );

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @AfterEach
    public void clear() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldInsertPointOfInterest() {

        long id = GeoLocatableHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample1);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));

        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void shouldInsertPointOfInterest2() {

        long id = GeoLocatableHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample2);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }


    @Test
    void shouldNotInsertPointOfInterestWithInvalidClassification() {
        long id = GeoLocatableHandler.insertPointOfInterest(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                poiIFSample1);

        assertFalse(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));

        assertNotNull(MunicipalityRepository.getInstance().getGeoLocatableByID(id));
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    public void shouldNotInsertPointOfInterestWithInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> GeoLocatableHandler.insertPointOfInterest(
                SampleRepositoryProvider.TURIST_1.getID(),
                poiIFSample3));
    }

    @Test
    public void shouldInsertCompoundPoint() {
        long id = GeoLocatableHandler.insertCompoundPoint(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.TURIST_1.getID(),
                cpointInsertableSample);

        assertTrue(RequestRepository.getInstance()
                .getAllMunicipalityRequests()
                .anyMatch(request -> request.getItem().getID() == id));

        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    public void shouldInsertCompoundPointWithNoRequest() {
        long id = GeoLocatableHandler.insertCompoundPoint(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                cpointInsertableSample);

        assertTrue(MunicipalityRepository.getInstance().getGeoLocatableByID(id).isApproved());
    }

    @Test
    public void shouldNotInsertCompoundPoint1() {
        assertThrows(IllegalArgumentException.class, () -> GeoLocatableHandler.insertCompoundPoint(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.TURIST_1.getID(),
                cpointNonInsertableSample));


    }

    @Test
    public void shouldObtainOnlyPointOfInterests() {
        GeoLocatableHandler.insertCompoundPoint(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                cpointInsertableSample);

        List<GeoLocatableSOF> vals = GeoLocatableHandler.getFilteredPointOfInterests(
                SampleRepositoryProvider.CAMERINO.getID(),
                List.of());
        assertTrue(vals
                .stream()
                .map(GeoLocatableSOF::getID)
                .map(id -> MunicipalityRepository.getInstance().getGeoLocatableByID(id))
                .allMatch(poi -> poi instanceof PointOfInterest));
    }

    @Test
    public void shouldNotInsertCompoundPoint2() {

        assertThrows(NotEnoughGeoLocatablesException.class, () -> GeoLocatableHandler.insertCompoundPoint(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.TURIST_1.getID(),
                cpointNonInsertableSample2));


    }

    @Test
    void handleMap() {
        try {
            assertEquals(GeoLocatableHandler
                            .visualizeMap(SampleRepositoryProvider.CAMERINO.getID(), SampleRepositoryProvider.CAMERINO.getCoordinatesBox())
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
            assertEquals(1, GeoLocatableHandler
                    .visualizeFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            SampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
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
            List<Long> ids = GeoLocatableHandler
                    .visualizeFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            SampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
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
            assertEquals(0, GeoLocatableHandler.visualizeFilteredMap(SampleRepositoryProvider.CAMERINO.getID(),
                            SampleRepositoryProvider.CAMERINO.getCoordinatesBox(),
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
