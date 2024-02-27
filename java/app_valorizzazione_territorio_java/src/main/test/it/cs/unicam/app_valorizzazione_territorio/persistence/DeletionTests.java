package it.cs.unicam.app_valorizzazione_territorio.persistence;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class})
@DataJpaTest
public class DeletionTests {
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
    public void shouldDeleteRepositories() {
        sampleRepositoryProvider.clearMessages();
        assertEquals(0, messageJpaRepository.count());

        sampleRepositoryProvider.clearNotifications();
        assertEquals(0, notificationJpaRepository.count());

        sampleRepositoryProvider.clearRequests();
        assertEquals(0, requestJpaRepository.count());

        sampleRepositoryProvider.clearContents();
        assertEquals(0, contentJpaRepository.count());

        sampleRepositoryProvider.clearContests();
        assertEquals(0, contestJpaRepository.count());

        sampleRepositoryProvider.clearGeoLocatables();
        assertEquals(0, geoLocatableJpaRepository.count());

        sampleRepositoryProvider.clearUsers();
        assertEquals(0, userJpaRepository.count());

        sampleRepositoryProvider.clearMunicipalities();
        assertEquals(0, roleJpaRepository.count());
        assertEquals(0, municipalityJpaRepository.count());
    }

    @Test
    public void shouldDeleteMessage() {
        messageJpaRepository.delete(sampleRepositoryProvider.MESSAGGIO_1);
        messageJpaRepository.flush();

        assertFalse(messageJpaRepository.findById(sampleRepositoryProvider.MESSAGGIO_1.getID()).isPresent());
    }

    @Test
    public void shouldDeleteRequest() {
        GeoLocatable geoLocatable = (GeoLocatable) sampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA.getCommand().getItem();
        requestJpaRepository.delete(sampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA);
        requestJpaRepository.flush();

        assertFalse(requestJpaRepository.findById(sampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA.getID()).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(geoLocatable)).isPresent());
    }

    @Test
    public void shouldDeleteContestContent() {
        contentJpaRepository.delete(sampleRepositoryProvider.FOTO_STRADE_MACERATA);
        contentJpaRepository.flush();
        assertFalse(contentJpaRepository.findById(sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID()).isPresent());

        assertFalse(contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.getID() == sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID()));
    }

    @Test
    public void shouldDeletePointOfInterestContent() {
        contentJpaRepository.delete(sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1);
        contentJpaRepository.flush();
        assertFalse(contentJpaRepository.findById(sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());

        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(sampleRepositoryProvider.PIAZZA_LIBERTA.getID()).get()
                .getContents().stream()
                .anyMatch(c -> c.getID() == sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID()));
    }

    @Test
    public void shouldDeletePointOfInterestWithRequest() {
        geoLocatableJpaRepository.delete(sampleRepositoryProvider.PIAZZA_LIBERTA);
        requestJpaRepository.delete(sampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA);
        geoLocatableJpaRepository.flush();

        assertFalse(geoLocatableJpaRepository.findById(sampleRepositoryProvider.PIAZZA_LIBERTA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());
    }

    @Test
    public void shouldDeleteCompoundPoint() {
        geoLocatableJpaRepository.delete(sampleRepositoryProvider.TOUR_STUDENTE);
        geoLocatableJpaRepository.flush();

        assertFalse(geoLocatableJpaRepository.findById(sampleRepositoryProvider.TOUR_STUDENTE.getID()).isPresent());
        assertTrue(geoLocatableJpaRepository.findById(sampleRepositoryProvider.UNIVERSITY_CAMERINO.getID()).isPresent());
    }

    @Test
    public void shouldDeleteContest() {
        contestJpaRepository.delete(sampleRepositoryProvider.CONCORSO_FOTO_2024);
        contestJpaRepository.flush();

        assertFalse(contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID()).isPresent());
        assertFalse(contentJpaRepository.findById(sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID()).isPresent());
    }

    @Test
    public void shouldDeleteContestWithDecorator() {
        requestJpaRepository.deleteAll(requestJpaRepository.findAllByContest(sampleRepositoryProvider.CONCORSO_PITTURA));
        contestJpaRepository.delete(sampleRepositoryProvider.CONCORSO_PITTURA);
        contestJpaRepository.flush();

        assertFalse(contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_PITTURA.getID()).isPresent());
        assertFalse(contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_PITTURA.getBaseContestId()).isPresent());
        assertFalse(contentJpaRepository.findById(sampleRepositoryProvider.FOTO_PITTURA_2.getID()).isPresent());
    }

    @AfterEach
    public void clearAll(){
        sampleRepositoryProvider.clearAllRepositories();
    }
}
