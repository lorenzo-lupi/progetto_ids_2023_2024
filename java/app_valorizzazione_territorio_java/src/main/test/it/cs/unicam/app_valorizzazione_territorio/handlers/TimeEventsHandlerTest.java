package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ContentJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ContestJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.NotificationJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class, TimeEventsHandler.class})
@DataJpaTest
public class TimeEventsHandlerTest {

    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    private TimeEventsHandler TimeEventsHandler;
    @Autowired
    private ContestJpaRepository contestJpaRepository;
    @Autowired
    private GeoLocatableJpaRepository geoLocatableJpaRepository;
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    @Autowired
    private ContentJpaRepository contentJpaRepository;

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldEndContest() {
        ContestContent winningContent = (ContestContent)
                contentJpaRepository.findById(sampleRepositoryProvider.FOTO_TORRE_CIVICA.getID()).get();
        Contest contest = contestJpaRepository.findByBaseContestIdAndValidTrue(
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getBaseContestId()).get();
        winningContent.addVoter(sampleRepositoryProvider.TURIST_1);
        winningContent.addVoter(sampleRepositoryProvider.TURIST_2);
        ((ContestContent)sampleRepositoryProvider.FOTO_TORRE_CIVICA).addVoter(sampleRepositoryProvider.TURIST_3);
        contentJpaRepository.flush();
        contestJpaRepository.flush();

        TimeEventsHandler.endContest(contest.getID());
        contestJpaRepository.flush();

        contest = contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID()).get();
        winningContent = (ContestContent) contentJpaRepository.findById(sampleRepositoryProvider.FOTO_TORRE_CIVICA.getID()).get();
        assertEquals(2, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(1).visualizable());
    }

    @Test
    public void shouldEndPrivateContest() {
        ContestContent winningContent = (ContestContent)
                contentJpaRepository.findById(sampleRepositoryProvider.FOTO_PITTURA_1.getID()).get();
        Contest contest = contestJpaRepository.findByBaseContestIdAndValidTrue(
                sampleRepositoryProvider.CONCORSO_PITTURA.getBaseContestId()).get();
        winningContent.addVoter(sampleRepositoryProvider.TURIST_1);
        winningContent.addVoter(sampleRepositoryProvider.TURIST_2);
        contentJpaRepository.flush();
        contestJpaRepository.flush();

        TimeEventsHandler.endContest(contest.getBaseContestId());
        contestJpaRepository.flush();

        contest = contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_PITTURA.getID()).get();
        winningContent = (ContestContent) contentJpaRepository.findById(sampleRepositoryProvider.FOTO_PITTURA_1.getID()).get();
        assertEquals(2, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(1).visualizable());
       for (User user : contest.getParticipants()) {
            assertTrue(user.getNotifications().contains(contest.getMunicipality().getNotifications().get(1)));
        }
    }

    @Test
    public void shouldEndContestWithGeoLocation() {
        ContestContent winningContent = (ContestContent)
                contentJpaRepository.findById(sampleRepositoryProvider.FOTO_PIZZA_REGINA.getID()).get();
        Contest contest = contestJpaRepository.findById(sampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID()).get();

        TimeEventsHandler.endContest(contest.getBaseContestId());
        contestJpaRepository.flush();

        assertEquals(2, contest.getMunicipality().getNotifications().size());
        assertEquals(winningContent, contest.getMunicipality().getNotifications().get(1).visualizable());

        PointOfInterest pizzeriaEnjoy =
                geoLocatableJpaRepository.findPointOfInterestById(sampleRepositoryProvider.PIZZERIA_ENJOY.getID()).get();
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
        TimeEventsHandler.startEvent(sampleRepositoryProvider.CORSA_SPADA.getID());
        geoLocatableJpaRepository.flush();

        assertEquals(2, sampleRepositoryProvider.CORSA_SPADA.getMunicipality().getNotifications().size());
        assertEquals(sampleRepositoryProvider.CORSA_SPADA,
                sampleRepositoryProvider.CAMERINO.getNotifications().get(1).visualizable());
    }
    
}
