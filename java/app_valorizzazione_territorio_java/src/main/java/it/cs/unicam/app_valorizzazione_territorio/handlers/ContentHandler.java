package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContentOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.InsertionUtils;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ContentJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This class represents a handler for
 * - Search and visualization of contents of points of interest
 * - Insertion of contents in points of interest
 */
@Service
public class ContentHandler {
    @Value("${fileResources.path}")
    private String filePath;
    private final InsertionUtils insertionUtils;
    private final MunicipalityJpaRepository municipalityRepository;
    private final GeoLocatableJpaRepository geoLocatableRepository;
    private final UserJpaRepository userRepository;
    private final ContentJpaRepository contentRepository;

    @Autowired
    public ContentHandler(MunicipalityJpaRepository municipalityRepository,
                          GeoLocatableJpaRepository geoLocatableJpaRepository,
                          UserJpaRepository userRepository,
                          ContentJpaRepository contentJpaRepository,
                          InsertionUtils insertionUtils) {
        this.municipalityRepository = municipalityRepository;
        this.geoLocatableRepository = geoLocatableJpaRepository;
        this.userRepository = userRepository;
        this.contentRepository = contentJpaRepository;
        this.insertionUtils = insertionUtils;
    }


    /**
     * Returns all the approved contents of the point of interest corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public List<ContentOF> viewApprovedContents(long pointOfInterestID) {
        return getPointOfInterestContents(pointOfInterestID)
                .filter(Content::isApproved)
                .map(Content::getOutputFormat)
                .toList();
    }

    /**
     * Returns all the contents of the point of interest corresponding to the given ID
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public List<ContentOF> viewAllContents(long pointOfInterestID) {
        return getPointOfInterestContents(pointOfInterestID)
                .map(Content::getOutputFormat)
                .toList();
    }

    /**
     * Returns the set of all the criteria available for the search.
     *
     * @return the set of all the criteria available for the search
     */
    public Set<String> getSearchCriteria() {
        return SearchUtils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     *
     * @return the search parameters for the user entity
     */
    public Set<String> getSearchParameters() {
        return Set.of(
                Parameter.DESCRIPTION.toString(),
                Parameter.APPROVAL_STATUS.toString()
        );
    }

    /**
     * Returns all the contents of the point of interest corresponding to the given ID
     * that satisfy the given filters, all applied in logical and.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param filters           the filters to apply
     * @return all the contents in the point of interest corresponding to the given filters
     * @throws IllegalArgumentException if the point of interest is not found
     */
    @SuppressWarnings("unchecked")
    public List<ContentOF> viewFilteredContents(long pointOfInterestID, List<SearchFilter> filters) {
        return (List<ContentOF>) SearchUtils
                .getFilteredItems(getPointOfInterestContents(pointOfInterestID).toList(),
                        filters);
    }

    /**
     * Returns the Detailed Format of a Content  in the system corresponding to the given ID.
     * The content can belong to any point of interest or contest.
     *
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     * @throws IllegalArgumentException if the Content having the given ID is not found
     */
    public ContentOF viewContent(long contentID) {
        return getContentByID(contentID).getOutputFormat();
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
    public long insertContent(long userID, long poiID, ContentIF contentIF) {
        User user = getUserByID(userID);
        PointOfInterest pointOfInterest = getPointOfInterestByID(poiID);

        PointOfInterestContent content = contentRepository.save(createContent(
                new PointOfInterestContentBuilder(pointOfInterest), user, contentIF, filePath));

        insertPoiContent(content, user);
        contentRepository.save(content);
        return content.getID();
    }

    /**
     * Creates a content from the specified contentIF.
     *
     * @param builder   the builder for the content to be created
     * @param filePath  the path to the file resources
     * @param user      the user who is creating the content
     * @param contentIF the contentIF from which the content will be created
     * @param <V>       the type of the content host
     * @param <K>       the type of the content that will be hosted
     * @return the created content
     * @throws IllegalArgumentException if the builder or the contentIF are null
     */
    public static <V extends ContentHost<V> & Visualizable, K extends Content<V>> K createContent(ContentBuilder<V, K> builder,
                                                                                                  User user,
                                                                                                  ContentIF contentIF,
                                                                                                  String filePath) {
        if (builder == null)
            throw new IllegalArgumentException("Builder cannot be null");
        if (contentIF == null)
            throw new IllegalArgumentException("ContentIF cannot be null");

        builder.buildUser(user).buildDescription(contentIF.description());
        contentIF.files().stream()
                .map(fileName -> new File(filePath + fileName))
                .forEach(builder::buildFile);
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
    public boolean saveContent(long userID, long contentID) {
        User user = getUserByID(userID);
        Content<?> content = getContentByID(contentID);
        if (user.addSavedContent(content)) {
            userRepository.save(user);
            return true;
        }
        return false;
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
    public boolean removeSavedContent(long userID, long contentID) {
        User user = getUserByID(userID);
        Content<?> content = getContentByID(contentID);
        if (user.removeSavedContent(content)) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Returns all the saved contents of the user with the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the saved contents of the user
     */
    public List<ContentOF> viewSavedContents(long userID) {
        User user = getUserByID(userID);
        return user.getSavedContents()
                .stream()
                .map(Content::getOutputFormat)
                .toList();
    }

    private void insertPoiContent(PointOfInterestContent content, User user) {
        PointOfInterest pointOfInterest = content.getHost();
        insertionUtils.insertItemApprovableByContributors(content,
                user,
                pointOfInterest.getMunicipality(),
                pointOfInterest::addContent);
    }

    private Stream<PointOfInterestContent> getPointOfInterestContents(long pointOfInterestID) {
        Optional<GeoLocatable> pointOfInterest = geoLocatableRepository.findById(pointOfInterestID);
        if (pointOfInterest.isEmpty())
            throw new IllegalArgumentException("Point of interest not found");
        if (!(pointOfInterest.get() instanceof PointOfInterest poi))
            throw new IllegalArgumentException("The given ID does not correspond to a point of interest");

        return poi
                .getContents()
                .stream();
    }

    private Content<?> getContentByID(long contentID) {
        Optional<Content<?>> content = contentRepository.findById(contentID);
        if (content.isEmpty())
            throw new IllegalArgumentException("Content not found");
        return content.get();
    }

    private PointOfInterest getPointOfInterestByID(long pointOfInterestID) {
        Optional<GeoLocatable> pointOfInterest = geoLocatableRepository.findById(pointOfInterestID);
        if (pointOfInterest.isEmpty())
            throw new IllegalArgumentException("Point of interest not found");
        if (!(pointOfInterest.get() instanceof PointOfInterest poi))
            throw new IllegalArgumentException("The given ID does not correspond to a point of interest");
        return poi;
    }

    private User getUserByID(long userID) {
        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return user.get();
    }
}
