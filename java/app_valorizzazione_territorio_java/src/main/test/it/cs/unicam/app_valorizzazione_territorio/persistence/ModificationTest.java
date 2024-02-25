package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class ModificationTest {
    @Autowired
    MunicipalityJpaRepository municipalityJPARepository;
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
        JpaTestEnvironment.setUpMunicipalities(municipalityJPARepository, roleJpaRepository);
        JpaTestEnvironment.setUpUsers(userJpaRepository);
        JpaTestEnvironment.setUpGeoLocatables(geoLocatableJpaRepository);
        JpaTestEnvironment.setUpContests(contestJpaRepository);
        JpaTestEnvironment.setUpContents(contentJpaRepository);
        JpaTestEnvironment.setUpRequests(requestJpaRepository);
        JpaTestEnvironment.setUpMessages(messageJpaRepository);
    }

    @Test
    public void testMunicipalityModifications() {
        JpaTestEnvironment.CAMERINO
                .setDescription("TEST MODIFICATO CAMERINO");
        municipalityJPARepository.saveAndFlush(JpaTestEnvironment.CAMERINO);
        assertTrue(municipalityJPARepository
                .findByDescriptionContaining("MODIFICATO")
                .findFirst().isPresent());

    }

    @Test
    public void testGeoLocatableModifications() {
        JpaTestEnvironment
                .CORSA_SPADA
                .setDescription("DESCRIZIONE_MODIFICATA");
        geoLocatableJpaRepository.saveAndFlush(JpaTestEnvironment.CORSA_SPADA);
        assertEquals(municipalityJPARepository
                .findOne(Example.of(JpaTestEnvironment.CAMERINO))
                .get()
                .getGeoLocatables()
                .stream()
                .filter(b -> b.getDescription().equals("DESCRIZIONE_MODIFICATA"))
                .findFirst()
                .get().getDescription(), JpaTestEnvironment.CORSA_SPADA.getDescription());
    }

    @Test
    public void addUserRole() {
        assertTrue(JpaTestEnvironment
                .TURIST_1
                .getRoles()
                .isEmpty());
        JpaTestEnvironment
                .TURIST_1
                .setNewRoles(List.of(AuthorizationEnum.CURATOR, AuthorizationEnum.CONTRIBUTOR),
                        JpaTestEnvironment.CAMERINO);
        userJpaRepository.saveAndFlush(JpaTestEnvironment.TURIST_1);


        userJpaRepository.findByUsername(JpaTestEnvironment
                        .TURIST_1
                        .getUsername())
                .ifPresentOrElse(user -> assertTrue(user.getRoles()
                                .contains(new Role(JpaTestEnvironment.CAMERINO, AuthorizationEnum.CURATOR))),
                        () -> fail("User not found"));
    }


    @Test
    void modifyUserRoles() {
        assertTrue(JpaTestEnvironment.CURATOR_CAMERINO
                .getRoles()
                .contains(new Role(JpaTestEnvironment.CAMERINO, AuthorizationEnum.CURATOR)));

        JpaTestEnvironment.CURATOR_CAMERINO.removeRole(new Role(JpaTestEnvironment.CAMERINO, AuthorizationEnum.CURATOR));
        userJpaRepository.saveAndFlush(JpaTestEnvironment.CURATOR_CAMERINO);
        userJpaRepository.findByUsername(JpaTestEnvironment.CURATOR_CAMERINO.getUsername())
                .ifPresentOrElse(user -> assertFalse(user.getRoles()
                                .contains(new Role(JpaTestEnvironment.CAMERINO, AuthorizationEnum.CURATOR))),
                        () -> fail("User not found"));
    }

    @Test
    void addAndRemoveGeoLocatableContents(){
        PointOfInterest poi = (PointOfInterest) JpaTestEnvironment.PIZZERIA_ENJOY;
        assertEquals(1, poi.getContents().size());
        PointOfInterestContent poic = contentJpaRepository.saveAndFlush(new PointOfInterestContent("Pizza Salsiccia e Friarelli",
                poi, List.of(), JpaTestEnvironment.TURIST_1));
        poic.approve();
        poi.addContent(poic);

        poi.removeContent((PointOfInterestContent) JpaTestEnvironment.FOTO_PIZZA_MARGHERITA);
        contentJpaRepository.delete(JpaTestEnvironment.FOTO_PIZZA_MARGHERITA);
        geoLocatableJpaRepository.saveAndFlush(poi);
        assertFalse(contentJpaRepository.findOne(Example.of(JpaTestEnvironment.FOTO_PIZZA_MARGHERITA)).isPresent());
        assertTrue(contentJpaRepository.findOne(Example.of(poic)).isPresent());
    }
}
