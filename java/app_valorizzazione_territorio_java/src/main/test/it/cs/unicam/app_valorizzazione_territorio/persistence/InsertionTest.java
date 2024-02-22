package it.cs.unicam.app_valorizzazione_territorio.persistence;


import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class InsertionTest {
    @Autowired
    MunicipalityJpaRepository municipalityJPARepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    GeoLocatableJpaRepository geoLocatableJpaRepository;

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        JpaTestEnvironment.setUpMunicipalities(municipalityJPARepository);
        JpaTestEnvironment.setUpUsers(userJpaRepository);
        JpaTestEnvironment.setUpGeoLocatableRepository(geoLocatableJpaRepository);

    }



    @Test
    public void testInsertions() throws InterruptedException {
        assertTrue(municipalityJPARepository.findOne(Example.of(JpaTestEnvironment.CAMERINO)).isPresent());
        assertTrue(municipalityJPARepository.findOne(Example.of(JpaTestEnvironment.MACERATA)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.BASILICA_SAN_VENANZIO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIAZZA_LIBERTA)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.PIZZERIA_ENJOY)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.UNIVERSITY_CAMERINO)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.VIA_MADONNA_CARCERI)).isPresent());
        assertTrue(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.TORUR_STUDENTE)).isPresent());
    }

    @AfterAll
    public void clearAll(){
        //JpaTestEnvironment.clearMunicipalities(municipalityJPARepository);
        //JpaTestEnvironment.clearUsers(userJpaRepository);
        //assertFalse(geoLocatableJpaRepository.findOne(Example.of(JpaTestEnvironment.VIA_MADONNA_CARCERI)).isPresent());
    }


}
