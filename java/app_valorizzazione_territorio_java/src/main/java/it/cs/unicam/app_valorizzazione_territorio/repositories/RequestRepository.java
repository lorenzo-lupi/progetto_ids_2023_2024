package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityRequest;

import java.util.stream.Stream;

public class RequestRepository extends Repository<Request> {
    private static RequestRepository instance;

    private RequestRepository() {
        super();
    }

    public static RequestRepository getInstance() {
        if (instance == null) instance = new RequestRepository();
        return instance;
    }

    /**
     * Returns a stream of all the municipality approval requests among all requests in the system.
     *
     * @return a stream of all the municipality approval requests n the system
     */
    @SuppressWarnings("unchecked")
    public Stream<MunicipalityRequest> getAllMunicipalityRequests() {
        return this.getItemStream()
                .filter(request -> request instanceof MunicipalityRequest)
                .map(request -> (MunicipalityRequest) request);
    }

    /**
     * Returns a stream of all the contest approval requests among all requests in the system.
     *
     * @return a stream of all the contest approval requests n the system
     */
    @SuppressWarnings("unchecked")
    public Stream<ContestRequest> getAllContestRequests() {
        return  this.getItemStream()
                .filter(request -> request instanceof ContestRequest)
                .map(request -> (ContestRequest) request);
    }

    /**
     * Returns the municipality approval request corresponding to the given ID, if any
     *
     * @param requestID the ID of the request
     * @return the municipality approval request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * municipality approval request
     */
    public MunicipalityRequest getMunicipalityRequestByID(long requestID) {
        if (!(this.getItemByID(requestID) instanceof MunicipalityRequest municipalityApprovalRequest))
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
    public ContestRequest getContestRequestByID(long requestID) {
        if (!(this.getItemByID(requestID) instanceof ContestRequest contestApprovalRequest))
            throw new IllegalArgumentException("The request with the given ID is not a contest approval request");
        return contestApprovalRequest;
    }
}
