package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.requests.ApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityApprovalRequest;

import java.util.stream.Stream;

public class ApprovalRequestRepository extends Repository<ApprovalRequest> {
    private static ApprovalRequestRepository instance;

    private ApprovalRequestRepository() {
        super();
    }

    public static ApprovalRequestRepository getInstance() {
        if (instance == null) instance = new ApprovalRequestRepository();
        return instance;
    }

    /**
     * Returns a stream of all the municipality approval requests among all requests in the system.
     *
     * @return a stream of all the municipality approval requests n the system
     */
    @SuppressWarnings("unchecked")
    public Stream<MunicipalityApprovalRequest> getAllMunicipalityRequests() {
        return (Stream<MunicipalityApprovalRequest>) this.getItemStream()
                .filter(request -> request instanceof MunicipalityApprovalRequest).toList();
    }

    /**
     * Returns a stream of all the contest approval requests among all requests in the system.
     *
     * @return a stream of all the contest approval requests n the system
     */
    @SuppressWarnings("unchecked")
    public Stream<ContestApprovalRequest> getAllContestRequests() {
        return (Stream<ContestApprovalRequest>) this.getItemStream()
                .filter(request -> request instanceof ContestApprovalRequest).toList();
    }

    /**
     * Returns the municipality approval request corresponding to the given ID, if any
     *
     * @param requestID the ID of the request
     * @return the municipality approval request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * municipality approval request
     */
    public MunicipalityApprovalRequest getMunicipalityRequestByID(long requestID) {
        if (!(this.getItemByID(requestID) instanceof MunicipalityApprovalRequest municipalityApprovalRequest))
            throw new IllegalArgumentException("The request with the given ID is not a municipality approval request");
        return municipalityApprovalRequest;
    }

    /**
     * Returns the contest approval request corresponding to the given ID, if any
     *
     * @param requestID the ID of the request
     * @return the contest approval request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * contest approval request
     */
    public ContestApprovalRequest getContestRequestByID(long requestID) {
        if (!(this.getItemByID(requestID) instanceof ContestApprovalRequest contestApprovalRequest))
            throw new IllegalArgumentException("The request with the given ID is not a contest approval request");
        return contestApprovalRequest;
    }
}
