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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
public class InsertionTest {
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
    MessageJpaRepository messageJpaRepository;

    @BeforeEach
    void setUp() {
        JpaTestEnvironment.setUpMunicipalities(municipalityJpaRepository, roleJpaRepository);
        JpaTestEnvironment.setUpUsers(userJpaRepository);
        JpaTestEnvironment.setUpGeoLocatables(geoLocatableJpaRepository);
        JpaTestEnvironment.setUpContests(contestJpaRepository);
        JpaTestEnvironment.setUpContents(contentJpaRepository);
        JpaTestEnvironment.setUpRequests(requestJpaRepository);
        JpaTestEnvironment.setUpMessages(messageJpaRepository);
    }
    @Test
    public void testMunicipalityInsertions() {
        assertTrue(municipalityJpaRepository.findOne(Example.of(JpaTestEnvironment.CAMERINO)).isPresent());
        assertTrue(municipalityJpaRepository.findOne(Example.of(JpaTestEnvironment.MACERATA)).isPresent());
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
        User user = userJpaRepository.findOne(Example.of(JpaTestEnvironment.CURATOR_CAMERINO)).get();
        assertTrue(user.getRoles().stream().map(Role::authorizationEnum).anyMatch(auth -> auth.equals(AuthorizationEnum.CURATOR)));
    }

    @Test
    public void testUserInsertions() {
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.TURIST_1)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.TURIST_2)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.TURIST_3)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.CURATOR_CAMERINO)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.ENTERTAINER_CAMERINO)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.ENTERTAINER_MACERATA)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.ENTERTAINER_TEST)).isPresent());
        assertTrue(userJpaRepository.findOne(Example.of(JpaTestEnvironment.ADMINISTRATOR_CAMERINO)).isPresent());
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
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.BASILICA_SAN_VENANZIO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIAZZA_LIBERTA)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIZZERIA_ENJOY)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.UNIVERSITY_CAMERINO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.VIA_MADONNA_CARCERI)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.TORUR_STUDENTE)).isPresent());
    }

    @Test
    public void testGeoLocatableInsertion() {
        PointOfInterest newPoi = new Attraction("Test POI", "Test POI",
                new Position(43.135, 13.067),
                JpaTestEnvironment.CAMERINO, AttractionTypeEnum.OTHER, JpaTestEnvironment.TURIST_1);

        geoLocatableJpaRepository.save(newPoi);
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(newPoi)).isPresent());

        JpaTestEnvironment.CAMERINO.addGeoLocatable(newPoi);
        assertTrue(municipalityJpaRepository.findOne(Example.of(JpaTestEnvironment.CAMERINO)).get()
                .getGeoLocatables().contains(newPoi));
    }

    @Test
    public void testContestInsertions(){
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_2024)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_2025)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_PIZZA)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_PITTURA)).isPresent());
    }

    @Test
    public void testContestInsertion() {
        Contest newContest = new ContestBase("Test Contest",
                JpaTestEnvironment.ENTERTAINER_CAMERINO, "Test topic", "Test rules",
                new Date(124, 1, 1), new Date(124, 1, 2), new Date(124, 1, 3),
                JpaTestEnvironment.CAMERINO);

        contestJpaRepository.save(newContest);
        assertTrue(contestJpaRepository.findOne(Example.of(newContest)).isPresent());

        JpaTestEnvironment.CAMERINO.addContest(newContest);
        assertTrue(municipalityJpaRepository.findOne(Example.of(JpaTestEnvironment.CAMERINO)).get()
                .getContests().contains(newContest));
    }

    @Test
    public void testContestInsertion2() {
        Contest contestBase = new ContestBase("Test Contest2",
                JpaTestEnvironment.ENTERTAINER_CAMERINO, "Test topic", "Test rules",
                new Date(124, 1, 1), new Date(124, 1, 2), new Date(124, 1, 3),
                JpaTestEnvironment.CAMERINO);
        Contest newContest = new PrivateContestDecorator(contestBase,
                List.of(JpaTestEnvironment.TURIST_1, JpaTestEnvironment.TURIST_2));

        contestJpaRepository.save(newContest);
        assertTrue(contestJpaRepository.findOne(Example.of(newContest)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(contestBase)).isPresent());

        JpaTestEnvironment.CAMERINO.addContest(newContest);
        assertTrue(municipalityJpaRepository.findOne(Example.of(JpaTestEnvironment.CAMERINO)).get()
                .getContests().contains(newContest));

        assertTrue(contestJpaRepository.findAllByValidTrue().contains(newContest));
        assertFalse(contestJpaRepository.findAllByValidTrue().contains(contestBase));
        assertTrue(contestJpaRepository.findByBaseContestIdAndValidTrue(contestBase.getID()).get() instanceof ContestDecorator);
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(newContest.getID()).isPresent());
    }

    @Test
    public void testContentInsertions() {
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_SAN_VENANZIO)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_2)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PIZZA_MARGHERITA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.MANIFESTO_CORSA_SPADA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_STRADE_MACERATA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_STRADE_MACERATA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_TORRE_CIVICA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PIZZA_REGINA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PITTURA_1)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PITTURA_2)).isPresent());
    }

    @Test
    public void testContentInsertion() {
        PointOfInterestContent newContent = new PointOfInterestContent("Test content",
                (PointOfInterest) JpaTestEnvironment.PIAZZA_LIBERTA, new ArrayList<>(), JpaTestEnvironment.TURIST_1);

        contentJpaRepository.save(newContent);
        assertTrue(contentJpaRepository.findOne(Example.of(newContent)).isPresent());

        ((PointOfInterest)JpaTestEnvironment.PIAZZA_LIBERTA).addContent(newContent);
        assertTrue(geoLocatableJpaRepository.findAllPointsOfInterest().stream()
                .filter(poi -> poi.equals(JpaTestEnvironment.PIAZZA_LIBERTA))
                .flatMap(poi -> poi.getContents().stream()).anyMatch(content -> content.equals(newContent)));
    }

    @Test
    public void testRequestInsertions() {
        assertTrue(requestJpaRepository.findOne(Example.of(JpaTestEnvironment.RICHIESTA_FOTO_BASILICA)).isPresent());
        assertTrue(requestJpaRepository.findOne(Example.of(JpaTestEnvironment.RICHIESTA_PITTURA_CAVOUR)).isPresent());
        assertTrue(requestJpaRepository.findOne(Example.of(JpaTestEnvironment.RICHIESTA_PIAZZA_LIBERTA)).isPresent());
    }

    @Test
    public void testMessageInsertions() {
        assertTrue(messageJpaRepository.findOne(Example.of(JpaTestEnvironment.MESSAGGIO_1)).isPresent());
        assertTrue(messageJpaRepository.findOne(Example.of(JpaTestEnvironment.MESSAGGIO_2)).isPresent());
    }

    @AfterEach
    public void clearAll(){
        JpaTestEnvironment.clearMessages(messageJpaRepository);
        JpaTestEnvironment.clearRequests(requestJpaRepository);
        JpaTestEnvironment.clearContents(contentJpaRepository);
        JpaTestEnvironment.clearContests(contestJpaRepository);
        JpaTestEnvironment.clearGeoLocatables(geoLocatableJpaRepository);
        JpaTestEnvironment.clearUsers(userJpaRepository);
        JpaTestEnvironment.clearMunicipalities(municipalityJpaRepository, roleJpaRepository);
    }

}
