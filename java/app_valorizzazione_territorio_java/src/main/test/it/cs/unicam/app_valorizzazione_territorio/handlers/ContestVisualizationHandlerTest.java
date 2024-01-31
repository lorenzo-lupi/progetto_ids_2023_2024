package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.ContestStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContestVisualizationHandlerTest {

    @BeforeAll
    static void setUpRepository() {
        SampleRepositoryProvider.setUpMunicipalitiesRepository();
        SampleRepositoryProvider.setUpUsersRepository();
    }

    @Test
    void shouldViewAllContestsWithPrivate() {
        List<ContestSOF> contests = ContestVisualizationHandler.viewAllContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        assertEquals(2, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat(),
                        SampleRepositoryProvider.CONCORSO_PITTURA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewAllContestsWithoutPrivate() {
        List<ContestSOF> contests = ContestVisualizationHandler.viewAllContests(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContest() {
        ContestDOF contest = ContestVisualizationHandler.viewContest(
                SampleRepositoryProvider.CAMERINO.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID());

        assertEquals(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getDetailedFormat(), contest);
    }

    @Test
    void shouldNotViewContest() {
        assertThrows(IllegalArgumentException.class, () -> ContestVisualizationHandler.viewContest(
                SampleRepositoryProvider.MACERATA.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getID()));
    }

    @Test
    void shouldViewContestWithNoFilters() {
        List<ContestSOF> contests = ContestVisualizationHandler.viewFilteredContests(
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
        List<ContestSOF> contests = ContestVisualizationHandler.viewFilteredContests(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.MACERATA.getID(),
                List.of(new SearchFilter(Parameter.CONTEST_STATUS.toString(), "EQUALS", ContestStatusEnum.OPEN)));

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_2024.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestWithMoreFilters() {
        List<ContestSOF> contests = ContestVisualizationHandler.viewFilteredContests(
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
        ContestDOF contest = ContestVisualizationHandler.viewContestFromRepository(
                SampleRepositoryProvider.CONCORSO_PITTURA.getID());

        assertEquals(SampleRepositoryProvider.CONCORSO_PITTURA.getDetailedFormat(), contest);
    }

    @Test
    void shouldNotViewContestFromRepository() {
        assertThrows(IllegalArgumentException.class, () -> ContestVisualizationHandler.viewContestFromRepository(
                MunicipalityRepository.getInstance().getNextContestID()));
    }

    @Test
    void shouldViewAllContestsInstance() {
        ContestVisualizationHandler contestVisualizationHandler = new ContestVisualizationHandler(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        List<ContestSOF> contests = contestVisualizationHandler.viewAllContests();

        assertEquals(2, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat(),
                        SampleRepositoryProvider.CONCORSO_PITTURA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

    @Test
    void shouldViewContestInstance() {
        ContestVisualizationHandler contestVisualizationHandler = new ContestVisualizationHandler(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        ContestDOF contest = contestVisualizationHandler.viewContest(SampleRepositoryProvider.CONCORSO_PITTURA.getID());

        assertEquals(SampleRepositoryProvider.CONCORSO_PITTURA.getDetailedFormat(), contest);
    }

    @Test
    void shouldViewContestsThatPermitUser() {
        ContestVisualizationHandler contestVisualizationHandler = new ContestVisualizationHandler(
                SampleRepositoryProvider.ENTERTAINER_MACERATA.getID(),
                SampleRepositoryProvider.CAMERINO.getID());

        contestVisualizationHandler.startSearch();
        contestVisualizationHandler.setSearchCriterion(new SearchFilter(Parameter.CONTEST_TOPIC.toString(),
                "CONTAINS", "Concorso"));
        List<ContestSOF> contests = contestVisualizationHandler.getSearchResult();

        assertEquals(1, contests.size());
        assertEquals(Set.of(SampleRepositoryProvider.CONCORSO_FOTO_PIZZA.getSynthesizedFormat()),
                Set.copyOf(contests));
    }

}