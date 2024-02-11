package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.*;

import java.util.List;

/**
 * This class handles the evaluation of requests for approval of items.
 */
public class RequestEvaluationHandler {

    private static final RequestRepository requestRepository = RequestRepository.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();

    /**
     * Returns the Synthesized Format of all the municipality requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable municipality requests
     */
    public static List<MunicipalityRequestSOF> viewMunicipalityRequests(long userID) {
        User user = userRepository.getItemByID(userID);
        if (!canAccessRequests(user, RoleTypeEnum.CURATOR))
            throw new UnsupportedOperationException("The user cannot access the municipality requests");

        return requestRepository.getAllMunicipalityRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(MunicipalityRequest::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contest requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable contest requests
     */
    public static List<ContestRequestSOF> viewContestRequests(long userID) {
        User user = userRepository.getItemByID(userID);
        if (!canAccessRequests(user, RoleTypeEnum.ENTERTAINER))
            throw new UnsupportedOperationException("The user cannot access the contest requests");

        return requestRepository.getAllContestRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(ContestRequest::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Detailed Format of the municipality request corresponding to the given ID, if any.
     *
     * @param requestID the ID of the request
     * @return the Detailed Format of the municipality request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * municipality request
     */
    public static MunicipalityRequestDOF viewMunicipalityRequest(long requestID) {
        return requestRepository.getMunicipalityRequestByID(requestID).getDetailedFormat();
    }

    /**
     * Returns the Detailed Format of the contest request corresponding to the given ID, if any.
     *
     * @param requestID the ID of the request
     * @return the Detailed Format of the contest request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * contest request
     */
    public static ContestRequestDOF viewContestRequest(long requestID) {
        return requestRepository.getContestRequestByID(requestID).getDetailedFormat();
    }

    /**
     * Return the confirmation type of the request corresponding to the given ID.
     *
     * @param requestID the ID of the request
     * @return the confirmation type of the request
     */
    public static ConfirmationType getConfirmationType(long requestID) {
        return requestRepository.getItemByID(requestID).getCommand().getConfirmationType();
    }

    /**
     * Approves or disapproves the municipality request corresponding to the given ID depending on
     * the given boolean value.
     *
     * @param requestID the ID of the request
     * @param isApproved true if the user approves the request, false otherwise
     * @throws IllegalArgumentException if the request with the given ID is not found
     */
    public static void setApprovation(long requestID, boolean isApproved) {
        Request<?> request = RequestRepository.getInstance().getItemByID(requestID);
        if (isApproved) request.approve();
        else request.reject();
    }

    /**
     * Checks if the given user can access the municipality requests for the given role or, equivalently, if he
     * has the given role for at least one municipality.
     *
     * @param user the user to check
     * @return true if the user can access the requests for the given role, false otherwise
     */
    private static boolean canAccessRequests(User user, RoleTypeEnum role) {
        return user.getRoles().stream()
                .anyMatch(userRole -> userRole.roleTypeEnum().equals(role));
    }

}
