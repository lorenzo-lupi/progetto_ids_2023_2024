package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContentHandlerTest {

    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();
    private static final RequestRepository requestRepository = RequestRepository.getInstance();

    @BeforeEach
    void setUpRepositories() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    void viewAllContents() {
        SampleRepositoryProvider.clearAndSetUpRepositories();

        SampleRepositoryProvider
                .geoLocatables.stream()
                .filter(g -> g instanceof PointOfInterest)
                .forEach(g -> viewAllContents((PointOfInterest) g));

    }

    void viewAllContents(PointOfInterest geoLocatable){
        assertEquals(ContentHandler.viewApprovedContents(geoLocatable.getID())
                .stream()
                .map(c  -> ContentHandler.viewContentFromRepository(c.getID()))
                .map(ContentDOF::getID)
                .map(id -> municipalityRepository.getContentByID(id))
                .toList(),

                geoLocatable
                        .getApprovedContents()
                        .stream()
                        .toList()
        );
    }

    private static final ContentIF sampleContent = new ContentIF(
            "sample description",
            new ArrayList<>()
    );

    @Test
    void shouldCreateContestContent() {
        ContestContent content = ContentHandler.createContent(
                new ContestContentBuilder(SampleRepositoryProvider.CONCORSO_FOTO_2024, SampleRepositoryProvider.TURIST_1),
                sampleContent);

        assertEquals(content.getDescription(), sampleContent.description());
        assertEquals(0, content.getFiles().size());
        assertEquals(SampleRepositoryProvider.TURIST_1 , content.getUser());
    }

    @Test
    void shouldCreatePointOfInterestContent() {
        PointOfInterestContent content = ContentHandler.createContent(
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
                municipalityRepository.getContentByID(contentID)));
    }

    @Test
    void shouldInsertPointOfInterestContent() {
        long contentID = ContentHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        assertEquals(2, ((PointOfInterest)SampleRepositoryProvider.CORSA_SPADA).getContents().size());
        assertTrue(((PointOfInterest)SampleRepositoryProvider.CORSA_SPADA).getContents().contains(
                municipalityRepository.getContentByID(contentID)));
    }

    @Test
    void shouldCreateRequestAfterContestContentInsertion() {
        long contentID = ContestContentInsertionHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleContent);

        List<ContestRequest> contestRequests = requestRepository.getAllContestRequests()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CONCORSO_FOTO_2024.getEntertainer()))
                .toList();

        assertEquals(contestRequests.size(), 1);
        assertEquals(contestRequests.get(0).getItem().getID(), contentID);
        assertEquals(contestRequests.get(0).getSender(), SampleRepositoryProvider.TURIST_1);
    }

    @Test
    void shouldCreateRequestAfterPointOfInterestContentInsertion() {
        long contentID = ContentHandler.insertContent(
                SampleRepositoryProvider.TURIST_1.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request> requests = requestRepository.getItemStream()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(3, requests.size());
        assertTrue(requestRepository.getAllMunicipalityRequests().anyMatch(r -> r.getItem().getID() == contentID));
        assertEquals(requests.get(1).getSender(), SampleRepositoryProvider.TURIST_1);
    }

    @Test
    void shouldNotCreateRequestAfterPointOfInterestContentInsertion() {
        long contentID = ContentHandler.insertContent(
                SampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                SampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request> requests = requestRepository.getItemStream()
                .filter(r -> r.canBeApprovedBy(SampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(2, requests.size());
        assertEquals(municipalityRepository.getContentByID(contentID).getApprovalStatus(),
                ApprovalStatusEnum.APPROVED);
    }

}