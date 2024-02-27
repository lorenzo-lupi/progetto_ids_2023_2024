package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class provides the methods to handle:
 * - Viewing of requests.
 * - Approval or disapproval of requests.
 * - Requests of deletion of {@link GeoLocatable} objects.
 * - Requests modification of {@link GeoLocatable} objects.
 * - User promotion requests.
 * - Creation of reports.
 */
@Service
public class RequestHandler {

    private final RequestJpaRepository requestJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final MunicipalityJpaRepository municipalityJpaRepository;
    private final GeoLocatableJpaRepository geoLocatableJpaRepository;
    private final ContentJpaRepository contentJpaRepository;

    @Autowired
    public RequestHandler(RequestJpaRepository requestJpaRepository,
                          UserJpaRepository userJpaRepository,
                          MunicipalityJpaRepository municipalityJpaRepository,
                          GeoLocatableJpaRepository geoLocatableJpaRepository,
                          ContentJpaRepository contentJpaRepository) {
        this.requestJpaRepository = requestJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.municipalityJpaRepository = municipalityJpaRepository;
        this.geoLocatableJpaRepository = geoLocatableJpaRepository;
        this.contentJpaRepository = contentJpaRepository;
    }

    /**
     * Returns all the municipality requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable municipality requests
     * @throws IllegalArgumentException if the user with the given ID is not found
     * @throws UnsupportedOperationException if the user cannot access the municipality requests
     */
    public List<MunicipalityRequestOF> viewMunicipalityRequests(long userID) {
        Optional<User> optionalUser = userJpaRepository.findById(userID);
        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("User not found");

        User user = optionalUser.get();
        if (!canAccessRequests(user, AuthorizationEnum.CURATOR))
            throw new UnsupportedOperationException("The user cannot access the municipality requests");

        return requestJpaRepository.findAllMunicipalityRequests().stream()
                .filter(request -> request.canBeApprovedBy(user))
                .map(MunicipalityRequest::getOutputFormat)
                .toList();
    }

    /**
     * Returns all the contest requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable contest requests
     * @throws IllegalArgumentException if the user with the given ID is not found
     * @throws UnsupportedOperationException if the user cannot access the contest requests
     */
    public List<ContestRequestOF> viewContestRequests(long userID) {
        Optional<User> optionalUser = userJpaRepository.findById(userID);
        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("User not found");

        User user = optionalUser.get();
        if (!canAccessRequests(user, AuthorizationEnum.ENTERTAINER))
            throw new UnsupportedOperationException("The user cannot access the contest requests");

        return requestJpaRepository.findAllContestRequests().stream()
                .filter(request -> request.canBeApprovedBy(user))
                .map(ContestRequest::getOutputFormat)
                .toList();
    }

    /**
     * Returns the municipality request corresponding to the given ID, if any.
     *
     * @param requestID the ID of the request
     * @return the Detailed Format of the municipality request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * municipality request
     */
    public MunicipalityRequestOF viewMunicipalityRequest(long requestID) {
        Optional<MunicipalityRequest<?>> optionalRequest = requestJpaRepository.findMunicipalityRequestById(requestID);
        if (optionalRequest.isEmpty())
            throw new IllegalArgumentException("Request not found");

        return optionalRequest.get().getOutputFormat();
    }

    /**
     * Returns the contest request corresponding to the given ID, if any.
     *
     * @param requestID the ID of the request
     * @return the Detailed Format of the contest request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * contest request
     */
    public ContestRequestOF viewContestRequest(long requestID) {
        Optional<ContestRequest> optionalRequest = requestJpaRepository.findContestRequestById(requestID);
        if (optionalRequest.isEmpty())
            throw new IllegalArgumentException("Request not found");

        return optionalRequest.get().getOutputFormat();
    }

    /**
     * Return the confirmation type of the request corresponding to the given ID.
     *
     * @param requestID the ID of the request
     * @return the confirmation type of the request
     * @throws IllegalArgumentException if the request with the given ID is not found
     */
    public ConfirmationType getConfirmationType(long requestID) {
        Optional<Request<?>> optionalRequest = requestJpaRepository.findById(requestID);
        if (optionalRequest.isEmpty())
            throw new IllegalArgumentException("Request not found");

        return optionalRequest.get().getCommand().getConfirmationType();
    }

