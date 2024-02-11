package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContestInsertionHandlerTest {

    private static ContestIF sampleInputBaseContest = new ContestIF(
            "Sample Contest",
            "Sample Topic",
            "Sample rules",
            false,
            null,
            null,
            new Date(124, 10, 10),
            new Date(124, 10, 30),
            new Date(124, 11, 10));

    private static ContestIF sampleInputPrivateContest = new ContestIF(
            "Sample Contest",
            "Sample Topic",
            "Sample rules",
            true,
            List.of(SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.TURIST_2.getID(), SampleRepositoryProvider.TURIST_3.getID()),
            null,
            new Date(124, 10, 10),
            new Date(124, 10, 30),
            new Date(124, 11, 10));

    private static ContestIF sampleInputGeoLocatableContest = new ContestIF(
            "Sample Contest",
            "Sample Topic",
            "Sample rules",
            false,
            null,
            SampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
            new Date(124, 10, 10),
            new Date(124, 10, 30),
            new Date(124, 11, 10));

    private static ContestIF sampleInputPrivateGeoLocatableContest = new ContestIF(
            "Sample Contest",
            "Sample Topic",
            "Sample rules",
            true,
            List.of(SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.TURIST_2.getID(), SampleRepositoryProvider.TURIST_3.getID()),
            SampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
            new Date(124, 10, 10),
            new Date(124, 10, 30),
            new Date(124, 11, 10));

    @BeforeEach
    void setUpRepository() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    void clearRepository() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldBeUnauthorizedUser() {
        assertThrows(IllegalArgumentException.class, () -> new ContestInsertionHandler(
                SampleRepositoryProvider.TURIST_1.getID(), SampleRepositoryProvider.MACERATA.getID()));
    }

    @Test
    void shouldBeAuthorizedUser() {
        assertDoesNotThrow(() -> new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID()));
    }

    @Test
    void shouldInsertBaseContestDetails() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputBaseContest);
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertEquals(sampleInputBaseContest.name(), insertedContest.getName());
        assertEquals(sampleInputBaseContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputBaseContest.rules(), insertedContest.getRules());
        assertEquals(sampleInputBaseContest.startDate(), insertedContest.getStartDate());
        assertEquals(sampleInputBaseContest.votingStartDate(), insertedContest.getVotingStartDate());
        assertEquals(sampleInputBaseContest.endDate(), insertedContest.getEndDate());
        assertEquals(sampleInputBaseContest.isPrivate(), insertedContest.isPrivate());
        assertEquals(SampleRepositoryProvider.ENTERTAINER_MACERATA, insertedContest.getEntertainer());
    }

    @Test
    void shouldInsertPrivateContestDetails() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputPrivateContest);
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.isPrivate());
        assertEquals(List.of(SampleRepositoryProvider.TURIST_1, SampleRepositoryProvider.TURIST_2, SampleRepositoryProvider.TURIST_3),
                insertedContest.getParticipants());
    }

    @Test
    void shouldInsertGeoLocatableContestDetails() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputGeoLocatableContest);
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.hasGeoLocation());
        assertEquals(SampleRepositoryProvider.PIAZZA_LIBERTA, insertedContest.getGeoLocation());
    }

    @Test
    void shouldInsertPrivateGeoLocatableContestDetails() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputPrivateGeoLocatableContest);
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.isPrivate());
        assertTrue(insertedContest.hasGeoLocation());
        assertEquals(SampleRepositoryProvider.PIAZZA_LIBERTA, insertedContest.getGeoLocation());
        assertEquals(List.of(SampleRepositoryProvider.TURIST_1, SampleRepositoryProvider.TURIST_2, SampleRepositoryProvider.TURIST_3),
                insertedContest.getParticipants());
    }

    @Test
    void shouldNotInsertContestWithMissingInfo() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        contestInsertionHandler.insertName(sampleInputPrivateGeoLocatableContest.name());
        contestInsertionHandler.insertTopic(sampleInputPrivateGeoLocatableContest.topic());

        assertThrows(IllegalStateException.class, contestInsertionHandler::insertContest);
    }

    @Test
    void shouldRemoveParticipant() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputPrivateContest);

        contestInsertionHandler.removeParticipant(SampleRepositoryProvider.TURIST_1.getID());
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.isPrivate());
        assertEquals(List.of(SampleRepositoryProvider.TURIST_2, SampleRepositoryProvider.TURIST_3),
                insertedContest.getParticipants());
    }

    @Test
    void shouldRemoveGeoLocation() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputGeoLocatableContest);

        contestInsertionHandler.removeGeoLocation();
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertFalse(insertedContest.hasGeoLocation());
    }

    @Test
    void shouldNotInsertContestWithInvalidDates1() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        assertThrows(IllegalArgumentException.class, () ->
                contestInsertionHandler.insertStartDate(new Date(100, 1, 1)));
    }

    @Test
    void shouldNotInsertContestWithInvalidDates2() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        contestInsertionHandler.insertStartDate(sampleInputBaseContest.startDate());
        contestInsertionHandler.insertVotingStartDate(sampleInputBaseContest.votingStartDate());
        assertThrows(IllegalArgumentException.class, () ->
                contestInsertionHandler.insertEndDate(sampleInputBaseContest.startDate()));
    }

    @Test
    void shouldNotAddPartecipantIfNotPrivate() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());

        assertThrows(IllegalStateException.class, () ->
                contestInsertionHandler.addParticipant(SampleRepositoryProvider.TURIST_1.getID()));
    }

    @Test
    void shouldInsertBaseContest() {
        long contestID = ContestInsertionHandler.insertContest(SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(), sampleInputBaseContest);

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertEquals(sampleInputBaseContest.name(), insertedContest.getName());
        assertEquals(sampleInputBaseContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputBaseContest.rules(), insertedContest.getRules());
        assertEquals(sampleInputBaseContest.startDate(), insertedContest.getStartDate());
        assertEquals(sampleInputBaseContest.votingStartDate(), insertedContest.getVotingStartDate());
        assertEquals(sampleInputBaseContest.endDate(), insertedContest.getEndDate());
        assertEquals(sampleInputBaseContest.isPrivate(), insertedContest.isPrivate());
        assertEquals(SampleRepositoryProvider.ENTERTAINER_MACERATA, insertedContest.getEntertainer());
    }

    @Test
    void shouldInsertPrivateContest() {
        long contestID = ContestInsertionHandler.insertContest(SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(), sampleInputPrivateContest);

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.isPrivate());
        assertEquals(List.of(SampleRepositoryProvider.TURIST_1, SampleRepositoryProvider.TURIST_2, SampleRepositoryProvider.TURIST_3),
                insertedContest.getParticipants());
    }

    @Test
    void shouldInsertGeoLocatableContest() {
        long contestID = ContestInsertionHandler.insertContest(SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(), sampleInputGeoLocatableContest);

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.hasGeoLocation());
        assertEquals(SampleRepositoryProvider.PIAZZA_LIBERTA, insertedContest.getGeoLocation());
    }

    @Test
    void shouldInsertPrivateGeoLocatableContest() {
        long contestID = ContestInsertionHandler.insertContest(SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(), sampleInputPrivateGeoLocatableContest);

        assertEquals(3, SampleRepositoryProvider.MACERATA.getContests().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertTrue(insertedContest.isPrivate());
        assertTrue(insertedContest.hasGeoLocation());
        assertEquals(SampleRepositoryProvider.PIAZZA_LIBERTA, insertedContest.getGeoLocation());
        assertEquals(List.of(SampleRepositoryProvider.TURIST_1, SampleRepositoryProvider.TURIST_2, SampleRepositoryProvider.TURIST_3),
                insertedContest.getParticipants());
    }

    @Test
    void shouldSendNotifications() {
        ContestInsertionHandler contestInsertionHandler = new ContestInsertionHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(), SampleRepositoryProvider.MACERATA.getID());
        insertDetailsFromContestIF(contestInsertionHandler, sampleInputPrivateContest);
        long contestID = contestInsertionHandler.insertContest();

        assertEquals(1, SampleRepositoryProvider.TURIST_1.getNotifications().size());
        assertEquals(1, SampleRepositoryProvider.TURIST_2.getNotifications().size());
        assertEquals(1, SampleRepositoryProvider.TURIST_3.getNotifications().size());

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertEquals(insertedContest, SampleRepositoryProvider.TURIST_1.getNotifications().get(0).visualizable());
        assertEquals(insertedContest, SampleRepositoryProvider.TURIST_2.getNotifications().get(0).visualizable());
        assertEquals(insertedContest, SampleRepositoryProvider.TURIST_3.getNotifications().get(0).visualizable());

        assertEquals(ContestInsertionHandler.INVITATION_MESSAGE,
                SampleRepositoryProvider.TURIST_1.getNotifications().get(0).message());
    }

    private void insertDetailsFromContestIF(ContestInsertionHandler contestInsertionHandler, ContestIF contestIF) {
        contestInsertionHandler.insertName(contestIF.name());
        contestInsertionHandler.insertTopic(contestIF.topic());
        contestInsertionHandler.insertRules(contestIF.rules());
        contestInsertionHandler.insertStartDate(contestIF.startDate());
        contestInsertionHandler.insertVotingStartDate(contestIF.votingStartDate());
        contestInsertionHandler.insertEndDate(contestIF.endDate());
        contestInsertionHandler.insertVisibility(contestIF.isPrivate());
        if (contestIF.isPrivate()) {
            contestIF.userIDs().forEach(contestInsertionHandler::addParticipant);
        }
        if (contestIF.geoLocatableID() != null) {
            contestInsertionHandler.insertGeoLocation(contestIF.geoLocatableID());
        }

    }
}