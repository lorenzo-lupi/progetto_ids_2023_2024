package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for the search and visualization of the contests of a municipality.
 */
public class ContestVisualizationHandler extends SearchHandler<Contest> {

    private final User user;

    private final Municipality municipality;

    public ContestVisualizationHandler(long userID, long municipalityID) {
        super(MunicipalityRepository.getInstance().getItemByID(municipalityID).getContests());
        this.user = UserRepository.getInstance().getItemByID(userID);
        this.municipality = MunicipalityRepository.getInstance().getItemByID(municipalityID);
    }

    /**
     * Returns the Synthesized Format of all the contests that permits the user corresponding to the
     * given ID among all registered contests in the municipality corresponding to the given ID.
     *
     * @param userID the ID of the user
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
     * @param userID the ID of the user
     * @param municipalityID the ID of the municipality
     * @param filters the filters to apply
     * @return the Synthesized Format of all the suitable contests
     */
    @SuppressWarnings("unchecked")
    public static List<ContestSOF> viewFilteredContests(long userID, long municipalityID, List<SearchFilter> filters) {
        User user = UserRepository.getInstance().getItemByID(userID);
        List<SearchFilter> filtersWithUser = List.copyOf(filters);
        filtersWithUser.add(new SearchFilter(Parameter.THIS.toString(), SearchCriterion.PERMITS_USER.toString(), user));
        return (List<ContestSOF>) getFilteredItems(
                MunicipalityRepository.getInstance().getItemByID(municipalityID).getContests(),
                filtersWithUser);
    }

    /**
     * Returns the Detailed Format of a Contest having the given ID.
     *
     * @param contestID the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found
     **/
    public static ContestDOF viewContest(long contestID) {
        Contest contest = MunicipalityRepository.getInstance().getContestByID(contestID);
        if (contest == null)
            throw new IllegalArgumentException("Contest not found");

        return contest.getDetailedFormat();
    }

    /**
     * Returns the Synthesized Format of all the contests that permits the user among all registered contests
     * in the municipality.
     *
     * @return the Synthesized Format of all the suitable contests
     */
    public List<ContestSOF> viewAllContests() {
        return municipality.getContests().stream()
                .filter(contest -> contest.permitsUser(user))
                .map(Contest::getSynthesizedFormat)
                .toList();
    }

    /**
     * Starts a new search by resetting the criteria of the search engine.
     * The started search is performed on the contests of the municipality that permit the user.
     */
    @Override
    public void startSearch() {
        super.startSearch();
        this.searchEngine.addCriterion(Parameter.THIS, SearchCriterion.PERMITS_USER, user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContestSOF> getSearchResult() {
        return (List<ContestSOF>) super.getSearchResult();
    }
}