    /**
     * Approves or disapproves the municipality request corresponding to the given ID depending on
     * the given boolean value.
     *
     * @param requestID the ID of the request
     * @param isApproved true if the user approves the request, false otherwise
     * @throws IllegalArgumentException if the request with the given ID is not found
     */
    public void setApprovation(long requestID, boolean isApproved) {
        Optional<Request<?>> optionalRequest = requestJpaRepository.findById(requestID);
        if (optionalRequest.isEmpty())
            throw new IllegalArgumentException("Request not found");

        if (isApproved) optionalRequest.get().approve();
        else optionalRequest.get().reject();

        requestJpaRepository.save(optionalRequest.get());
    }

    /**
     * Checks if the given user can access the municipality requests for the given role or, equivalently, if he
     * has the given role for at least one municipality.
     *
     * @param user the user to check
     * @return true if the user can access the requests for the given role, false otherwise
     */
    private boolean canAccessRequests(User user, AuthorizationEnum role) {
        return user.getRoles().stream()
                .anyMatch(userRole -> userRole.authorizationEnum().equals(role));
    }

    /**
     * This method creates a request for the deletion of a GeoLocatable object.
     * The deletion request is sent to the municipality where the GeoLocatable resides.
     *
     * @param geoLocatableID the id of the geo-locatable object to delete
     * @param userId   the id of the user who deletes the object
     * @param message  the message of the request
     * @return the id of the request
     * @throws IllegalArgumentException if the user with the given ID is not found or if the GeoLocatable object
     * with the given ID is not found
     */
    public long deleteGeoLocatable(long userId, long geoLocatableID, String message) {
        Optional<User> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("User not found");

        Optional<GeoLocatable> optionalGeoLocatable = geoLocatableJpaRepository.findById(geoLocatableID);
        if (optionalGeoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");

        return addRequest(RequestFactory.getDeletionRequest(optionalUser.get(), optionalGeoLocatable.get(), message));
    }

    /**
     * Returns all the possible authorizations that can be referenced for promotions.
     * The authorizations are all the authorizations except the administrator one.
     *
     * @return the authorizations that can be referenced for promotions
     */
    public List<String> obtainPossiblePromotions() {
        return Arrays.stream(AuthorizationEnum.values())
                .filter(p -> !p.equals(AuthorizationEnum.ADMINISTRATOR))
                .map(Enum::toString)
                .toList();
    }

    /**
     * Inserts a promotion request for the user corresponding to the given ID, for the municipality
     * corresponding to the given ID.
     *
     * @param userId         the ID of the user
     * @param municipalityId the ID of the municipality
     * @param authorization       the role type
     * @param message        the message
     * @return the ID of the request
     * @throws IllegalArgumentException if the authorization is null, if the message is null, if the municipality
     * with the given ID is not found, or if the user with the given ID is not found
     * @throws UnsupportedOperationException if the user has already the authorization for the municipality
     */
    public long insertPromotionRequest(long userId, long municipalityId, AuthorizationEnum authorization, String message) {
        if (authorization == null || message == null)
            throw new IllegalArgumentException("parameters can't be null");

        Optional<Municipality> optionalMunicipality = municipalityJpaRepository.findById(municipalityId);
        if (optionalMunicipality.isEmpty())
            throw new IllegalArgumentException("Municipality not found");

        Optional<User> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("User not found");

        if (optionalUser.get().getAuthorizations(optionalMunicipality.get()).contains(authorization))
            throw new UnsupportedOperationException("User is already a " + authorization + " for the municipality");

        Request<User> request = RequestFactory.getPromotionRequest(optionalUser.get(),
                new Role(optionalMunicipality.get(), authorization),
                message);

        return addRequest(request);
    }

    /**
     * This method creates a new report for a GeoLocatable object.
     * @param geoLocatableID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     * @throws IllegalArgumentException if the GeoLocatable object with the given ID is not found
     */
    public long reportGeoLocatable(long geoLocatableID, String description){
        Optional<GeoLocatable> optionalGeoLocatable = geoLocatableJpaRepository.findById(geoLocatableID);
        if(optionalGeoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");

        return addRequest(RequestFactory.getDeletionRequest(optionalGeoLocatable.get(), description));
    }

    /**
     * This method creates a new report for a content.
     * The report will be addressed to the municipality where the content resides or to the animator of the contest,
     * depending on the type of the reported content.
     *
     * @param contentID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     * @throws IllegalArgumentException if the Content object with the given ID is not found
     */
    public long reportContent(long contentID, String description){
        Optional<Content<?>> optionalContent = contentJpaRepository.findById(contentID);
        if(optionalContent.isEmpty())
            throw new IllegalArgumentException("Content not found");

        return addRequest(RequestFactory.getDeletionRequest(optionalContent.get(), description));
    }

    /**
     * This method returns the setters of the GeoLocatable object with the given ID.
     *
     * @param geolocatableID the ID of the GeoLocatable object
     * @return the setters of the GeoLocatable object
     * @throws IllegalArgumentException if the GeoLocatable object with the given ID is not found
     */
    public List<String> getGeoLocatableSetters(long geolocatableID) {
        Optional<GeoLocatable> optionalGeoLocatable = geoLocatableJpaRepository.findById(geolocatableID);
        if (optionalGeoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");

        return optionalGeoLocatable.get()
                .getSettersMapping()
                .keySet()
                .stream()
                .map(Enum::toString)
                .toList();
    }

    /**
     * Sends a modification request for the GeoLocatable having the given id to the municipality where the
     * GeoLocatable resides.
     * The modification request is sent only if the user with the given id doesn't have Curator
     * authorization for the municipality of the GeoLocatable or greater.
     *
     * @param userID the id of the user
     * @param geolocatableID the id of the GeoLocatable object
     * @param settings the pairs of parameters and values to modify
     * @param message the message of the request
     * @return the id of the generated request if any, 0 otherwise
     * @throws IllegalArgumentException if the user with the given ID is not found or if the GeoLocatable object
     * with the given ID is not found, or if the provided pairs are invalid
     */
    public long modifyGeoLocatable(long userID,
                                   long geolocatableID,
                                   List<ModificationSetting> settings,
                                   String message) {
        Optional<GeoLocatable> optionalGeoLocatable = geoLocatableJpaRepository.findById(geolocatableID);
        if (optionalGeoLocatable.isEmpty())
            throw new IllegalArgumentException("GeoLocatable not found");

        Optional<User> optionalUser = userJpaRepository.findById(userID);
        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("User not found");

        List<Pair<Parameter, Object>> pairs  = settings.stream()
                .map(setting -> Pair.of(Parameter.valueOf(setting.parameter()), setting.value()))
                .toList();

        return modifyGeoLocatable(optionalUser.get(), optionalGeoLocatable.get(), pairs, message);
    }

    private long modifyGeoLocatable(User user,
                                    GeoLocatable geoLocatable,
                                    List<Pair<Parameter, Object>> pairs,
                                    String message) {

        if (userIsAuthorized(user, geoLocatable.getMunicipality())) {
            modifyGeoLocatableValues(geoLocatable, pairs);
            return 0L;
        } else {
            return addModifyRequest(pairs, geoLocatable, message);
        }

    }

    private void modifyGeoLocatableValues(GeoLocatable geoLocatable,
                                          List<Pair<Parameter, Object>> pairs) {
        pairs.forEach(pair -> {
                    if (!geoLocatable.getSettersMapping().containsKey(pair.getLeft()))
                        throw new IllegalArgumentException("Invalid parameter");
                    geoLocatable.getSettersMapping()
                            .get(pair.getLeft())
                            .accept(pair.getRight());
                }
        );
    }

    private boolean userIsAuthorized(User user, Municipality municipality) {
        return Role.isAtLeastContributorForMunicipality(municipality).test(user);
    }

    private long addRequest(Request<?> request){
        requestJpaRepository.save(request);
        return request.getID();
    }

    private long addModifyRequest(List<Pair<Parameter, Object>> pairs,
                                         GeoLocatable geoLocatable, String message) {
        Request<?> request = RequestFactory.getModificationRequest(geoLocatable, pairs, message);
        return addRequest(request);
    }


}
