package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContentVisualizationHandlerTest {

    @Test
    void viewAllContents() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        SampleRepositoryProvider
                .geoLocatables.stream()
                .filter(g -> g instanceof PointOfInterest)
                .forEach(g -> viewAllContents((PointOfInterest) g));

    }


    void viewAllContents(PointOfInterest geoLocatable){
        assertEquals(ContentVisualizationHandler.viewApprovedContents(geoLocatable.getID())
                .stream()
                .map(c  -> ContentVisualizationHandler.viewContentFromRepository(c.getID()))
                .map(ContentDOF::getID)
                .map(id -> MunicipalityRepository.getInstance().getContentByID(id))
                .toList(),

                geoLocatable
                        .getApprovedContents()
                        .stream()
                        .toList()
        );
    }

    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

}