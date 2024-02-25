package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ModificationTest {
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

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
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
    public void testMunicipalityModifications() {
        JpaTestEnvironment.CAMERINO
                .setDescription("TEST MODIFICATO CAMERINO");
        municipalityJpaRepository.save(JpaTestEnvironment.CAMERINO);
        assertTrue(municipalityJpaRepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst().isPresent());
        JpaTestEnvironment
                .CORSA_SPADA
                .setDescription("DESCRIZIONE_MODIFICATA");
        geoLocatableJpaRepository.save(JpaTestEnvironment.CORSA_SPADA);
        assertEquals(municipalityJpaRepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst()
                .get()
                .getGeoLocatables()
                .stream()
                .filter(b -> b.getDescription().equals("DESCRIZIONE_MODIFICATA"))
                .findFirst()
                .get().getDescription(), JpaTestEnvironment.CORSA_SPADA.getDescription());
    }

    @AfterAll
    public void clearAll(){
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
