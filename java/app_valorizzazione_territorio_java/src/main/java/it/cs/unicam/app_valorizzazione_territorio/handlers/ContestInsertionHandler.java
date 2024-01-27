package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.ContestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.Date;
import java.util.List;

public class ContestInsertionHandler {

    private final SearchHandler<GeoLocatable> geoLocatableSearchHandler;
    private final SearchHandler<User> userSearchHandler;
    private final User user;
    private final Municipality municipality;
    private final ContestBuilder builder;

    public static final String INVITATION_MESSAGE = "You have been invited to participate in a contest";

    /**
     * Creates a new ContestInsertionHandler for the user with the given ID and the municipality corresponding
     * to the given ID.
     *
     * @param userID the ID of the user
     * @param municipalityID the ID of the municipality
     * @throws IllegalArgumentException if the user is not authorized to insert contests in the municipality
     */
    public ContestInsertionHandler(long userID, long municipalityID) {
        this.user = UserRepository.getInstance().getItemByID(userID);
        this.municipality = MunicipalityRepository.getInstance().getItemByID(municipalityID);
        if (!user.getAuthorizations(municipality).contains(RoleTypeEnum.ENTERTAINER))
            throw new IllegalArgumentException("User is not authorized to insert contests in this municipality");

        this.builder = new ContestBuilder(user);
        this.geoLocatableSearchHandler = new SearchHandler<>(municipality.getGeoLocatables());
        this.userSearchHandler = new SearchHandler<>(UserRepository.getInstance().getItemStream().toList());
    }

