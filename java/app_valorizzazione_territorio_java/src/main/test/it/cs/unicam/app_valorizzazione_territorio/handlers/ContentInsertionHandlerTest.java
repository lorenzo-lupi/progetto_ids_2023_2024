package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestRequest;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContentInsertionHandlerTest {

    private static final ContentIF sampleContent = new ContentIF(
            "sample description",
            new ArrayList<>()
    );

    @BeforeEach
    void setUpRepositories() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void shouldCreateContestContent() {
        ContestContent content = ContentInsertionHandler.createContent(
                new ContestContentBuilder(SampleRepositoryProvider.CONCORSO_FOTO_2024, SampleRepositoryProvider.TURIST_1),
                sampleContent);

        assertEquals(content.getDescription(), sampleContent.description());
        assertEquals(0, content.getFiles().size());
        assertEquals(SampleRepositoryProvider.TURIST_1 , content.getUser());
    }

    @Test
    void shouldCreatePointOfInterestContent() {
        PointOfInterestContent content = ContentInsertionHandler.createContent(
                new PointOfInterestContentBuilder(
                        (PointOfInterest) SampleRepositoryProvider.CORSA_SPADA,
                        SampleRepositoryProvider.TURIST_1), sampleContent);

        assertEquals(content.getDescription(), sampleContent.description());
        assertEquals(0, content.getFiles().size());
        assertEquals(SampleRepositoryProvider.TURIST_1 , content.getUser());
    }

    @Test
    void shouldInsertContestContent() {
        long contentID = ContestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleContent);

        assertEquals(3, SampleRepositoryProvider.CONCORSO_FOTO_2024.getContents().size());
        assertTrue(SampleRepositoryProvider.CONCORSO_FOTO_2024.getContents().contains(
                MunicipalityRepository.getInstance().getContentByID(contentID)));
    }

    @Test
    void shouldInsertPointOfInterestContent() {
        long contentID = PointOfInterestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        assertEquals(2, ((PointOfInterest)SampleRepositoryProvider.CORSA_SPADA).getContents().size());
        assertTrue(((PointOfInterest)SampleRepositoryProvider.CORSA_SPADA).getContents().contains(
                MunicipalityRepository.getInstance().getContentByID(contentID)));
    }

    @Test
    void shouldCreateRequestAfterContestContentInsertion() {
        long contentID = ContestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleContent);

        List<ContestRequest> contestRequests = ApprovalRequestRepository.getInstance().getAllContestRequests()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CONCORSO_FOTO_2024.getEntertainer()))
                .toList();

        assertEquals(contestRequests.size(), 1);
        assertEquals(contestRequests.get(0).getItem().getID(), contentID);
        assertEquals(contestRequests.get(0).getSender(), SampleRepositoryProvider.TURIST_1);
    }

    @Test
    void shouldCreateRequestAfterPointOfInterestContentInsertion() {
        long contentID = PointOfInterestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request> requests = ApprovalRequestRepository.getInstance().getItemStream()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(2, requests.size());
        assertEquals(((Identifiable)requests.get(1).getItem()).getID(), contentID);
        assertEquals(requests.get(1).getSender(), SampleRepositoryProvider.TURIST_1);
    }

    @Test
    void shouldNotCreateRequestAfterPointOfInterestContentInsertion() {
        long contentID = PointOfInterestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request> requests = ApprovalRequestRepository.getInstance().getItemStream()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(1, requests.size());
        assertEquals(MunicipalityRepository.getInstance().getContentByID(contentID).getApprovalStatus(),
                ApprovalStatusEnum.APPROVED);
    }

}