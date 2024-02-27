package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.*;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.VotedContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class is used to handle the contests.
 * It provides methods to manage contests.
 */
@Service
public class ContestHandler {
    @Value("${fileResources.path}")
    private String filePath;
    private final UserJpaRepository userRepository;
    private final MunicipalityJpaRepository municipalityRepository;
    private final ContentJpaRepository contentRepository;
    private final ContestJpaRepository contestRepository;
    private final GeoLocatableJpaRepository geoLocatableRepository;
    private final RequestJpaRepository requestRepository;
    private final NotificationJpaRepository notificationRepository;

    public final String INVITATION_MESSAGE = "You have been invited to participate in a contest";

    @Autowired
    public ContestHandler(UserJpaRepository userRepository,
                          MunicipalityJpaRepository municipalityRepository,
                          ContentJpaRepository contentRepository,
                          ContestJpaRepository contestRepository,
                          GeoLocatableJpaRepository geoLocatableRepository,
                          RequestJpaRepository requestRepository,
                          NotificationJpaRepository notificationRepository) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.contentRepository = contentRepository;
        this.contestRepository = contestRepository;
        this.geoLocatableRepository = geoLocatableRepository;
        this.requestRepository = requestRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Inserts the content obtained from the given contentIF in the contest with the given ID.
     * If the content insertion has success, an approval request for the entertainer of the contest is created.
     *
     * @param userID    the ID of the user who is inserting the content
     * @param contestID the ID of the contest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public long insertContent(long userID, long contestID, ContentIF contentIF) {
        User user = getUserByID(userID);
        Contest contest = getContestByID(contestID);

        ContestContent content = contentRepository.saveAndFlush(ContentHandler.createContent(
                new ContestContentBuilder(contest), user, contentIF, filePath));

        contest.getProposalRegister().proposeContent(content);
        contestRepository.saveAndFlush(contest);
        requestRepository.saveAndFlush(RequestFactory.getApprovalRequest(content));
        return content.getID();
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest corresponding to the given ID
     * combined with their number of votes .
     *
     * @param contestID the ID of the contest
     * @return the Synthesized Format of all the proposals
     */
    public List<VotedContentOF> viewAllProposals(long contestID) {
        return getContestByID(contestID)
                .getProposalRegister().getProposals().stream()
                .map(VotedContent::getOutputFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest corresponding to the given ID
     * combined with their number of votes that satisfy the given filters, all applied in logical and.
     *
     * @param contestID the ID of the contest
     * @param filters   the filters to apply
     * @return the Synthesized Format of all the suitable proposals
     */
    @SuppressWarnings("unchecked")
    public List<VotedContentOF> viewFilteredProposals(long contestID, List<SearchFilter> filters) {
        return (List<VotedContentOF>) SearchUtils.getFilteredItems(
                getContestByID(contestID).getProposalRegister().getProposals(),
                filters);
    }


    /**
     * Returns the detailed format of the content corresponding to the given ID proposed for the
     * contest corresponding to the given ID combined with its number of votes.
     *
     * @param contestID the ID of the contest
     * @param contentID the ID of the content
     * @return the detailed format of the proposal
     * @throws IllegalArgumentException if the content is not found in the contest
     */
    public VotedContentOF viewProposal(long contestID, long contentID) {
        return getVotedContent(getContestByID(contestID),
                contentID).getOutputFormat();
    }

    /**
     * Adds a vote to the content corresponding to the given ID from the user corresponding to the given ID.
     *
     * @param userID    the ID of the user
     * @param contestID the ID of the contest
     * @param contentID the ID of the content to be voted
     * @throws IllegalArgumentException if the user or the contest is not found or if the content
     *                                  is not found in the contest or if the user has already voted in the contest
     */
    public void vote(long userID, long contestID, long contentID) {
        User user = getUserByID(userID);
        Contest contest = getContestByID(contestID);
        ContestContent content = getContestContentByID(contentID);
        contest.getProposalRegister().addVote(content, user);
        contestRepository.saveAndFlush(contest);
        contentRepository.saveAndFlush(content);

    }

    /**
     * Removes the vote on some content of the contest corresponding to the given ID previously
     * given by the user corresponding to the given ID, if any.
     *
     * @param userID    the ID of the user
     * @param contestID the ID of the contest
     * @throws IllegalArgumentException if the user or the contest is not found or if the user
     *                                  has not voted for any content in the contest
     */
    public void removeVote(long userID, long contestID) {
        User user = getUserByID(userID);
        Contest contest = getContestByID(contestID);
        contest.getProposalRegister().removeVote(user);
        contestRepository.saveAndFlush(contest);
        contentRepository.saveAllAndFlush(contest.getContents());
    }

    /**
     * Inserts a contest in the municipality corresponding to the given ID.
     *
     * @param municipalityID the ID of the municipality
     * @param contestIF      the DTO of the contest to insert
     * @return the ID of the inserted contest
     * @throws IllegalArgumentException if the animator of the contest is not authorized to insert contests
     *                                  in the municipality, or if the animator is null, or if the contest is null or if the contest is invalid
     */
    public long insertContest(long userID, long municipalityID, ContestIF contestIF) {
        ContestBuilder builder = new ContestBuilder(getUserByID(userID),
                getMunicipalityByID(municipalityID));

        builder.setName(contestIF.name())
                .setTopic(contestIF.topic())
                .setRules(contestIF.rules())
                .setStartDate(contestIF.startDate())
                .setVotingStartDate(contestIF.votingStartDate())
                .setEndDate(contestIF.endDate());

        if (contestIF.isPrivate()) {
            builder.setPrivate();
            contestIF.userIDs().stream()
                    .map(this::getUserByID)
                    .forEach(builder::addParticipant);
        }
        if (contestIF.geoLocatableID() != null)
            builder.setGeoLocation(getGeoLocatableByID(contestIF.geoLocatableID()));

        builder.build();
        Contest contest = contestRepository.saveAndFlush(builder.getResult());
        Municipality municipality = getMunicipalityByID(municipalityID);
        municipality.addContest(contest);
        municipalityRepository.saveAndFlush(municipality);
        if (contestIF.isPrivate()) sendNotifications(builder.getResult());
        return contest.getID();
    }

    /**
     * Performs a search on the list of users in the system using the given filters.
     * The filters are applied in logical and.
     *
     * @param filters the filters to apply
     * @return the list of users corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public List<UserOF> viewFilteredUsers(List<SearchFilter> filters) {
        return (List<UserOF>) SearchUtils.getFilteredItems(
                userRepository.findAll(),
                filters);
    }

    /**
     * Performs a search on the list of geo-locatables in the system using the given filters.
     * The filters are applied in logical and.
     *
     * @param filters the filters to apply
     * @return the list of geo-locatable corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public List<GeoLocatableOF> viewFilteredGeoLocatables(List<SearchFilter> filters) {
        return (List<GeoLocatableOF>) SearchUtils.getFilteredItems(
                geoLocatableRepository.findAll(),
                filters);
    }

    /**
     * Returns the Synthesized Format of all the contests that permits the user with the
     * given ID among all registered contests in the municipality with the given ID.
     *
     * @param userID         the ID of the user
     * @param municipalityID the ID of the municipality
     * @return the Synthesized Format of all the suitable contests
     */
    public List<ContestOF> viewAllContests(long userID, long municipalityID) {
        User user = getUserByID(userID);
        return contestRepository.findByMunicipalityAndValidTrue(getMunicipalityByID(municipalityID))
                .stream()
                .filter(contest -> contest.permitsUser(user))
                .map(Contest::getOutputFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contests that permits the user corresponding to the
     * given ID among all registered contests in the municipality corresponding to the given ID
     * that satisfy the given filters, all applied in logical and.
     *
     * @param userID         the ID of the user
     * @param municipalityID the ID of the municipality
     * @param filters        the filters to apply
     * @return the Synthesized Format of all the suitable contests
     */
    @SuppressWarnings("unchecked")
    public List<ContestOF> viewFilteredContests(long userID, long municipalityID, List<SearchFilter> filters) {
        User user = getUserByID(userID);
        List<SearchFilter> filtersWithUser = new ArrayList<>(filters);
        filtersWithUser.add(new SearchFilter(Parameter.THIS.toString(), "CONTEST_PERMITS_USER", user));
        return (List<ContestOF>) SearchUtils.getFilteredItems(
                contestRepository.findByMunicipalityAndValidTrue(getMunicipalityByID(municipalityID)),
                filtersWithUser);
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
    public List<String> getParameters() {
        return List.of(Parameter.NAME.toString(),
                Parameter.CONTEST_TOPIC.toString(),
                Parameter.CONTEST_STATUS.toString());
    }

    /**
     * Returns the Detailed Format of a Contest corresponding to the given ID in the municipality corresponding
     * to the given ID.
     *
     * @param contestID the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found in the municipality
     */
    public ContestOF viewContest(long contestID) {
        return getContestByID(contestID).getOutputFormat();
    }


    /**
     * Returns the Detailed Format of a Contest in the system having the given ID.
     * The contest can belong to any municipality.
     *
     * @param contestID the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found in the system
     **/
    public ContestOF viewContestFromRepository(long contestID) {
        return getContestByID(contestID).getOutputFormat();
    }

    private void sendNotifications(Contest contest) {
        Notification contestNotification = notificationRepository.saveAndFlush(Notification.createNotification(contest, this.INVITATION_MESSAGE));
        contest.getParticipants().forEach(user -> {
            user.addNotification(contestNotification);
            userRepository.saveAndFlush(user);
        });
    }

    private VotedContent getVotedContent(Contest contest, long contentID) {
        return contest.getProposalRegister().getProposals().stream()
                .filter(votedContent -> votedContent.getID() == contentID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
    }

    private Contest getContest(Municipality municipality, long contestID) {
        if (municipality == null)
            throw new IllegalArgumentException("Municipality can't be null");
        return municipality.getContests().stream()
                .filter(contest -> contest.getID() == contestID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contest not found"));
    }

    private ContestContent getContestContentByID(long contentID){
        if(getContentByID(contentID) instanceof ContestContent c)
            return c;
        throw new IllegalArgumentException("Content not found");
    }
    private Content<?> getContentByID(long contentID) {
        Optional<Content<?>> content = contentRepository.findById(contentID);
        if (content.isEmpty())
            throw new IllegalArgumentException("Content not found");
        return content.get();
    }

    private User getUserByID(long userID) {
        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return user.get();
    }

    private Contest getContestByID(long contestID) {
        Optional<Contest> contest = contestRepository.findById(contestID);
        if (contest.isEmpty())
            throw new IllegalArgumentException("Contest not found");
        return contest.get();
    }

    private Municipality getMunicipalityByID(long municipalityID) {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty())
            throw new IllegalArgumentException("Municipality not found");
        return municipality.get();
    }

    private GeoLocatable getGeoLocatableByID(long geoLocatableID) {
        Optional<GeoLocatable> geoLocatable = geoLocatableRepository.findById(geoLocatableID);
        if (geoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");
        return geoLocatable.get();
    }
}
