package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.contest.VotedContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.VotedContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for the search and visualization of the contents proposed for a contest
 * and their voting.
 */
public class ContestContentVoteHandler extends SearchHandler<VotedContent> {
    private final User user;
    private final Contest contest;

    public ContestContentVoteHandler(long userID, long contestID) {
        super(MunicipalityRepository.getInstance().getContestByID(contestID).getProposalRequests().getProposals());
        this.user = UserRepository.getInstance().getItemByID(userID);
        this.contest = MunicipalityRepository.getInstance().getContestByID(contestID);
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest corresponding to the given ID
     * combined with their number of votes .
     *
     * @param contestID the ID of the contest
     * @return the Synthesized Format of all the proposals
     */
    public static List<VotedContentSOF> viewAllProposals(long contestID) {
        return MunicipalityRepository.getInstance().getContestByID(contestID).getProposalRequests().getProposals().stream()
                .map(VotedContent::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest corresponding to the given ID
     * combined with their number of votes that satisfy the given filters, all applied in logical and.
     *
     * @param contestID the ID of the contest
     * @param filters the filters to apply
     * @return the Synthesized Format of all the suitable proposals
     */
    @SuppressWarnings("unchecked")
    public static List<VotedContentSOF> viewFilteredProposals(long contestID, List<SearchFilter> filters) {
        return (List<VotedContentSOF>) getFilteredItems(
                MunicipalityRepository.getInstance().getContestByID(contestID).getProposalRequests().getProposals(),
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
        return getVotedContent(MunicipalityRepository.getInstance().getContestByID(contestID), contentID).getDetailedFormat();
    }

    /**
     * Adds a vote to the content corresponding to the given ID from the user corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @param contestID the ID of the contest
     * @param contentID the ID of the content to be voted
     * @throws IllegalArgumentException if the user or the contest is not found or if the content
     * is not found in the contest or if the user has already voted in the contest
     */
    public static void vote(long userID, long contestID, long contentID) {
        User user = UserRepository.getInstance().getItemByID(userID);
        Contest contest = MunicipalityRepository.getInstance().getContestByID(contestID);
        contest.getProposalRequests().addVote(getVotedContent(contest, contentID).content(), user);
    }

    /**
     * Removes the vote on some content of the contest corresponding to the given ID previously
     * given by the user corresponding to the given ID, if any.
     *
     * @param userID the ID of the user
     * @param contestID the ID of the contest
     * @throws IllegalArgumentException if the user or the contest is not found or if the user
     * has not voted for any content in the contest
     */
    public static void removeVote(long userID, long contestID) {
        User user = UserRepository.getInstance().getItemByID(userID);
        Contest contest = MunicipalityRepository.getInstance().getContestByID(contestID);
        contest.getProposalRequests().removeVote(user);
    }

    /**
     * Returns the Synthesized Format of all the contents proposed for the contest combined with their number
     * of votes.
     *
     * @return the Synthesized Format of all the proposals
     */
    public List<VotedContentSOF> viewAllProposals() {
        return contest.getProposalRequests().getProposals().stream()
                .map(VotedContent::getSynthesizedFormat)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<VotedContentSOF> getSearchResult() {
        return (List<VotedContentSOF>) super.getSearchResult();
    }

    private VotedContent getVotedContent(long contentID) {
        return getVotedContent(this.contest, contentID);
    }

    /**
     * Returns the detailed format of the content proposed for the contest combined with its number of votes
     * corresponding to the given ID.
     *
     * @param contentID the ID of the content
     * @return the detailed format of the proposal
     * @throws IllegalArgumentException if the content is not found in the contest
     */
    public VotedContentDOF viewProposal(long contentID) {
        return getVotedContent(contentID).getDetailedFormat();
    }

    /**
     * Adds a vote to the content from the user corresponding to the given ID.
     *
     * @param contentID the ID of the content to be voted
     * @throws IllegalArgumentException if the content is not found in the contest or
     * if the user has already voted
     */
    public void vote(long contentID) {
        contest.getProposalRequests().addVote(getVotedContent(contentID).content(), user);
    }

    /**
     * Removes the vote on the content previously voted by the user in the contest, if any.
     *
     * @throws IllegalArgumentException if the user has not voted for any content
     */
    public void removeVote() {
        contest.getProposalRequests().removeVote(user);
    }

    private static VotedContent getVotedContent(Contest contest, long contentID) {
        return contest.getProposalRequests().getProposals().stream()
                .filter(votedContent -> votedContent.getID() == contentID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
    }
}
