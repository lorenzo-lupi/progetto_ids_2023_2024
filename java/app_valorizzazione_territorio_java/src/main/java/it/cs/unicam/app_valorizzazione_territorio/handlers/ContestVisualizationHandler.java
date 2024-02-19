package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a handler for the search and visualization of the contests of a municipality.
 */
public class ContestVisualizationHandler {

    /**
     * Returns the Synthesized Format of all the contests that permits the user with the
     * given ID among all registered contests in the municipality with the given ID.
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
        List<SearchFilter> filtersWithUser = new ArrayList<>(filters);
        filtersWithUser.add(new SearchFilter(Parameter.THIS.toString(), "CONTEST_PERMITS_USER", user));
        return (List<ContestSOF>) SearchHandler.getFilteredItems(
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

    private static Contest getContest(Municipality municipality, long contestID) {
        if(municipality == null)
            throw new IllegalArgumentException("Municipality can't be null");
        return municipality.getContests().stream()
                .filter(contest -> contest.getID() == contestID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contest not found"));
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
}
