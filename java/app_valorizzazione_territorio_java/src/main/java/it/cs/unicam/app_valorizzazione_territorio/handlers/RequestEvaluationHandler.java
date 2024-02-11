package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContestRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestRequest;

import java.util.List;

/**
 * This class handles the evaluation of requests for approval of items.
 */
public class RequestEvaluationHandler {

    /**
     * Returns the Synthesized Format of all the municipality requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable municipality requests
     */
    public static List<MunicipalityRequestSOF> viewMunicipalityRequests(long userID) {
        User user = UserRepository.getInstance().getItemByID(userID);
        if (!canAccessRequests(user, RoleTypeEnum.CURATOR))
            throw new UnsupportedOperationException("The user cannot access the municipality requests");

        return viewMunicipalityRequestsApprovableBy(user);
    }

    /**
     * Returns the Synthesized Format of all the contest requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable contest requests
     */
    public static List<ContestRequestSOF> viewContestRequests(long userID) {
        User user = UserRepository.getInstance().getItemByID(userID);
        if (!canAccessRequests(user, RoleTypeEnum.ENTERTAINER))
            throw new UnsupportedOperationException("The user cannot access the contest requests");

        return viewContestRequestsApprovableBy(user);
    }

    /**
     * Returns the Detailed Format of the municipality request corresponding to the given ID, if any.
     * The user corresponding to the given ID must be able to access the municipality request.
     *
     * @param userID the ID of the user
     * @param requestID the ID of the request
     * @return the Detailed Format of the municipality request corresponding to the given ID
     * @throws IllegalArgumentException if the user with the given ID is not found, if the request with the
     * given ID is not found or if it is not a municipality approval request
     * @throws UnsupportedOperationException if the user cannot access the municipality request
     */
    public static MunicipalityRequestDOF viewMunicipalityRequest(long userID, long requestID) {
        return viewMunicipalityApprovableRequest(UserRepository.getInstance().getItemByID(userID), requestID);
    }

    /**
     * Returns the Detailed Format of the contest request corresponding to the given ID, if any.
     * The user corresponding to the given ID must be able to access the contest request.
     *
     * @param userID the ID of the user
     * @param requestID the ID of the request
     * @return the Detailed Format of the contest request corresponding to the given ID
     */
    public static ContestRequestDOF viewContestRequest(long userID, long requestID) {
        return viewContestApprovableRequest(UserRepository.getInstance().getItemByID(userID), requestID);
    }

    /**
     * Makes the user with the given ID approve or disapprove the municipality request corresponding to the given ID.
     * The user corresponding to the given ID must be able to access the request.
     *
     * @param userID the ID of the user
     * @param requestID the ID of the request
     * @param isApproved true if the user approves the request, false otherwise
     * @throws IllegalArgumentException if the user with the given ID is not found, if the request with the
     * given ID is not found
     * @throws UnsupportedOperationException if the user cannot approve or reject the request
     */
    public static void setApprovation(long userID, long requestID, boolean isApproved) {
        setApprovation(UserRepository.getInstance().getItemByID(userID), requestID, isApproved);
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

    /**
     * Returns the Synthesized Format of all the municipality requests that can be approved by the user.
     *
     * @param user  the user
     * @return the Synthesized Format of all the suitable municipality requests
     */
    private static List<MunicipalityRequestSOF> viewMunicipalityRequestsApprovableBy(User user) {
        return ApprovalRequestRepository.getInstance().getAllMunicipalityRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(MunicipalityRequest::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contest requests that can be approved by the user.
     *
     * @param user the user
     * @return the Synthesized Format of all the suitable contest requests
     */
    private static List<ContestRequestSOF> viewContestRequestsApprovableBy(User user) {
        return ApprovalRequestRepository.getInstance().getAllContestRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(ContestRequest::getSynthesizedFormat)
                .toList();
    }

    private static MunicipalityRequestDOF viewMunicipalityApprovableRequest(User user, long requestID) {
        MunicipalityRequest request = ApprovalRequestRepository.getInstance().getMunicipalityRequestByID(requestID);
        if (!request.canBeApprovedBy(user))
            throw new UnsupportedOperationException("The user cannot access the municipality request");
        return request.getDetailedFormat();
    }

    private static ContestRequestDOF viewContestApprovableRequest(User user, long requestID) {
        ContestRequest request = ApprovalRequestRepository.getInstance().getContestRequestByID(requestID);
        if (!request.canBeApprovedBy(user))
            throw new UnsupportedOperationException("The user cannot access the contest request");
        return request.getDetailedFormat();
    }

    private static void setApprovation(User user, long requestID, boolean isApproved) {
        Request<?> request = ApprovalRequestRepository.getInstance().getItemByID(requestID);
        if (!request.canBeApprovedBy(user))
            throw new UnsupportedOperationException("The user cannot approve or disapprove the request");

        if (isApproved) request.approve();
        else request.reject();
    }

}
