package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan
public class ModificationTest {

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
    public void testMunicipalityModifications() {
        SampleRepositoryProvider.CAMERINO
                .setDescription("TEST MODIFICATO CAMERINO");
        municipalityJpaRepository.save(SampleRepositoryProvider.CAMERINO);
        assertTrue(municipalityJpaRepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst().isPresent());
        SampleRepositoryProvider
                .CORSA_SPADA
                .setDescription("DESCRIZIONE_MODIFICATA");
        geoLocatableJpaRepository.save(SampleRepositoryProvider.CORSA_SPADA);
        assertEquals(municipalityJpaRepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst()
                .get()
                .getGeoLocatables()
                .stream()
                .filter(b -> b.getDescription().equals("DESCRIZIONE_MODIFICATA"))
                .findFirst()
                .get().getDescription(), SampleRepositoryProvider.CORSA_SPADA.getDescription());
    }

    @AfterEach
    public void clearAll(){
        sampleRepositoryProvider.clearAllRepositories();
    }

}
