package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContestHandlerTest {


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
    void shouldViewAllContestsWithPrivate() {
        List<ContestSOF> contests = ContestHandler.viewAllContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        assertEquals(2, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat(),
                        SampleRepositoryProvider.CONCORSO_PITTURA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewAllContestsWithoutPrivate() {
        List<ContestSOF> contests = ContestHandler.viewAllContests(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContest() {
        ContestDOF contest = ContestHandler.viewContest(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID());

        assertEquals(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getDetailedFormat(), contest);
    }

    @Test
    void shouldNotViewContest() {
        assertThrows(IllegalArgumentException.class, () -> ContestHandler.viewContest(
                SampleRepositoryProvider.MACERATA.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID()));
    }

    @Test
    void shouldViewContestWithNoFilters() {
        List<ContestSOF> contests = ContestHandler.viewFilteredContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                List.of());

        assertEquals(2, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_2024.getSynthesizedFormat(),
                        SampleRepositoryProvider.CONCORSO_FOTO_2025.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestWithOneFilter() {
        List<ContestSOF> contests = ContestHandler.viewFilteredContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                List.of(new SearchFilter(Parameter.CONTEST_STATUS.toString(), "EQUALS", ContestStatusEnum.OPEN)));

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_2024.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestWithMoreFilters() {
        List<ContestSOF> contests = ContestHandler.viewFilteredContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                List.of(new SearchFilter(Parameter.CONTEST_STATUS.toString(), "EQUALS", ContestStatusEnum.PLANNED),
                        new SearchFilter(Parameter.CONTEST_TOPIC.toString(), "CONTAINS", "foto")));

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_2025.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestFromRepository() {
        ContestDOF contest = ContestHandler.viewContestFromRepository(
                SampleRepositoryProvider.CONCORSO_PITTURA.getID());

        assertEquals(SampleRepositoryProvider.CONCORSO_PITTURA.getDetailedFormat(), contest);
    }

    @Test
    void shouldNotViewContestFromRepository() {
        assertThrows(IllegalArgumentException.class, () -> ContestHandler.viewContestFromRepository(
                MunicipalityRepository.getInstance().getNextContestID()));
    }
    @Test
    void shouldInsertBaseContest() {
        long contestID = ContestHandler.insertContest(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                sampleInputBaseContest
        );

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertNotNull(insertedContest);
        assertEquals(sampleInputBaseContest.name(), insertedContest.getName());
        assertEquals(sampleInputBaseContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputBaseContest.rules(), insertedContest.getRules());
        assertFalse(insertedContest.isPrivate());
    }

    @Test
    void shouldInsertPrivateContest() {
        long contestID = ContestHandler.insertContest(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                sampleInputPrivateContest
        );

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertNotNull(insertedContest);
        assertEquals(sampleInputPrivateContest.name(), insertedContest.getName());
        assertEquals(sampleInputPrivateContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputPrivateContest.rules(), insertedContest.getRules());
        assertTrue(insertedContest.isPrivate());
    }

    @Test
    void shouldInsertGeoLocatableContest() {
        long contestID = ContestHandler.insertContest(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                sampleInputGeoLocatableContest
        );

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertNotNull(insertedContest);
        assertEquals(sampleInputGeoLocatableContest.name(), insertedContest.getName());
        assertEquals(sampleInputGeoLocatableContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputGeoLocatableContest.rules(), insertedContest.getRules());
        assertFalse(insertedContest.isPrivate());
        assertNotNull(insertedContest.getGeoLocation());
    }

    @Test
    void shouldInsertPrivateGeoLocatableContest() {
        long contestID = ContestHandler.insertContest(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                sampleInputPrivateGeoLocatableContest
        );

        Contest insertedContest = MunicipalityRepository.getInstance().getContestByID(contestID);
        assertNotNull(insertedContest);
        assertEquals(sampleInputPrivateGeoLocatableContest.name(), insertedContest.getName());
        assertEquals(sampleInputPrivateGeoLocatableContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputPrivateGeoLocatableContest.rules(), insertedContest.getRules());
        assertTrue(insertedContest.isPrivate());
        assertNotNull(insertedContest.getGeoLocation());
    }


    @Test
    void viewAllProposals() {
        assertTrue(
                ContestHandler.viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                        .stream()
                        .map(Identifiable::getID)
                        .toList()
                        .containsAll(
                                List.of(SampleRepositoryProvider.FOTO_STRADE_MACERATA.getID(),
                                        SampleRepositoryProvider.FOTO_TORRE_CIVICA.getID())));
    }

    void vote() {
        ContestHandler.vote(SampleRepositoryProvider.TURIST_2.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                SampleRepositoryProvider.FOTO_STRADE_MACERATA.getID());

        ContestHandler.vote(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                SampleRepositoryProvider.FOTO_TORRE_CIVICA.getID());
    }


    @Test
    void testVote() {
        vote();
        assertTrue(ContestHandler
                .viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentSOF::votes)
                .allMatch(votes -> votes == 1));
        removeVote();
        assertTrue(ContestHandler
                .viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentSOF::votes)
                .allMatch(votes -> votes == 0));
    }


    static void removeVote() {
        ContestHandler.removeVote(SampleRepositoryProvider.TURIST_2.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID());
        ContestHandler.removeVote(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID());

    }
}
