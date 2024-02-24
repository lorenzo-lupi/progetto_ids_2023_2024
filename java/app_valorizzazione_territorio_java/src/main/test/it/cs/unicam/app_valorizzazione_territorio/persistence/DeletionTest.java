package it.cs.unicam.app_valorizzazione_territorio.persistence;

import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DeletionTest {

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
    public void shouldDeleteRepositories() {
        JpaTestEnvironment.clearMessages(messageJpaRepository);
        assertEquals(0, messageJpaRepository.count());

        JpaTestEnvironment.clearRequests(requestJpaRepository);
        assertEquals(0, requestJpaRepository.count());

        JpaTestEnvironment.clearContents(contentJpaRepository);
        assertEquals(0, contentJpaRepository.count());

        JpaTestEnvironment.clearContests(contestJpaRepository);
        assertEquals(0, contestJpaRepository.count());

        JpaTestEnvironment.clearGeoLocatables(geoLocatableJpaRepository);
        assertEquals(0, geoLocatableJpaRepository.count());

        JpaTestEnvironment.clearUsers(userJpaRepository);
        assertEquals(0, userJpaRepository.count());

        JpaTestEnvironment.clearMunicipalities(municipalityJpaRepository, roleJpaRepository);
        assertEquals(0, roleJpaRepository.count());
    }

    @Test
    public void shouldDeleteContestContent() {
        contentJpaRepository.delete(JpaTestEnvironment.FOTO_STRADE_MACERATA);
        contentJpaRepository.flush();
        assertFalse(contentJpaRepository.findById(JpaTestEnvironment.FOTO_STRADE_MACERATA.getID()).isPresent());

        assertFalse(contestJpaRepository.findById(JpaTestEnvironment.CONCORSO_FOTO_2024.getID()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.getID() == JpaTestEnvironment.FOTO_STRADE_MACERATA.getID()));
    }

    @Test
    public void shouldDeletePointOfInterestContent() {
        contentJpaRepository.delete(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1);
        contentJpaRepository.flush();
        assertFalse(contentJpaRepository.findById(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());

        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIAZZA_LIBERTA.getID()).get()
                .getContents().stream()
                .anyMatch(c -> c.getID() == JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1.getID()));
    }

    @Test
    public void shouldDeletePointOfInterestWithRequest() {
        geoLocatableJpaRepository.delete(JpaTestEnvironment.PIAZZA_LIBERTA);
        requestJpaRepository.delete(JpaTestEnvironment.RICHIESTA_PIAZZA_LIBERTA);
        geoLocatableJpaRepository.flush();

        assertFalse(geoLocatableJpaRepository.findById(JpaTestEnvironment.PIAZZA_LIBERTA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());
    }

    @Test
    public void shouldDeleteCompoundPoint() {
        geoLocatableJpaRepository.delete(JpaTestEnvironment.TORUR_STUDENTE);
        geoLocatableJpaRepository.flush();

        assertFalse(geoLocatableJpaRepository.findById(JpaTestEnvironment.TORUR_STUDENTE.getID()).isPresent());
        assertTrue(contentJpaRepository.findById(JpaTestEnvironment.UNIVERSITY_CAMERINO.getID()).isPresent());
    }

    @Test
    public void shouldDeleteContest() {
        contestJpaRepository.delete(JpaTestEnvironment.CONCORSO_FOTO_2024);
        contestJpaRepository.flush();

        assertFalse(contestJpaRepository.findById(JpaTestEnvironment.CONCORSO_FOTO_2024.getID()).isPresent());
        assertFalse(contentJpaRepository.findById(JpaTestEnvironment.FOTO_STRADE_MACERATA.getID()).isPresent());
    }

    @Test
    public void shouldDeleteContestWithDecorator() {
        requestJpaRepository.deleteAll(requestJpaRepository.findAllByContest(JpaTestEnvironment.CONCORSO_PITTURA));
        contestJpaRepository.delete(JpaTestEnvironment.CONCORSO_PITTURA);
        contestJpaRepository.flush();

        assertFalse(contestJpaRepository.findById(JpaTestEnvironment.CONCORSO_PITTURA.getID()).isPresent());
        assertFalse(contestJpaRepository.findById(JpaTestEnvironment.CONCORSO_PITTURA.getBaseContestId()).isPresent());
        assertFalse(contentJpaRepository.findById(JpaTestEnvironment.FOTO_PITTURA_2.getID()).isPresent());
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
