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

    /**
     * Creates a new ContestVisualizationHandler for the municipality corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @param municipalityID the ID of the municipality
     */
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
     * Returns the Detailed Format of a Contest corresponding to the given ID in the municipality corresponding
     * to the given ID.
     *
     * @param municipalityID the ID of the municipality
     * @param contestID the ID of the Contest to visualize
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
     * Returns the Detailed Format of a Contest corresponding to the given ID in the municipality.
     *
     * @param contestID the ID of the Contest to visualize
     * @return the Detailed Format of the Contest having the given ID
     * @throws IllegalArgumentException if the Contest having the given ID is not found in the municipality
     */
    public ContestDOF viewContest(long contestID) {
        return getContest(municipality, contestID).getDetailedFormat();
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

    private static Contest getContest(Municipality municipality, long contestID) {
        return municipality.getContests().stream()
                .filter(contest -> contest.getID() == contestID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contest not found"));
    }
}
