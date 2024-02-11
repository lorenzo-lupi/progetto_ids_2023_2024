package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContestContentVoteHandlerTest {
    /**
     * CONCORSO_FOTO_2024.getProposalRequests().proposeContent((ContestContent) FOTO_STRADE_MACERATA);
     * FOTO_STRADE_MACERATA.approve();
     * CONCORSO_FOTO_2024.getProposalRequests().proposeContent((ContestContent) FOTO_TORRE_CIVICA);
     * FOTO_TORRE_CIVICA.approve();
     */

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void viewAllProposals() {
        assertTrue(
                ContestContentVoteHandler.viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                        .stream()
                        .map(Identifiable::getID)
                        .toList()
                        .containsAll(
                                List.of(SampleRepositoryProvider.FOTO_STRADE_MACERATA.getID(),
                                        SampleRepositoryProvider.FOTO_TORRE_CIVICA.getID())));
    }

    void vote() {
        ContestContentVoteHandler.vote(SampleRepositoryProvider.TURIST_2.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                SampleRepositoryProvider.FOTO_STRADE_MACERATA.getID());

        ContestContentVoteHandler.vote(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                SampleRepositoryProvider.FOTO_TORRE_CIVICA.getID());
    }


    @Test
    void testVote() {
        vote();
        assertTrue(ContestContentVoteHandler
                .viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentSOF::votes)
                .allMatch(votes -> votes == 1));
        removeVote();
        assertTrue(ContestContentVoteHandler
                .viewAllProposals(SampleRepositoryProvider.CONCORSO_FOTO_2024.getID())
                .stream()
                .map(VotedContentSOF::votes)
                .allMatch(votes -> votes == 0));
    }


    static void removeVote() {
        ContestContentVoteHandler.removeVote(SampleRepositoryProvider.TURIST_2.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID());
        ContestContentVoteHandler.removeVote(SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID());

    }
}