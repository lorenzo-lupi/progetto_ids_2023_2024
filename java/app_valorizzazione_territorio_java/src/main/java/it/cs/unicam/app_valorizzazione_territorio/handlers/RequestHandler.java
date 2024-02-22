package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides the methods to handle:
 * - Viewing of requests.
 * - Approval or disapproval of requests.
 * - Requests of deletion of {@link GeoLocatable} objects.
 * - Requests modification of {@link GeoLocatable} objects.
 * - User promotion requests.
 * - Creation of reports.
 */
public class RequestHandler {

    private static final RequestRepository requestRepository = RequestRepository.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    /**
     * Returns the Synthesized Format of all the municipality requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable municipality requests
     */
    public static List<MunicipalityRequestOF> viewMunicipalityRequests(long userID) {
        User user = userRepository.getItemByID(userID);
        if (!canAccessRequests(user, AuthorizationEnum.CURATOR))
            throw new UnsupportedOperationException("The user cannot access the municipality requests");

        return requestRepository.getAllMunicipalityRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(MunicipalityRequest::getOutputFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contest requests that can be approved by the user
     * corresponding to the given ID.
     *
     * @param userID the ID of the user
     * @return the Synthesized Format of all the suitable contest requests
     */
    public static List<ContestRequestOF> viewContestRequests(long userID) {
        User user = userRepository.getItemByID(userID);
        if (!canAccessRequests(user, AuthorizationEnum.ENTERTAINER))
            throw new UnsupportedOperationException("The user cannot access the contest requests");

        return requestRepository.getAllContestRequests()
                .filter(request -> request.canBeApprovedBy(user))
                .map(ContestRequest::getOutputFormat)
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
    public static MunicipalityRequestOF viewMunicipalityRequest(long requestID) {
        return requestRepository.getMunicipalityRequestByID(requestID).getOutputFormat();
    }

    /**
     * Returns the Detailed Format of the contest request corresponding to the given ID, if any.
     *
     * @param requestID the ID of the request
     * @return the Detailed Format of the contest request corresponding to the given ID
     * @throws IllegalArgumentException if the request with the given ID is not found or if it is not a
     * contest request
     */
    public static ContestRequestOF viewContestRequest(long requestID) {
        return requestRepository.getContestRequestByID(requestID).getOutputFormat();
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
        Request<?> request = requestRepository.getItemByID(requestID);
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
    private static boolean canAccessRequests(User user, AuthorizationEnum role) {
        return user.getRoles().stream()
                .anyMatch(userRole -> userRole.authorizationEnum().equals(role));
    }

    /**
     * This method deletes a GeoLocatable object.
     *
     * @param objectID the id of the object to delete
     * @param userId   the id of the user who deletes the object
     */
    public static long deleteGeoLocatable(long userId, long objectID, String message) {
        if (!userRepository.getInstance().contains(userId))
            throw new IllegalArgumentException("User not found");
        if (!municipalityRepository.containsGeoLocatable(objectID))
            throw new IllegalArgumentException("GeoLocatable not found");

        return createGeoLocatableDeletionRequest(
                municipalityRepository.getInstance().getGeoLocatableByID(objectID),
                userRepository.getItemByID(userId),
                message);
    }

    private static long createGeoLocatableDeletionRequest(GeoLocatable item, User user, String message) {
        Request<?> request = RequestFactory.getDeletionRequest(user, item, message);
        return addRequest(request);
    }

    /**
     * Returns the possible promotions.
     *
     * @return the possible promotions
     */
    public static List<String> obtainPossiblePromotions() {
        return Arrays.stream(AuthorizationEnum.values())
                .filter(p -> !p.equals(AuthorizationEnum.ADMINISTRATOR))
                .map(Enum::toString)
                .toList();
    }

    /**
     * Inserts a promotion request for the user corresponding to the given ID, for the municipality corresponding to the given ID,
     *
     * @param userId         the ID of the user
     * @param municipalityId the ID of the municipality
     * @param roleType       the role type
     * @param message        the message
     */
    public static long insertPromotionRequest(long userId,
                                              long municipalityId,
                                              AuthorizationEnum roleType,
                                              String message) {
        if (roleType == null || message == null)
            throw new IllegalArgumentException("parameters can't be null");

        Municipality municipality = municipalityRepository.getItemByID(municipalityId);
        User user = userRepository.getItemByID(userId);

        if (user.getAuthorizations(municipalityRepository.getItemByID(municipalityId)).contains(roleType))
            throw new UnsupportedOperationException("User is already a " + roleType.toString());

        Request<User> request = RequestFactory.getPromotionRequest(userRepository.getItemByID(userId),
                new Role(municipality, roleType),
                message);

        return addRequest(request);
    }

    /**
     * This method creates a new report for a GeoLocatable object.
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertGeoLocatableReport(long objectID, String description){
        if(!municipalityRepository.containsGeoLocatable(objectID))
            throw new IllegalArgumentException("GeoLocatable not found");
        GeoLocatable item = municipalityRepository.getGeoLocatableByID(objectID);
        return addRequest(RequestFactory.getDeletionRequest(item, description));
    }

    /**
     * This method creates a new report for a PointOfInterestContent object.
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertPointerOfInterestReport(long objectID, String description){
        if(!municipalityRepository.containsContent(objectID))
            throw new IllegalArgumentException("Content not found");
        if(!(municipalityRepository.getContentByID(objectID) instanceof PointOfInterestContent item))
            throw new IllegalArgumentException("Content is not a PointOfInterestContent");
        return addRequest(RequestFactory.getDeletionRequest(item, description));

    }

    /**
     * This method creates a new report for a ContestContent object.
     * @param objectID the id of the object to report
     * @param description the description of the report
     * @return the id of the report
     */
    public static long insertContentContestReport(long objectID, String description){
        if(!municipalityRepository.containsContent(objectID))
            throw new IllegalArgumentException("Content not found");
        if(!(municipalityRepository.getContentByID(objectID) instanceof ContestContent item))
            throw new IllegalArgumentException("Content is not a ContestContent");
        return addRequest(RequestFactory.getDeletionRequest(item, description));
    }

    /**
     * This method returns the setters of a GeoLocatable object.
     *
     * @param geolocatableID the ID of the GeoLocatable object
     * @return the setters of the GeoLocatable object
     */
    public static List<String> getGeoLocatableSetters(long geolocatableID) {
        return municipalityRepository
                .getGeoLocatableByID(geolocatableID)
                .getSettersMapping()
                .keySet()
                .stream()
                .map(Enum::toString)
                .toList();
    }

    /**
     * Sends a modification request for the GeoLocatable having the given id to the municipality where the
     * GeoLocatable resides.
     * The modification request can be sent only if the user with the given id has at least Curator authorization
     * for the municipality of the GeoLocatable.
     *
     * @param userID the id of the user
     * @param geolocatableID the id of the GeoLocatable object
     * @param pairs the pairs of parameters and values to modify
     * @param message the message of the request
     * @return the id of the request
     */
    public static long modifyGeoLocatable(long userID,
                                          long geolocatableID,
                                          List<Pair<Parameter, Object>> pairs,
                                          String message) {

        GeoLocatable geoLocatable = municipalityRepository.getGeoLocatableByID(geolocatableID);
        User user = userRepository.getItemByID(userID);

        return modifyGeoLocatable(user, geoLocatable, pairs, message);
    }

    private static long modifyGeoLocatable(User user,
                                           GeoLocatable geoLocatable,
                                           List<Pair<Parameter, Object>> pairs,
                                           String message) {

        if (userIsAuthorized(user, geoLocatable.getMunicipality())) {
            modifyGeoLocatableValues(geoLocatable, pairs);
            return Repository.NULL_ID;
        } else {
            return addModifyRequest(pairs, geoLocatable, message);
        }

    }

    private static void modifyGeoLocatableValues(GeoLocatable geoLocatable,
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

    private static boolean userIsAuthorized(User user, Municipality municipality) {
        return Role.isAtLeastContributorForMunicipality(municipality).test(user);
    }

    private static long addRequest(Request<?> request){
        requestRepository.add(request);
        return request.getID();
    }

    private static long addModifyRequest(List<Pair<Parameter, Object>> pairs,
                                         GeoLocatable geoLocatable, String message) {
        Request<?> request = RequestFactory.getModificationRequest(geoLocatable, pairs, message);
        return addRequest(request);
    }


}
