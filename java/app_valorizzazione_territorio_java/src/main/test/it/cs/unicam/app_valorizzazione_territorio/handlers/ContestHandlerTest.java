package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.VotedContentOF;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.*;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class,
        ContentHandler.class, ContestHandler.class})
@DataJpaTest
public class ContestHandlerTest {
    @Autowired
    ContentHandler contentHandler;
    @Autowired
    ContestHandler contestHandler;
    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    MunicipalityJpaRepository municipalityRepository;
    @Autowired
    ContentJpaRepository contentRepository;
    @Autowired
    ContestJpaRepository contestRepository;
    @Autowired
    RequestJpaRepository requestRepository;
    @Autowired
    UserJpaRepository userRepository;

    ContestIF sampleInputBaseContest ;

    ContestIF sampleInputPrivateContest ;

    ContestIF sampleInputGeoLocatableContest;

    ContestIF sampleInputPrivateGeoLocatableContest ;
    @BeforeEach
    void setUpRepository() {
        sampleRepositoryProvider.setUpAllRepositories();
        sampleInputBaseContest = new ContestIF(
                "Sample Contest",
                "Sample Topic",
                "Sample rules",
                false,
                null,
                null,
                new Date(124, 10, 10),
                new Date(124, 10, 30),
                new Date(124, 11, 10));

        sampleInputPrivateContest = new ContestIF(
                "Sample Contest",
                "Sample Topic",
                "Sample rules",
                true,
                List.of(sampleRepositoryProvider.TURIST_1.getID(), sampleRepositoryProvider.TURIST_2.getID(), sampleRepositoryProvider.TURIST_3.getID()),
                null,
                new Date(124, 10, 10),
                new Date(124, 10, 30),
                new Date(124, 11, 10));

        sampleInputGeoLocatableContest = new ContestIF(
                "Sample Contest",
                "Sample Topic",
                "Sample rules",
                false,
                null,
                sampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
                new Date(124, 10, 10),
                new Date(124, 10, 30),
                new Date(124, 11, 10));

        sampleInputPrivateGeoLocatableContest = new ContestIF(
                "Sample Contest",
                "Sample Topic",
                "Sample rules",
                true,
                List.of(sampleRepositoryProvider.TURIST_1.getID(), sampleRepositoryProvider.TURIST_2.getID(), sampleRepositoryProvider.TURIST_3.getID()),
                sampleRepositoryProvider.PIAZZA_LIBERTA.getID(),
                new Date(124, 10, 10),
                new Date(124, 10, 30),
                new Date(124, 11, 10));
    }

    @AfterEach
    void clearRepository() {
        sampleRepositoryProvider.clearAllRepositories();
    }
    @Test
    void shouldViewAllContestsWithPrivate() {
        List<ContestOF> contests = contestHandler.viewAllContests(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CAMERINO.getID());

        assertEquals(2, contests.size());

        assertEquals(Set.of(sampleRepositoryProvider.CONCORSO_FOTO_PIZZA,
                                sampleRepositoryProvider.CONCORSO_PITTURA).stream()
                        .map(Contest::getOutputFormat).map(ContestOF::ID).collect(Collectors.toSet()),
                contests.stream().map(ContestOF::ID).collect(Collectors.toSet()));
    }

    @Test
    void shouldViewAllContestsWithoutPrivate() {
        List<ContestOF> contests = contestHandler.viewAllContests(
                sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                sampleRepositoryProvider.CAMERINO.getID());

        assertEquals(1, contests.size());
        ContestOF contestOF = sampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getOutputFormat();
        assertEquals(contestOF.ID(), contests.get(0).ID());
        assertEquals(contestOF.name(), contests.get(0).name());
    }

    @Test
    void shouldViewContest() {
        ContestOF contest = contestHandler.viewContest(
                sampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getBaseContestId());

        ContestOF contestOF = sampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getOutputFormat();
        assertEquals(contestOF.ID(), contest.ID());
        assertEquals(contestOF.name(), contest.name());
    }

