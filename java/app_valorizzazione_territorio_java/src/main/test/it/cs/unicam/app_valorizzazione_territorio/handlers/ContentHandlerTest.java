package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContentOF;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.ContestRequest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ContentJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class,
        ContentHandler.class, ContestHandler.class})
@DataJpaTest
class ContentHandlerTest {
    @Value("${fileResources.path}")
    private String filePath;
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
    RequestJpaRepository requestRepository;
    @Autowired
    UserJpaRepository userRepository;

    @BeforeEach
    void setUpRepositories() {
        sampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    void clearRepositories() {
        sampleRepositoryProvider.clearAllRepositories();
    }


    void viewAllContents(PointOfInterest geoLocatable){
        assertEquals(contentHandler.viewApprovedContents(geoLocatable.getID())
                .stream()
                .map(c  -> contentHandler.viewContent(c.getID()))
                .map(ContentOF::getID)
                .map(id -> contentRepository.findById(id).get())
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
        ContestContent content = contentHandler.createContent(
                new ContestContentBuilder(sampleRepositoryProvider.CONCORSO_FOTO_2024),
                sampleRepositoryProvider.TURIST_1, sampleContent, filePath);

        assertEquals(content.getDescription(), sampleContent.description());
        assertEquals(0, content.getFiles().size());
        assertEquals(sampleRepositoryProvider.TURIST_1 , content.getUser());
    }

    @Test
    void shouldCreatePointOfInterestContent() {
        PointOfInterestContent content = contentHandler.createContent(
                new PointOfInterestContentBuilder(
                        (PointOfInterest) sampleRepositoryProvider.CORSA_SPADA),
                        sampleRepositoryProvider.TURIST_1, sampleContent, filePath);

        assertEquals(content.getDescription(), sampleContent.description());
        assertEquals(0, content.getFiles().size());
        assertEquals(sampleRepositoryProvider.TURIST_1 , content.getUser());
    }

    @Test
    void shouldInsertContestContent() {
        long contentID = contestHandler.insertContent(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleContent);

        assertEquals(3, sampleRepositoryProvider.CONCORSO_FOTO_2024.getContents().size());
        assertTrue(sampleRepositoryProvider.CONCORSO_FOTO_2024.getContents().contains(
                contentRepository.findById(contentID).get()));
    }

    @Test
    void shouldInsertPointOfInterestContent() {
        long contentID = contentHandler.insertContent(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        assertEquals(2, ((PointOfInterest)sampleRepositoryProvider.CORSA_SPADA).getContents().size());
        assertTrue(((PointOfInterest)sampleRepositoryProvider.CORSA_SPADA).getContents().contains(
                contentRepository.findById(contentID).get()));
    }

    @Test
    void shouldCreateRequestAfterContestContentInsertion() {
        long contentID = contestHandler.insertContent(
                sampleRepositoryProvider.TURIST_2.getID(),
                sampleRepositoryProvider.CONCORSO_FOTO_2024.getID(),
                sampleContent);

        List<ContestRequest> contestRequests = requestRepository.findAll()
                .stream()
                .filter(r -> r instanceof ContestRequest)
                .filter(r -> r.getItem().getID() == contentID)
                .map(r -> (ContestRequest) r)
                .toList();

        assertEquals(contestRequests.size(), 1);
        assertEquals(contestRequests.get(0).getItem().getID(), contentID);
        assertEquals(contestRequests.get(0).getSender(), sampleRepositoryProvider.TURIST_2);
    }

    @Test
    void shouldCreateRequestAfterPointOfInterestContentInsertion() {
       long contentID = contentHandler.insertContent(
                sampleRepositoryProvider.TURIST_1.getID(),
                sampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request<?>> requests = requestRepository.findAll()
                .stream()
                .filter(r -> r.canBeApprovedBy(sampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(2, requests.size());
        assertTrue(requestRepository.findAll().stream().anyMatch(r -> r.getItem().getID() == contentID));
        assertEquals(requests.get(1).getSender(), sampleRepositoryProvider.TURIST_1);
    }

    @Test
    void shouldNotCreateRequestAfterPointOfInterestContentInsertion() {
        long contentID = contentHandler.insertContent(
                sampleRepositoryProvider.CURATOR_CAMERINO.getID(),
                sampleRepositoryProvider.CORSA_SPADA.getID(),
                sampleContent);

        List<Request<?>> requests = requestRepository.findAll()
                .stream()
                .filter(r -> r.canBeApprovedBy(sampleRepositoryProvider.CURATOR_CAMERINO))
                .toList();

        assertEquals(1, requests.size());
        assertEquals(contentRepository.findById(contentID).get().getApprovalStatus(),
                ApprovalStatusEnum.APPROVED);
    }

    @Test
    void shouldSaveContent() {
        long userID = sampleRepositoryProvider.TURIST_1.getID();
        long contentID = sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID();
        contentHandler.saveContent(userID, contentID);

        assertTrue(userRepository.getByID(userID).get().getSavedContents()
                .contains(contentRepository.findById(contentID).get()));
    }

    @Test
    void shouldDeleteSavedContent() {
        long userID = sampleRepositoryProvider.TURIST_1.getID();
        long contentID = sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID();
        contentHandler.saveContent(userID, contentID);
        contentHandler.removeSavedContent(userID, contentID);

        assertFalse(userRepository.getByID(userID).get().getSavedContents()
                .contains(contentRepository.findById(contentID).get()));
    }

    @Test
    void shouldViewSavedContents() {
        long userID = sampleRepositoryProvider.TURIST_1.getID();
        long contentID = sampleRepositoryProvider.FOTO_PIAZZA_LIBERTA_1.getID();
        contentHandler.saveContent(userID, contentID);

        List<ContentOF> savedContents = contentHandler.viewSavedContents(userID);
        ContentOF content = userRepository.getByID(userID).get().getSavedContents().get(0).getOutputFormat();
        assertEquals(content, savedContents.get(0));


    }

}