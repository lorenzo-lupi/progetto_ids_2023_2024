package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ModificationTests {
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
    MessageJpaRepository messageJpaRepository;

    @BeforeEach
    void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
    }

    @Test
    public void testMunicipalityModifications() {
        sampleRepositoryProvider.CAMERINO
                .setDescription("TEST MODIFICATO CAMERINO");
        municipalityJpaRepository.saveAndFlush(sampleRepositoryProvider.CAMERINO);
        assertTrue(municipalityJpaRepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst().isPresent());

    }

    @Test
    public void testGeoLocatableModifications() {
        sampleRepositoryProvider
                .CORSA_SPADA
                .setDescription("DESCRIZIONE_MODIFICATA");
        geoLocatableJpaRepository.saveAndFlush(sampleRepositoryProvider.CORSA_SPADA);
        assertEquals(municipalityJpaRepository
                .findOne(Example.of(sampleRepositoryProvider.CAMERINO))
                .get()
                .getGeoLocatables()
                .stream()
                .filter(b -> b.getDescription().equals("DESCRIZIONE_MODIFICATA"))
                .findFirst()
                .get().getDescription(), sampleRepositoryProvider.CORSA_SPADA.getDescription());
    }

    @Test
    public void addUserRole() {
        assertTrue(sampleRepositoryProvider
                .TURIST_1
                .getRoles()
                .isEmpty());
        sampleRepositoryProvider
                .TURIST_1
                .setNewRoles(List.of(AuthorizationEnum.CURATOR, AuthorizationEnum.CONTRIBUTOR),
                        sampleRepositoryProvider.CAMERINO);
        userJpaRepository.saveAndFlush(sampleRepositoryProvider.TURIST_1);


        userJpaRepository.findByUsername(sampleRepositoryProvider
                        .TURIST_1
                        .getUsername())
                .ifPresentOrElse(user -> assertTrue(user.getRoles()
                                .contains(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.CURATOR))),
                        () -> fail("User not found"));
    }


    @Test
    void modifyUserRoles() {
        assertTrue(sampleRepositoryProvider.CURATOR_CAMERINO
                .getRoles()
                .contains(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.CURATOR)));

        sampleRepositoryProvider.CURATOR_CAMERINO.removeRole(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.CURATOR));
        userJpaRepository.saveAndFlush(sampleRepositoryProvider.CURATOR_CAMERINO);
        userJpaRepository.findByUsername(sampleRepositoryProvider.CURATOR_CAMERINO.getUsername())
                .ifPresentOrElse(user -> assertFalse(user.getRoles()
                                .contains(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.CURATOR))),
                        () -> fail("User not found"));
    }

    @Test
    void addAndRemoveGeoLocatableContents(){
        PointOfInterest poi = (PointOfInterest) sampleRepositoryProvider.PIZZERIA_ENJOY;
        assertEquals(1, poi.getContents().size());
        PointOfInterestContent poic = contentJpaRepository.saveAndFlush(new PointOfInterestContent("Pizza Salsiccia e Friarelli",
                poi, List.of(), sampleRepositoryProvider.TURIST_1));
        poic.approve();
        poi.addContent(poic);

        poi.removeContent((PointOfInterestContent) sampleRepositoryProvider.FOTO_PIZZA_MARGHERITA);
        contentJpaRepository.delete(sampleRepositoryProvider.FOTO_PIZZA_MARGHERITA);
        geoLocatableJpaRepository.saveAndFlush(poi);
        assertFalse(contentJpaRepository.findOne(Example.of(sampleRepositoryProvider.FOTO_PIZZA_MARGHERITA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(poic)).isPresent());
    }

    @AfterEach
    public void clearAll(){
        sampleRepositoryProvider.clearAllRepositories();
    }
}
