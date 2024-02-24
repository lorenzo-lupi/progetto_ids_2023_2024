package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
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
    public void testGeoLocatableInsertions(){
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.BASILICA_SAN_VENANZIO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIAZZA_LIBERTA)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIZZERIA_ENJOY)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.UNIVERSITY_CAMERINO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.VIA_MADONNA_CARCERI)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.TORUR_STUDENTE)).isPresent());
    }

    @Test
    public void testContestInsertions(){
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_2024)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_2025)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_FOTO_PIZZA)).isPresent());
        assertTrue(contestJpaRepository.findOne(Example.of(JpaTestEnvironment.CONCORSO_PITTURA)).isPresent());
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

    @AfterAll
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
