package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.CompoundPointIsNotItineraryException;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class})
@DataJpaTest
public class CompoundPointBuilderTest {

    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    
    private CompoundPointBuilder itinerary_builder;
    private CompoundPointBuilder experience_builder;

    @BeforeEach
    void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();

        itinerary_builder = new CompoundPointBuilder(CompoundPointTypeEnum.ITINERARY,
                sampleRepositoryProvider.CAMERINO,
                sampleRepositoryProvider.CURATOR_CAMERINO);

        experience_builder = new CompoundPointBuilder(CompoundPointTypeEnum.EXPERIENCE,
                sampleRepositoryProvider.CAMERINO,
                sampleRepositoryProvider.CURATOR_CAMERINO);

        addInfoToCompoundPoint1();
        addInfoToCompoundPoint2();
    }

    @AfterEach
    void clear() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    void addInfoToCompoundPoint1() {
        itinerary_builder.setTitle("title")
                .setDescription("description");
        itinerary_builder.addPointOfInterest((PointOfInterest) sampleRepositoryProvider.UNIVERSITY_CAMERINO)
                .addPointOfInterest((PointOfInterest) sampleRepositoryProvider.BASILICA_SAN_VENANZIO)
                .addPointOfInterest((PointOfInterest) sampleRepositoryProvider.PIZZERIA_ENJOY);
    }

    void addInfoToCompoundPoint2(){
        experience_builder.setTitle("title")
                .setDescription("description");
        experience_builder.addPointOfInterest((PointOfInterest) sampleRepositoryProvider.UNIVERSITY_CAMERINO)
                .addPointOfInterest((PointOfInterest) sampleRepositoryProvider.BASILICA_SAN_VENANZIO)
                .addPointOfInterest((PointOfInterest) sampleRepositoryProvider.PIZZERIA_ENJOY);
    }

    @Test
    void shouldBeInOrder(){
        List<PointOfInterest> poi = new LinkedList<>(itinerary_builder.getPointOfInterests());
        assertEquals(sampleRepositoryProvider.UNIVERSITY_CAMERINO, poi.get(0));
        assertEquals(sampleRepositoryProvider.BASILICA_SAN_VENANZIO, poi.get(1));
        assertEquals(sampleRepositoryProvider.PIZZERIA_ENJOY, poi.get(2));

        itinerary_builder.invertPointOfInterest((PointOfInterest)sampleRepositoryProvider.UNIVERSITY_CAMERINO,
                (PointOfInterest) sampleRepositoryProvider.PIZZERIA_ENJOY);

        poi = new LinkedList<>(itinerary_builder.getPointOfInterests());
        assertEquals( sampleRepositoryProvider.PIZZERIA_ENJOY, poi.get(0));
        assertEquals(sampleRepositoryProvider.BASILICA_SAN_VENANZIO, poi.get(1));
        assertEquals(sampleRepositoryProvider.UNIVERSITY_CAMERINO, poi.get(2));
    }

    @Test
    void shouldThrowException(){
        assertThrows(CompoundPointIsNotItineraryException.class,
                () ->  experience_builder.invertPointOfInterest((PointOfInterest)sampleRepositoryProvider.UNIVERSITY_CAMERINO,
                        (PointOfInterest) sampleRepositoryProvider.PIZZERIA_ENJOY));
    }
}
