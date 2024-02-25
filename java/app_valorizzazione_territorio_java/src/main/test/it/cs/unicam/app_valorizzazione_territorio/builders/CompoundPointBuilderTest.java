package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.CompoundPointIsNotItineraryException;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompoundPointBuilderTest {
    CompoundPointBuilder itinerary_builder;
    CompoundPointBuilder experience_builder;

    @BeforeAll
    void setUp() {
        itinerary_builder = new CompoundPointBuilder(CompoundPointTypeEnum.ITINERARY,
                SampleRepositoryProvider.CAMERINO,
                SampleRepositoryProvider.CURATOR_CAMERINO);

        experience_builder = new CompoundPointBuilder(CompoundPointTypeEnum.EXPERIENCE,
                SampleRepositoryProvider.CAMERINO,
                SampleRepositoryProvider.CURATOR_CAMERINO);

        addInfoToCompoundPoint1();
        addInfoToCompoundPoint2();
    }
    void addInfoToCompoundPoint1() {
        itinerary_builder.setTitle("title")
                .setDescription("description");
        itinerary_builder.addPointOfInterest((PointOfInterest) SampleRepositoryProvider.UNIVERSITY_CAMERINO)
                .addPointOfInterest((PointOfInterest) SampleRepositoryProvider.BASILICA_SAN_VENANZIO)
                .addPointOfInterest((PointOfInterest) SampleRepositoryProvider.PIZZERIA_ENJOY);
    }

    void addInfoToCompoundPoint2(){
        experience_builder.setTitle("title")
                .setDescription("description");
        experience_builder.addPointOfInterest((PointOfInterest) SampleRepositoryProvider.UNIVERSITY_CAMERINO)
                .addPointOfInterest((PointOfInterest) SampleRepositoryProvider.BASILICA_SAN_VENANZIO)
                .addPointOfInterest((PointOfInterest) SampleRepositoryProvider.PIZZERIA_ENJOY);
    }

    @Test
    void shouldBeInOrder(){
        List<PointOfInterest> poi = (java.util.List<PointOfInterest>) new LinkedList<>(itinerary_builder.getPointOfInterests());
        assertEquals((PointOfInterest) SampleRepositoryProvider.UNIVERSITY_CAMERINO, poi.get(0));
        assertEquals((PointOfInterest) SampleRepositoryProvider.BASILICA_SAN_VENANZIO, poi.get(1));
        assertEquals((PointOfInterest) SampleRepositoryProvider.PIZZERIA_ENJOY, poi.get(2));

        itinerary_builder.invertPointOfInterest((PointOfInterest)SampleRepositoryProvider.UNIVERSITY_CAMERINO,
                (PointOfInterest) SampleRepositoryProvider.PIZZERIA_ENJOY);

        poi = (java.util.List<PointOfInterest>) new LinkedList<>(itinerary_builder.getPointOfInterests());
        assertEquals( SampleRepositoryProvider.PIZZERIA_ENJOY, poi.get(0));
        assertEquals(SampleRepositoryProvider.BASILICA_SAN_VENANZIO, poi.get(1));
        assertEquals(SampleRepositoryProvider.UNIVERSITY_CAMERINO, poi.get(2));
    }

    @Test
    void shouldThrowException(){
        assertThrows(CompoundPointIsNotItineraryException.class,
                () ->  experience_builder.invertPointOfInterest((PointOfInterest)SampleRepositoryProvider.UNIVERSITY_CAMERINO,
                        (PointOfInterest) SampleRepositoryProvider.PIZZERIA_ENJOY));
    }

}
