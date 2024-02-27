package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeEventsHandlerTest {
/*
    MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clear() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldEndContest() {
        ContestContent winningContent = (ContestContent)
                municipalityRepository.getContentByID(SampleRepositoryProvider.FOTO_TORRE_CIVICA.getID());
        Contest contest = municipalityRepository.getContestByID(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID());
        winningContent.addVoter(SampleRepositoryProvider.TURIST_1);
        winningContent.addVoter(SampleRepositoryProvider.TURIST_2);
        ((ContestContent)SampleRepositoryProvider.FOTO_TORRE_CIVICA).addVoter(SampleRepositoryProvider.TURIST_3);

        TimeEventsHandler.endContest(contest.getID());
        assertEquals(1, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(0).visualizable());
    }

    @Test
    public void shouldEndPrivateContest() {
        ContestContent winningContent = (ContestContent)
                municipalityRepository.getContentByID(SampleRepositoryProvider.FOTO_PITTURA_1.getID());
        Contest contest = municipalityRepository.getContestByID(SampleRepositoryProvider.CONCORSO_PITTURA.getID());
        winningContent.addVoter(SampleRepositoryProvider.TURIST_1);
        winningContent.addVoter(SampleRepositoryProvider.TURIST_2);

        TimeEventsHandler.endContest(contest.getID());
        assertEquals(1, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(0).visualizable());
       for (User user : contest.getParticipants()) {
            assertTrue(user.getNotifications().contains(contest.getMunicipality().getNotifications().get(0)));
        }
    }


    @Test
    public void shouldEndContestWithGeoLocation() {
        ContestContent winningContent = (ContestContent)
                municipalityRepository.getContentByID(SampleRepositoryProvider.FOTO_PIZZA_REGINA.getID());
        Contest contest = municipalityRepository.getContestByID(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID());

        TimeEventsHandler.endContest(contest.getID());
        assertEquals(1, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(0).visualizable());

        PointOfInterest pizzeriaEnjoy =
                MunicipalityRepository.getInstance().getPointOfInterestByID(SampleRepositoryProvider.PIZZERIA_ENJOY.getID());
        assertEquals(2, pizzeriaEnjoy.getContents().size());
        assertTrue(pizzeriaEnjoy.getContents()
                .stream()
                .anyMatch(content -> content.getDescription().equals(winningContent.getDescription()))
        );
        assertTrue(pizzeriaEnjoy.getContents()
                .stream()
                .anyMatch(content -> content.getUser().equals(winningContent.getUser()))
        );
    }

    @Test
    public void shouldStartEvent() {
        TimeEventsHandler.startEvent(SampleRepositoryProvider.CORSA_SPADA.getID());

        assertEquals(1, SampleRepositoryProvider.CORSA_SPADA.getMunicipality().getNotifications().size());
        assertEquals(SampleRepositoryProvider.CORSA_SPADA,
                SampleRepositoryProvider.CAMERINO.getNotifications().get(0).visualizable());
    }

 */
}
