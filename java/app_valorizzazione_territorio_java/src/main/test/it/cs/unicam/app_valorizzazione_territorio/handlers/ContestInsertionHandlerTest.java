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
    void shouldInsertBaseContest() {
        long contestID = ContestInsertionHandler.insertContest(
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
        long contestID = ContestInsertionHandler.insertContest(
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
        long contestID = ContestInsertionHandler.insertContest(
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
        long contestID = ContestInsertionHandler.insertContest(
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




}