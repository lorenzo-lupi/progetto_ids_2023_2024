package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUltils;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;
import java.util.Set;

import static it.cs.unicam.app_valorizzazione_territorio.handlers.utils.InsertionUtils.insertItemApprovableByContributors;

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
     * @param filters           the filters to apply
     * @return the Synthesized Format of all the contest in the point of interest corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<ContentSOF> viewFilteredContents(long pointOfInterestID, List<SearchFilter> filters) {
        return (List<ContentSOF>) SearchUltils.getFilteredItems(MunicipalityRepository.getInstance().getAllContents().toList(), filters);
    }


    /**
     * Returns the set of all the criteria available for the search.
     * @return the set of all the criteria available for the search
     */
    public static Set<String> getSearchCriteria() {
        return SearchUltils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     * @return the search parameters for the user entity
     */
    public static List<String> getParameters(){
        return List.of(Parameter.DESCRIPTION.toString(),
                Parameter.APPROVAL_STATUS.toString());
    }

    /**
     * Returns the Detailed Format of a Content corresponding to the given ID in the point of interest
     * corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param contentID         the ID of the Content to visualize
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
     * @param userID    the ID of the user who is inserting the content
     * @param poiID     the ID of the point of interest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public static long insertContent(long userID, long poiID, ContentIF contentIF) {
        User user = userRepository.getItemByID(userID);
        PointOfInterest pointOfInterest = municipalityRepository.getPointOfInterestByID(poiID);

        PointOfInterestContent content = createContent(
                new PointOfInterestContentBuilder(pointOfInterest), user, contentIF);

        insertPoiContent(content, user);
        return content.getID();
    }

    /**
     * Creates a content from the specified contentIF.
     *
     * @param builder   the builder for the content to be created
     * @param user      the user who is creating the content
     * @param contentIF the contentIF from which the content will be created
     * @param <V>       the type of the content host
     * @param <K>       the type of the content that will be hosted
     * @return the created content
     * @throws IllegalArgumentException if the builder or the contentIF are null
     */
    static <V extends ContentHost<V> & Visualizable, K extends Content<V>>
    K createContent(ContentBuilder<V, K> builder, User user, ContentIF contentIF) {
        if (builder == null)
            throw new IllegalArgumentException("Builder cannot be null");
        if (contentIF == null)
            throw new IllegalArgumentException("ContentIF cannot be null");

        builder.buildUser(user).buildDescription(contentIF.description());
        contentIF.files().forEach(builder::buildFile);
        return builder.build().getResult();
    }

    /**
     * Saves the content with the given ID for the user with the given ID.
     * If the content is already saved, the method returns false.
     *
     * @param userID    the ID of the user who is saving the content
     * @param contentID the ID of the content to save
     * @return true if the content is saved, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue") // This method is used for its side effects
    public static boolean saveContent(long userID, long contentID) {
        User user = userRepository.getItemByID(userID);
        Content<?> content = municipalityRepository.getContentByID(contentID);
        return user.addSavedContent(content);
    }

    /**
     * Removes the content with the given ID from the saved contents of the user with the given ID.
     * If the content is not saved, the method returns false.
     *
     * @param userID    the ID of the user who is removing the content
     * @param contentID the ID of the content to remove
     * @return true if the content is removed, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue") // This method is used for its side effects
    public static boolean removeSavedContent(long userID, long contentID) {
        User user = userRepository.getItemByID(userID);
        Content<?> content = municipalityRepository.getContentByID(contentID);
        return user.removeSavedContent(content);
    }

    /**
     * Returns the Synthesized Format of all the saved contents of the user with the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the saved contents of the user
     */
    public static List<ContentSOF> viewSavedContents(long userID) {
        return userRepository.getItemByID(userID)
                .getSavedContents()
                .stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    private static void insertPoiContent(PointOfInterestContent content, User user) {
        PointOfInterest pointOfInterest = content.getHost();
        insertItemApprovableByContributors(content, user, pointOfInterest.getMunicipality(), pointOfInterest::addContent);
    }

}
