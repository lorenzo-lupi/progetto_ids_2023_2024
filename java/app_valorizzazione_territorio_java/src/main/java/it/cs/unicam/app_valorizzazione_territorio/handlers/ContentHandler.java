package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.contest.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for
 * - Search and visualization of contents of points of interest
 * - Insertion of contents in points of interest
 */
public class ContentHandler {

    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();

    /**
     * Returns the Synthesized Format of all the approved contents of the point of interest
     * corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public static List<ContentSOF> viewApprovedContents(long pointOfInterestID) {
        return municipalityRepository.getPointOfInterestByID(pointOfInterestID)
                .getApprovedContents()
                .stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents of the point of interest corresponding to the given ID
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public static List<ContentSOF> viewAllContents(long pointOfInterestID) {
        return municipalityRepository.getPointOfInterestByID(pointOfInterestID)
                .getContents()
                .stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents of the point of interest corresponding to the given ID
     * that satisfy the given filters, all applied in logical and.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param filters the filters to apply
     * @return the Synthesized Format of all the contest in the point of interest corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<ContentSOF> viewFilteredContents(long pointOfInterestID, List<SearchFilter> filters) {
        return (List<ContentSOF>) SearchHandler.getFilteredItems(MunicipalityRepository.getInstance().getAllContents().toList(), filters);
    }

    /**
     * Returns the Detailed Format of a Content corresponding to the given ID in the point of interest
     * corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     * @throws IllegalArgumentException if the Content having the given ID is not found
     */
    public static ContentDOF viewContent(long pointOfInterestID, long contentID) {
        return getContent(municipalityRepository.getPointOfInterestByID(pointOfInterestID), contentID)
                .getDetailedFormat();
    }

    /**
     * Returns the Detailed Format of a Content  in the system corresponding to the given ID.
     * The content can belong to any point of interest.
     *
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     */
    public static ContentDOF viewContentFromRepository(long contentID) {
        return municipalityRepository.getContentByID(contentID).getDetailedFormat();
    }

    private static Content<PointOfInterest> getContent(PointOfInterest pointOfInterest, long contentID) {
        return pointOfInterest.getContents().stream()
                .filter(content -> content.getID() == contentID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
    }

    /**
     * Inserts the content obtained from the given contentIF in the point of interest with the given ID.
     * If the content insertion has success, an approval request may be created for the municipality of the point
     * of interest depending on the user's role.
     *
     * @param userID the ID of the user who is inserting the content
     * @param poiID the ID of the point of interest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public static long insertContent(long userID, long poiID, ContentIF contentIF){
        User user = userRepository.getItemByID(userID);
        PointOfInterest pointOfInterest = municipalityRepository.getPointOfInterestByID(poiID);

        PointOfInterestContent content = createContent(
                new PointOfInterestContentBuilder(pointOfInterest, user), contentIF);

        GeoLocatableControllerUtils.insertPoiContent(content, user);
        return content.getID();
    }

    /**
     * Creates a content from the specified contentIF.
     *
     * @param builder the builder for the content to be created
     * @param contentIF the contentIF from which the content will be created
     * @return the created content
     * @param <V> the type of the content host
     * @param <K> the type of the content that will be hosted
     * @throws IllegalArgumentException if the builder or the contentIF are null
     */
    static <V extends ContentHost<V> & Visualizable, K extends Content<V>>
    K createContent(ContentBuilder<V, K> builder, ContentIF contentIF){
        if(builder == null)
            throw new IllegalArgumentException("Builder cannot be null");
        if(contentIF == null)
            throw new IllegalArgumentException("ContentIF cannot be null");

        builder.buildDescription(contentIF.description());
        contentIF.files().forEach(builder::buildFile);
        return builder.build();
    }
}