    /**
     * Inserts the name of the contest.
     *
     * @param name the name of the contest
     * @throws IllegalArgumentException if the name is null
     */
    public void insertName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name cannot be null");
        builder.setName(name);
    }

    /**
     * Inserts the topic of the contest.
     *
     * @param topic the topic of the contest
     * @throws IllegalArgumentException if the topic is null
     */
    public void insertTopic(String topic) {
        if (topic == null)
            throw new IllegalArgumentException("Topic cannot be null");
        builder.setTopic(topic);
    }

    /**
     * Inserts the start date of the contest.
     *
     * @param rules the rules of the contest
     * @throws IllegalArgumentException if the rules are null
     */
    public void insertRules(String rules) {
        if (rules == null)
            throw new IllegalArgumentException("Rules cannot be null");
        builder.setRules(rules);
    }

    /**
     * Inserts the start date of the contest.
     *
     * @param startDate the start date of the contest
     * @throws IllegalArgumentException if the start date is null, or if it is before the current date
     * or if it is after the voting start date or if it is after the end date.
     */
    public void insertStartDate(Date startDate) {
        builder.setStartDate(startDate);
    }

    /**
     * Inserts the voting start date of the contest.
     *
     * @param votingStartDate the voting start date of the contest
     * @throws IllegalArgumentException if the voting start date is null, or if it is before the current date
     * or if it is before the start date or if it is after the end date.
     */
    public void insertVotingStartDate(Date votingStartDate) {
        builder.setVotingStartDate(votingStartDate);
    }

    /**
     * Inserts the end date of the contest.
     *
     * @param endDate the end date of the contest
     * @throws IllegalArgumentException if the end date is null, or if it is before the current date
     * or if it is before the start date or if it is before the voting start date.
     */
    public void insertEndDate(Date endDate) {
        builder.setEndDate(endDate);
    }

    /**
     * Sets the contest as private or as public based on the boolean value provided.
     * A true value sets the contest as private, a false value sets the contest as public.
     *
     * @param isPrivate true if the contest is private, false otherwise
     */
    public void insertVisibility(boolean isPrivate) {
        if (isPrivate) builder.setPrivate();
        else builder.setPublic();
    }

    /**
     * Starts the search for a user.
     */
    public void startUserSearch() {
        userSearchHandler.startSearch();
    }

    /**
     * Sets a search criterion for the user search.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param parameter the user parameter to apply the criterion on
     * @param criterion the criterion to add
     * @param value the reference value
     * @throws IllegalArgumentException if any of the arguments is null or invalid
     */
    public void setUserSearchCriterion(String parameter, String criterion, Object value){
        userSearchHandler.setSearchCriterion(parameter, criterion, value);
    }

    /**
     * Sets a search criterion for the user search.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param filter the filter to add
     * @throws IllegalArgumentException if the filter is null or any of the parameters
     * in the filter is null or invalid
     */
    public void setUserSearchCriterion(SearchFilter filter){
        userSearchHandler.setSearchCriterion(filter);
    }

    /**
     * Returns the search result based on the previously submitted search criteria.
     *
     * @return the search result of the search engine
     */
    @SuppressWarnings("unchecked")
    public List<UserSOF> getUserSearchResult(){
        return (List<UserSOF>) userSearchHandler.getSearchResult();
    }

    /**
     * Adds a participant to the contest.
     *
     * @param userID the ID of the participant to add
     * @throws IllegalArgumentException if the user is null or if the user is not found
     * @throws IllegalStateException if the contest is currently set as public
     */
    public void addParticipant(long userID) {
        builder.addParticipant(UserRepository.getInstance().getItemByID(userID));
    }

    /**
     * Removes an invitation to the contest.
     *
     * @param userID the ID of the user to remove the invitation to
     * @throws IllegalArgumentException if the user is null or if the user is not found
     * @throws IllegalStateException if the contest is currently set as public
     */
    public void removeParticipant(long userID) {
        builder.removeParticipant(UserRepository.getInstance().getItemByID(userID));
    }

    /**
     * Starts the search for a geo-locatable.
     */
    public void startGeoLocatableSearch() {
        geoLocatableSearchHandler.startSearch();
    }

    /**
     * Sets a search criterion for the geo-locatable search.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param parameter the geo-locatable parameter to apply the criterion on
     * @param criterion the criterion to add
     * @param value the reference value
     * @throws IllegalArgumentException if any of the arguments is null or invalid
     */
    public void setGeoLocatableSearchCriterion(String parameter, String criterion, Object value){
        geoLocatableSearchHandler.setSearchCriterion(parameter, criterion, value);
    }

    /**
     * Sets a search criterion for the geo-locatable search.
     * Subsequent searches will be performed on the given parameters.
     *
     * @param filter the filter to add
     * @throws IllegalArgumentException if the filter is null or any of the parameters
     * in the filter is null or invalid
     */
    public void setGeoLocatableSearchCriterion(SearchFilter filter){
        geoLocatableSearchHandler.setSearchCriterion(filter);
    }

    /**
     * Returns the search result based on the previously submitted search criteria.
     *
     * @return the search result of the search engine
     */
    @SuppressWarnings("unchecked")
    public List<GeoLocatableSOF> getGeoLocatableSearchResult(){
        return (List<GeoLocatableSOF>) geoLocatableSearchHandler.getSearchResult();
    }

    /**
     * Inserts the geolocation of the contest.
     *
     * @param geoLocatableID the ID of the geolocation of the contest
     * @throws IllegalArgumentException if the geolocation is null or if the geolocation is not
     * found in the municipality
     */
    public void insertGeoLocation(long geoLocatableID) {
        builder.setGeoLocation(MunicipalityRepository.getInstance().getGeoLocatableByID(geoLocatableID));
    }

    /**
     * Removes the geo-location of the contest, if any.
     */
    public void removeGeoLocation() {
        builder.removeGeoLocation();
    }

    /**
     * Inserts the created contest in the municipality.
     * If the contest is private, a notification with a standard message is sent to all the participants.
     */
    public void insertContest() {
        insertContest(INVITATION_MESSAGE);
    }

    /**
     * Inserts the created contest in the municipality.
     * If the contest is private, a notification with the given message is sent to all the participants.
     *
     * @param message the message to send to the participants
     */
    public void insertContest(String message) {
        this.builder.build();
        if (builder.getResult().isPrivate()) {
            Notification contestNotification = new Notification(this.builder.getResult(), message);
            builder.getResult().getParticipants().forEach(user -> user.addNotification(contestNotification));
        }
        municipality.addContest(builder.getResult());
    }

}
