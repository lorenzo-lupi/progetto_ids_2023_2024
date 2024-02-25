package it.cs.unicam.app_valorizzazione_territorio.persistence;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RequestsTests {

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
        JpaTestEnvironment.setUpMunicipalities(municipalityJpaRepository, roleJpaRepository);
        JpaTestEnvironment.setUpUsers(userJpaRepository);
        JpaTestEnvironment.setUpGeoLocatables(geoLocatableJpaRepository);
        JpaTestEnvironment.setUpContests(contestJpaRepository);
        JpaTestEnvironment.setUpContents(contentJpaRepository);
        JpaTestEnvironment.setUpRequests(requestJpaRepository);
        JpaTestEnvironment.setUpNotifications(notificationJpaRepository);
        JpaTestEnvironment.setUpMessages(messageJpaRepository);
    }

    @Test
    public void shouldAcceptApprovalRequest1() {
        assertFalse(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIAZZA_LIBERTA)).get().isApproved());
        requestJpaRepository.findById(JpaTestEnvironment.RICHIESTA_PIAZZA_LIBERTA.getID()).get().approve();
        requestJpaRepository.flush();

        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIAZZA_LIBERTA)).get().isApproved());
    }

    @Test
    public void shouldAcceptApprovalRequest2() {
        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PITTURA_2)).get().isApproved());
        requestJpaRepository.findById(JpaTestEnvironment.RICHIESTA_PITTURA_CAVOUR.getID()).get().approve();
        requestJpaRepository.flush();

        assertTrue(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PITTURA_2)).get().isApproved());
    }

    @Test
    public void shouldRejectApprovalRequest() {
        assertEquals(ApprovalStatusEnum.PENDING,
                contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_SAN_VENANZIO)).get().getApprovalStatus());
        requestJpaRepository.findById(JpaTestEnvironment.RICHIESTA_FOTO_BASILICA.getID()).get().reject();
        requestJpaRepository.flush();

        assertEquals(ApprovalStatusEnum.REJECTED,
                contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_SAN_VENANZIO)).get().getApprovalStatus());
    }

    @Test
    public void shouldAcceptPoiContentDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.MANIFESTO_CORSA_SPADA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.MANIFESTO_CORSA_SPADA)).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.CORSA_SPADA.getID()).get()
                .getContents().stream()
                .anyMatch(poi -> poi.equals(JpaTestEnvironment.MANIFESTO_CORSA_SPADA))
        );
    }

    @Test
    public void shouldAcceptPoiContentDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.FOTO_SAN_VENANZIO));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(JpaTestEnvironment.RICHIESTA_FOTO_BASILICA.getID()).isPresent());
        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_SAN_VENANZIO)).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.BASILICA_SAN_VENANZIO.getID()).get()
                .getContents().stream()
                .anyMatch(poi -> poi.equals(JpaTestEnvironment.FOTO_SAN_VENANZIO))
        );
    }

    @Test
    public void shouldAcceptContestContentDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.FOTO_STRADE_MACERATA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_STRADE_MACERATA)).isPresent());
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(JpaTestEnvironment.CONCORSO_FOTO_2024.getBaseContestId()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.equals(JpaTestEnvironment.FOTO_STRADE_MACERATA))
        );
    }

    @Test
    public void shouldAcceptContestContentDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.FOTO_PITTURA_2));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PITTURA_2)).isPresent());
        assertFalse(contestJpaRepository.findByBaseContestIdAndValidTrue(JpaTestEnvironment.CONCORSO_PITTURA.getBaseContestId()).get()
                .getProposalRegister().getProposals().stream()
                .anyMatch(c -> c.equals(JpaTestEnvironment.FOTO_PITTURA_1))
        );
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest1() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.CORSA_SPADA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.CORSA_SPADA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(JpaTestEnvironment.MANIFESTO_CORSA_SPADA.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest2() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.PIAZZA_LIBERTA));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIAZZA_LIBERTA.getID()).isPresent());
        assertFalse(contentJpaRepository.findPointOfInterestContentById(JpaTestEnvironment.FOTO_PIAZZA_LIBERTA_1.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestDeletionRequest3() {
        Request<?> deletionRequest =
                requestJpaRepository.save(RequestFactory.getDeletionRequest(JpaTestEnvironment.PIZZERIA_ENJOY));
        requestJpaRepository.flush();

        deletionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(deletionRequest.getID()).isPresent());
        assertFalse(geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIZZERIA_ENJOY.getID()).isPresent());
        assertFalse(contentJpaRepository.findById(JpaTestEnvironment.FOTO_PIZZA_MARGHERITA.getID()).isPresent());
    }

    @Test
    public void shouldAcceptPointOfInterestModificationRequest1() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(JpaTestEnvironment.PIAZZA_LIBERTA,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Piazza della Libertà modificata"))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Piazza della Libertà modificata",
                geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIAZZA_LIBERTA.getID()).get().getDescription());
    }

    @Test
    public void shouldAcceptPointOfInterestModificationRequest2() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(JpaTestEnvironment.PIZZERIA_ENJOY,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Pizzeria Enjoy modificata"),
                                new ImmutablePair<>(Parameter.NAME, "Nome modificato"),
                                new ImmutablePair<>(Parameter.POSITION, new Position(0, 0)))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Nome modificato",
                geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIZZERIA_ENJOY.getID()).get().getName());
        assertEquals("Pizzeria Enjoy modificata",
                geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIZZERIA_ENJOY.getID()).get().getDescription());
        assertEquals(new Position(0, 0),
                geoLocatableJpaRepository.findPointOfInterestById(JpaTestEnvironment.PIZZERIA_ENJOY.getID()).get().getPosition());
    }

    @Test
    public void shouldAcceptContentModificationRequest1() {
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(JpaTestEnvironment.FOTO_PITTURA_1,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Foto pittura 1 modificata"))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Foto pittura 1 modificata",
                contentJpaRepository.findById(JpaTestEnvironment.FOTO_PITTURA_1.getID()).get().getDescription());
    }

    @Test
    public void shouldAcceptContentModificationRequest2() {
        File file = new File("/it/cs/unicam/app_valorizzazione_territorio/persistence/RequestsTest.java");
        Request<?> modificationRequest =
                requestJpaRepository.save(RequestFactory.getModificationRequest(JpaTestEnvironment.FOTO_PITTURA_2,
                        List.of(new ImmutablePair<>(Parameter.DESCRIPTION, "Foto pittura 2 modificata"),
                                new ImmutablePair<>(Parameter.ADD_FILE, file))));
        requestJpaRepository.flush();

        modificationRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(modificationRequest.getID()).isPresent());
        assertEquals("Foto pittura 2 modificata",
                contentJpaRepository.findById(JpaTestEnvironment.FOTO_PITTURA_2.getID()).get().getDescription());
        assertTrue(contentJpaRepository.findById(JpaTestEnvironment.FOTO_PITTURA_2.getID()).get().getFiles().contains(file));
    }

    @Test
    public void shouldAcceptPromotionRequest() {
        Request<?> promotionRequest =
                requestJpaRepository.save(RequestFactory.getPromotionRequest(JpaTestEnvironment.TURIST_1,
                        JpaTestEnvironment.roles.get(JpaTestEnvironment.CAMERINO).get(AuthorizationEnum.ADMINISTRATOR)));
        requestJpaRepository.flush();

        promotionRequest.approve();
        requestJpaRepository.flush();

        assertTrue(requestJpaRepository.findById(promotionRequest.getID()).isPresent());
        assertTrue(userJpaRepository.findById(JpaTestEnvironment.TURIST_1.getID()).get()
                .getAuthorizations(JpaTestEnvironment.CAMERINO).stream()
                .anyMatch(authorization -> authorization.equals(AuthorizationEnum.ADMINISTRATOR)));
    }

    @AfterEach
    void clearAll() {
        JpaTestEnvironment.clearMessages(messageJpaRepository);
        JpaTestEnvironment.clearNotifications(notificationJpaRepository);
        JpaTestEnvironment.clearRequests(requestJpaRepository);
        JpaTestEnvironment.clearContents(contentJpaRepository);
        JpaTestEnvironment.clearContests(contestJpaRepository);
        JpaTestEnvironment.clearGeoLocatables(geoLocatableJpaRepository);
        JpaTestEnvironment.clearUsers(userJpaRepository);
        JpaTestEnvironment.clearMunicipalities(municipalityJpaRepository, roleJpaRepository);
    }
}
