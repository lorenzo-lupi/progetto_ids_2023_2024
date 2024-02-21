package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.*;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUltils;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.VotedContent;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is used to handle the contests.
 * It provides methods to manage contests.
 */
public class ContestHandler {
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    public static final String INVITATION_MESSAGE = "You have been invited to participate in a contest";

    /**
     * Inserts the content obtained from the given contentIF in the contest with the given ID.
     * If the content insertion has success, an approval request for the entertainer of the contest is created.
     *
     * @param userID    the ID of the user who is inserting the content
     * @param contestID the ID of the contest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public static long insertContent(long userID, long contestID, ContentIF contentIF) {
        User user = userRepository.getItemByID(userID);
        Contest contest = municipalityRepository.getContestByID(contestID);

        ContestContent content = ContentHandler.createContent(
                new ContestContentBuilder(contest), user, contentIF);

        contest.getProposalRequests().proposeContent(content);
        RequestRepository.getInstance().add(RequestFactory.getApprovalRequest(content));
        return content.getID();
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest corresponding to the given ID
     * combined with their number of votes .
     *
     * @param contestID the ID of the contest
     * @return the Synthesized Format of all the proposals
     */
    public static List<VotedContentSOF> viewAllProposals(long contestID) {
        return municipalityRepository.getContestByID(contestID).getProposalRequests().getProposals().stream()
                .map(VotedContent::getSynthesizedFormat)
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
    public static List<VotedContentSOF> viewFilteredProposals(long contestID, List<SearchFilter> filters) {
        return (List<VotedContentSOF>) SearchUltils.getFilteredItems(
                municipalityRepository.getContestByID(contestID).getProposalRequests().getProposals(),
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
    public static VotedContentDOF viewProposal(long contestID, long contentID) {
        return getVotedContent(municipalityRepository.getContestByID(contestID), contentID).getDetailedFormat();
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
    public static void vote(long userID, long contestID, long contentID) {
        User user = userRepository.getItemByID(userID);
        Contest contest = municipalityRepository.getContestByID(contestID);
        contest.getProposalRequests().addVote(getVotedContent(contest, contentID).content(), user);
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
    public static void removeVote(long userID, long contestID) {
        User user = userRepository.getItemByID(userID);
        Contest contest = municipalityRepository.getContestByID(contestID);
        contest.getProposalRequests().removeVote(user);
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
    public static long insertContest(long userID, long municipalityID, ContestIF contestIF) {
        ContestBuilder builder = new ContestBuilder(userRepository.getItemByID(userID),
                municipalityRepository.getItemByID(municipalityID));

        builder.setName(contestIF.name())
                .setTopic(contestIF.topic())
                .setRules(contestIF.rules())
                .setStartDate(contestIF.startDate())
                .setVotingStartDate(contestIF.votingStartDate())
                .setEndDate(contestIF.endDate());

        if (contestIF.isPrivate()) {
            builder.setPrivate();
            contestIF.userIDs().stream()
                    .map(userRepository::getItemByID)
                    .forEach(builder::addParticipant);
        }
        if (contestIF.geoLocatableID() != null)
            builder.setGeoLocation(municipalityRepository.getGeoLocatableByID(contestIF.geoLocatableID()));

        builder.build();

        municipalityRepository.getItemByID(municipalityID).addContest(builder.getResult());
        if (contestIF.isPrivate()) sendNotifications(builder.getResult(), INVITATION_MESSAGE);
        return builder.getResult().getID();
    }

    /**
     * Performs a search on the list of users in the system using the given filters.
     * The filters are applied in logical and.
     *
     * @param filters the filters to apply
     * @return the list of users corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<UserSOF> viewFilteredUsers(List<SearchFilter> filters) {
        return (List<UserSOF>) SearchUltils.getFilteredItems(
                userRepository.getItemStream().toList(),
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
    public static List<GeoLocatableSOF> viewFilteredGeoLocatables(List<SearchFilter> filters) {
        return (List<GeoLocatableSOF>) SearchUltils.getFilteredItems(
                municipalityRepository.getAllGeoLocatables().toList(),
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
    public static List<ContestSOF> viewAllContests(long userID, long municipalityID) {
        User user = UserRepository.getInstance().getItemByID(userID);
        return MunicipalityRepository.getInstance().getItemByID(municipalityID).getContests().stream()
                .filter(contest -> contest.permitsUser(user))
                .map(Contest::getSynthesizedFormat)
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
    public static List<ContestSOF> viewFilteredContests(long userID, long municipalityID, List<SearchFilter> filters) {
        User user = UserRepository.getInstance().getItemByID(userID);
        List<SearchFilter> filtersWithUser = new ArrayList<>(filters);
        filtersWithUser.add(new SearchFilter(Parameter.THIS.toString(), "CONTEST_PERMITS_USER", user));
        return (List<ContestSOF>) SearchUltils.getFilteredItems(
                MunicipalityRepository.getInstance().getItemByID(municipalityID).getContests(),
                filtersWithUser);
    }


    /**
     * Returns the set of all the criteria available for the search.
     * @return the set of all the criteria available for the search
     */
    public static Set<String> getSearchCriteria(){
        return SearchUltils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     * @return the search parameters for the user entity
     */
    public static List<String> getParameters(){
        return List.of(Parameter.NAME.toString(),
                Parameter.CONTEST_TOPIC.toString(),
                Parameter.CONTEST_STATUS.toString());
    }

    /**
     * Returns the Detailed Format of a Contest corresponding to the given ID in the municipality corresponding
     * to the given ID.
     *
     * @param municipalityID the ID of the municipality
     * @param contestID      the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found in the municipality
     */
    public static ContestDOF viewContest(long municipalityID, long contestID) {
        return getContest(MunicipalityRepository.getInstance().getItemByID(municipalityID), contestID).getDetailedFormat();
    }


    /**
     * Returns the Detailed Format of a Contest in the system having the given ID.
     * The contest can belong to any municipality.
     *
     * @param contestID the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found in the system
     **/
    public static ContestDOF viewContestFromRepository(long contestID) {
        return MunicipalityRepository.getInstance().getContestByID(contestID).getDetailedFormat();
    }


    private static void sendNotifications(Contest contest, String message) {
        Notification contestNotification = Notification.createNotification(contest, message);
        contest.getParticipants().forEach(user -> user.addNotification(contestNotification));
    }

    private static VotedContent getVotedContent(Contest contest, long contentID) {
        return contest.getProposalRequests().getProposals().stream()
                .filter(votedContent -> votedContent.getID() == contentID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
    }

    private static Contest getContest(Municipality municipality, long contestID) {
        if (municipality == null)
            throw new IllegalArgumentException("Municipality can't be null");
        return municipality.getContests().stream()
                .filter(contest -> contest.getID() == contestID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contest not found"));
    }
}