    @Test
    void shouldViewContestWithNoFilters() {
        List<ContestOF> contests = contestHandler.viewFilteredContests(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                List.of());

        assertEquals(2, contests.size());
        assertEquals(Set.of(sampleRepositoryProvider.CONCORSO_FOTO_2024.getOutputFormat(),
                        sampleRepositoryProvider.CONCORSO_FOTO_2025.getOutputFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestWithOneFilter() {
        List<ContestOF> contests = contestHandler.viewFilteredContests(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                List.of(new SearchFilter(Parameter.CONTEST_STATUS.toString(), "EQUALS", ContestStatusEnum.OPEN)));

        assertEquals(1, contests.size());
        assertEquals(Set.of(sampleRepositoryProvider.CONCORSO_FOTO_2024.getOutputFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestWithMoreFilters() {
        List<ContestOF> contests = contestHandler.viewFilteredContests(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                List.of(new SearchFilter(Parameter.CONTEST_STATUS.toString(), "EQUALS", ContestStatusEnum.PLANNED),
                        new SearchFilter(Parameter.CONTEST_TOPIC.toString(), "CONTAINS", "foto")));

        assertEquals(1, contests.size());
        assertEquals(Set.of(sampleRepositoryProvider.CONCORSO_FOTO_2025.getOutputFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestFromRepository() {
        ContestOF contest = contestHandler.viewContestFromRepository(
                sampleRepositoryProvider.CONCORSO_PITTURA.getBaseContestId());

        assertEquals(sampleRepositoryProvider.CONCORSO_PITTURA.getOutputFormat(), contest);
    }

    @Test
    void shouldNotViewContestFromRepository() {
        assertThrows(IllegalArgumentException.class, () -> contestHandler.viewContestFromRepository(0L));
    }
    @Test
    void shouldInsertBaseContest() {
        long contestID = contestHandler.insertContest(
                sampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                sampleInputBaseContest
        );

        Contest insertedContest = contestRepository.findByBaseContestIdAndValidTrue(contestID).get();
        assertNotNull(insertedContest);
        assertEquals(sampleInputBaseContest.name(), insertedContest.getName());
        assertEquals(sampleInputBaseContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputBaseContest.rules(), insertedContest.getRules());
        assertFalse(insertedContest.isPrivate());
    }

    @Test
    void shouldInsertPrivateContest() {
        long contestID = contestHandler.insertContest(
                sampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                sampleInputPrivateContest
        );

        Contest insertedContest = contestRepository.findById(contestID).get();
        assertNotNull(insertedContest);
        assertEquals(sampleInputPrivateContest.name(), insertedContest.getName());
        assertEquals(sampleInputPrivateContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputPrivateContest.rules(), insertedContest.getRules());
        assertTrue(insertedContest.isPrivate());
    }

    @Test
    void shouldInsertGeoLocatableContest() {
        long contestID = contestHandler.insertContest(
                sampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                sampleInputGeoLocatableContest
        );

        Contest insertedContest = contestRepository.findById(contestID).get();
        assertNotNull(insertedContest);
        assertEquals(sampleInputGeoLocatableContest.name(), insertedContest.getName());
        assertEquals(sampleInputGeoLocatableContest.topic(), insertedContest.getTopic());
        assertEquals(sampleInputGeoLocatableContest.rules(), insertedContest.getRules());
        assertFalse(insertedContest.isPrivate());
        assertNotNull(insertedContest.getGeoLocation());
    }

    @Test
    void shouldInsertPrivateGeoLocatableContest() {
        long contestID = contestHandler.insertContest(
                sampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                sampleRepositoryProvider.MACERATA.getID(),
                sampleInputPrivateGeoLocatableContest
        );

        Contest insertedContest = contestRepository.findById(contestID).get();
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
                contestHandler.viewAllProposals(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                        .stream()
                        .map(Identifiable::getID)
                        .toList()
                        .containsAll(
                                List.of(sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID(),
                                        sampleRepositoryProvider.FOTO_TORRE_CIVICA.getID())));
    }

    void vote() {
        contestHandler.vote(sampleRepositoryProvider.TURIST_2.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleRepositoryProvider.FOTO_STRADE_MACERATA.getID());

        contestHandler.vote(sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleRepositoryProvider.FOTO_TORRE_CIVICA.getID());
    }


    @Test
    void testVote() {
        vote();
        assertTrue(contestHandler
                .viewAllProposals(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentOF::votes)
                .allMatch(votes -> votes == 1));
        removeVote();
        assertTrue(contestHandler
                .viewAllProposals(sampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentOF::votes)
                .allMatch(votes -> votes == 0));
    }


    void removeVote() {
        contestHandler.removeVote(sampleRepositoryProvider.TURIST_2.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID());
        contestHandler.removeVote(sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID());

    }

}
