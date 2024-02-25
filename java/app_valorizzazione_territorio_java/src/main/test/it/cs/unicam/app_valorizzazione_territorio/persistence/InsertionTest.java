package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestBase;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.PrivateContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Attraction;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.AttractionTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan
public class InsertionTest {
    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    MunicipalityJpaRepository municipalityJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    RoleJpaRepository roleJpaRepository;
    @Autowired
    GeoLocatableJpaRepository geoLocatableJpaRepository;
    @Autowired
    ContestJpaRepository contestJpaRepository;
    @Autowired
    ContentJpaRepository contentJpaRepository;
    @Autowired
    RequestJpaRepository requestJpaRepository;
    @Autowired
    NotificationJpaRepository notificationJpaRepository;
    @Autowired
    MessageJpaRepository messageJpaRepository;

    @BeforeEach
    void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
    }
    @Test
    public void testMunicipalityInsertions() {
        assertTrue(municipalityJpaRepository.findOne(Example.of(SampleRepositoryProvider.CAMERINO)).isPresent());
        assertTrue(municipalityJpaRepository.findOne(Example.of(SampleRepositoryProvider.MACERATA)).isPresent());
    }

    @Test
    public void testMunicipalityInsertion() {
        Municipality newMunicipality = new Municipality("TEST MUNICIPALITY", "TEST MUNICIPALITY",
                new Position(43.135, 13.067),
                new CoordinatesBox(
                        new Position(43.135, 13.067),
                        new Position(43.135, 13.067)
                ),
                new ArrayList<>());

        municipalityJpaRepository.save(newMunicipality);
        assertTrue(municipalityJpaRepository.findOne(Example.of(newMunicipality)).isPresent());
        assertTrue(municipalityJpaRepository.findByDescriptionContaining("TEST").findFirst().isPresent());
    }

    @Test
    public void testRoleInsertion(){
        User user = userJpaRepository.findOne(Example.of(SampleRepositoryProvider.CURATOR_CAMERINO)).get();
        assertTrue(user.getRoles().stream().map(Role::authorizationEnum).anyMatch(auth -> auth.equals(AuthorizationEnum.CURATOR)));
    }

    @Test
    public void testUserInsertions() {
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.TURIST_1)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.TURIST_2)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.TURIST_3)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.CURATOR_CAMERINO)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.ENTERTAINER_CAMERINO)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.ENTERTAINER_MACERATA)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.ENTERTAINER_TEST)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(SampleRepositoryProvider.ADMINISTRATOR_CAMERINO)).isPresent());
    }

    @Test
    public void testUserInsertion() {
        User newUser = new User("Test username", "test.email@test.it", "TestPass01");
        userJpaRepository.save(newUser);

        assertTrue(userJpaRepository.findOne(Example.of(newUser)).isPresent());
        assertTrue(userJpaRepository.findByUsername("Test username").isPresent());
    }

    @Test
    public void testGeoLocatableInsertions(){
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.BASILICA_SAN_VENANZIO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.PIAZZA_LIBERTA)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.PIZZERIA_ENJOY)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.UNIVERSITY_CAMERINO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.VIA_MADONNA_CARCERI)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.TOUR_STUDENTE)).isPresent());
    }

    @Test
    public void testGeoLocatableInsertion() {
        PointOfInterest newPoi = new Attraction("Test POI", "Test POI",
                new Position(43.135, 13.067),
                SampleRepositoryProvider.CAMERINO, AttractionTypeEnum.OTHER, SampleRepositoryProvider.TURIST_1);

        geoLocatableJpaRepository.save(newPoi);
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(newPoi)).isPresent());

        SampleRepositoryProvider.CAMERINO.addGeoLocatable(newPoi);
        assertTrue(municipalityJpaRepository.findOne(Example.of(SampleRepositoryProvider.CAMERINO)).get()
                .getGeoLocatables().contains(newPoi));
    }

    @Test
    public void testContestInsertions(){
        assertTrue(contestJpaRepository.findOne(Example.of(SampleRepositoryProvider.CONCORSO_FOTO_2024)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(SampleRepositoryProvider.CONCORSO_FOTO_2025)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(SampleRepositoryProvider.CONCORSO_PITTURA)).isPresent());
    }

    @Test
    public void testContestInsertion() {
        Contest newContest = new ContestBase("Test Contest",
                SampleRepositoryProvider.ENTERTAINER_CAMERINO, "Test topic", "Test rules",
                new Date(124, 1, 1), new Date(124, 1, 2), new Date(124, 1, 3),
                SampleRepositoryProvider.CAMERINO);

        contestJpaRepository.save(newContest);
        assertTrue(contestJpaRepository.findOne(Example.of(newContest)).isPresent());

        SampleRepositoryProvider.CAMERINO.addContest(newContest);
        assertTrue(municipalityJpaRepository.findOne(Example.of(SampleRepositoryProvider.CAMERINO)).get()
                .getContests().contains(newContest));
    }

    @Test
    public void testContestInsertion2() {
        Contest contestBase = new ContestBase("Test Contest2",
                SampleRepositoryProvider.ENTERTAINER_CAMERINO, "Test topic", "Test rules",
                new Date(124, 1, 1), new Date(124, 1, 2), new Date(124, 1, 3),
                SampleRepositoryProvider.CAMERINO);
        Contest newContest = new PrivateContestDecorator(contestBase,
                List.of(SampleRepositoryProvider.TURIST_1, SampleRepositoryProvider.TURIST_2));

        contestJpaRepository.save(newContest);
        assertTrue(contestJpaRepository.findOne(Example.of(newContest)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(contestBase)).isPresent());

        SampleRepositoryProvider.CAMERINO.addContest(newContest);
        assertTrue(municipalityJpaRepository.findOne(Example.of(SampleRepositoryProvider.CAMERINO)).get()
                .getContests().contains(newContest));

        assertTrue(contestJpaRepository.findAllByValidTrue().contains(newContest));
        assertFalse(contestJpaRepository.findAllByValidTrue().contains(contestBase));
        assertTrue(contestJpaRepository.findByBaseContestIdAndValidTrue(contestBase.getID()).get() instanceof ContestDecorator);
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(newContest.getID()).isPresent());
    }

    @Test
    public void testContentInsertions() {
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_SAN_VENANZIO)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_2)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PIZZA_MARGHERITA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.MANIFESTO_CORSA_SPADA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_STRADE_MACERATA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_STRADE_MACERATA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_TORRE_CIVICA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PIZZA_REGINA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PITTURA_1)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PITTURA_2)).isPresent());
    }

    @Test
    public void testContentInsertion() {
        PointOfInterestContent newContent = new PointOfInterestContent("Test content",
                (PointOfInterest) SampleRepositoryProvider.PIAZZA_LIBERTA, new ArrayList<>(), SampleRepositoryProvider.TURIST_1);

        contentJpaRepository.save(newContent);
        assertTrue(contentJpaRepository.findOne(Example.of(newContent)).isPresent());

        ((PointOfInterest) SampleRepositoryProvider.PIAZZA_LIBERTA).addContent(newContent);
        assertTrue(geoLocatableJpaRepository.findAllPointsOfInterest().stream()
                .filter(poi -> poi.equals(SampleRepositoryProvider.PIAZZA_LIBERTA))
                .flatMap(poi -> poi.getContents().stream()).anyMatch(content -> content.equals(newContent)));
    }

    @Test
    public void testRequestInsertions() {
        assertTrue(requestJpaRepository.findOne(Example.of(SampleRepositoryProvider.RICHIESTA_FOTO_BASILICA)).isPresent());
        assertTrue(requestJpaRepository.findOne(Example.of(SampleRepositoryProvider.RICHIESTA_PITTURA_CAVOUR)).isPresent());
        assertTrue(requestJpaRepository.findOne(Example.of(SampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA)).isPresent());
    }

    @Test
    public void testMessageInsertions() {
        assertTrue(messageJpaRepository.findOne(Example.of(SampleRepositoryProvider.MESSAGGIO_1)).isPresent());
        assertTrue(messageJpaRepository.findOne(Example.of(SampleRepositoryProvider.MESSAGGIO_2)).isPresent());
    }

    @AfterEach
    public void clearAll(){
        sampleRepositoryProvider.clearAllRepositories();
    }

}
