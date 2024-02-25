package it.cs.unicam.app_valorizzazione_territorio.persistence;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan
public class RequestsTest {
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
    public void shouldAcceptApprovalRequest1() {
        assertFalse(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.PIAZZA_LIBERTA)).get().isApproved());
        requestJpaRepository.findById(SampleRepositoryProvider.RICHIESTA_PIAZZA_LIBERTA.getID()).get().approve();
        requestJpaRepository.flush();

        assertTrue(geoLocatableJpaRepository.findOne(Example.of(SampleRepositoryProvider.PIAZZA_LIBERTA)).get().isApproved());
    }

    @Test
    public void shouldAcceptApprovalRequest2() {
        assertFalse(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PITTURA_2)).get().isApproved());
        requestJpaRepository.findById(SampleRepositoryProvider.RICHIESTA_PITTURA_CAVOUR.getID()).get().approve();
        requestJpaRepository.flush();

        assertTrue(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PITTURA_2)).get().isApproved());
    }

    @Test
    public void shouldRejectApprovalRequest() {
        assertEquals(ApprovalStatusEnum.PENDING,
                contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_SAN_VENANZIO)).get().getApprovalStatus());
        requestJpaRepository.findById(SampleRepositoryProvider.RICHIESTA_FOTO_BASILICA.getID()).get().reject();
        requestJpaRepository.flush();

        assertEquals(ApprovalStatusEnum.REJECTED,
                contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_SAN_VENANZIO)).get().getApprovalStatus());
    }

    @Test
    public void shouldAcceptPoiContentDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.MANIFESTO_CORSA_SPADA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.MANIFESTO_CORSA_SPADA)).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.CORSA_SPADA.getID()).get()
                .getContents().stream()
                .anyMatch(poi -> poi.equals(SampleRepositoryProvider.MANIFESTO_CORSA_SPADA))
        );
    }

    @Test
    public void shouldAcceptPoiContentDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.FOTO_SAN_VENANZIO));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(SampleRepositoryProvider.RICHIESTA_FOTO_BASILICA.getID()).isPresent());
        assertFalse(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_SAN_VENANZIO)).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.BASILICA_SAN_VENANZIO.getID()).get()
                .getContents().stream()
                .anyMatch(poi -> poi.equals(SampleRepositoryProvider.FOTO_SAN_VENANZIO))
        );
    }

    @Test
    public void shouldAcceptContestContentDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.FOTO_STRADE_MACERATA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_STRADE_MACERATA)).isPresent());
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(SampleRepositoryProvider.CONCORSO_FOTO_2024.getBaseContestId()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.equals(SampleRepositoryProvider.FOTO_STRADE_MACERATA))
        );
    }

    @Test
    public void shouldAcceptContestContentDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.FOTO_PITTURA_2));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(SampleRepositoryProvider.FOTO_PITTURA_2)).isPresent());
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(SampleRepositoryProvider.CONCORSO_PITTURA.getBaseContestId()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.equals(SampleRepositoryProvider.FOTO_PITTURA_1))
        );
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.CORSA_SPADA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.CORSA_SPADA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(SampleRepositoryProvider.MANIFESTO_CORSA_SPADA.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.PIAZZA_LIBERTA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIAZZA_LIBERTA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(SampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest3() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(SampleRepositoryProvider.PIZZERIA_ENJOY));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIZZERIA_ENJOY.getID()).isPresent());
        assertFalse(contentJpaRepository.findById(SampleRepositoryProvider.FOTO_PIZZA_MARGHERITA.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestModificationRequest1() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(SampleRepositoryProvider.PIAZZA_LIBERTA,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Piazza della Libertà modificata"))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Piazza della Libertà modificata",
                geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIAZZA_LIBERTA.getID()).get().getDescription());
    }

    @Test
    public void shouldAcceptPointOfInterestModificationRequest2() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(SampleRepositoryProvider.PIZZERIA_ENJOY,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Pizzeria Enjoy modificata"),
                                new ImmutablePair<>(Parameter.NAME, "Nome modificato"),
                                new ImmutablePair<>(Parameter.POSITION, new Position(0, 0)))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Nome modificato",
                geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIZZERIA_ENJOY.getID()).get().getName());
        assertEquals("Pizzeria Enjoy modificata",
                geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIZZERIA_ENJOY.getID()).get().getDescription());
        assertEquals(new Position(0, 0),
                geoLocatableJpaRepository.findPointOfInterestById(SampleRepositoryProvider.PIZZERIA_ENJOY.getID()).get().getPosition());
    }

    @Test
    public void shouldAcceptContentModificationRequest1() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(SampleRepositoryProvider.FOTO_PITTURA_1,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Foto pittura 1 modificata"))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Foto pittura 1 modificata",
                contentJpaRepository.findById(SampleRepositoryProvider.FOTO_PITTURA_1.getID()).get().getDescription());
    }

    @Test
    public void shouldAcceptContentModificationRequest2() {
        File file = new File("/it/cs/unicam/app_valorizzazione_territorio/persistence/RequestsTest.java");
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(SampleRepositoryProvider.FOTO_PITTURA_2,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Foto pittura 2 modificata"),
                                new ImmutablePair<>(Parameter.ADD_FILE, file))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Foto pittura 2 modificata",
                contentJpaRepository.findById(SampleRepositoryProvider.FOTO_PITTURA_2.getID()).get().getDescription());
        assertTrue(contentJpaRepository.findById(SampleRepositoryProvider.FOTO_PITTURA_2.getID()).get().getFiles().contains(file));
    }

    @Test
    public void shouldAcceptPromotionRequest() {
        Request<?> promotionRequest =
                requestJpaRepository.save(RequestFactory.getPromotionRequest(SampleRepositoryProvider.TURIST_1,
                        SampleRepositoryProvider.roles.get(SampleRepositoryProvider.CAMERINO).get(AuthorizationEnum.ADMINISTRATOR)));
        requestJpaRepository.flush();

        promotionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(promotionRequest.getID()).isPresent());
        assertTrue(userJpaRepository.findById(SampleRepositoryProvider.TURIST_1.getID()).get()
                .getAuthorizations(SampleRepositoryProvider.CAMERINO).stream()
                .anyMatch(authorization -> authorization.equals(AuthorizationEnum.ADMINISTRATOR)));
    }

    @AfterEach
    void clearAll() {
        sampleRepositoryProvider.clearAllRepositories();
    }
}
