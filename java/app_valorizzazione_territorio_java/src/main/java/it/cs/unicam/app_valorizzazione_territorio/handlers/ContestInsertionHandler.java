package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.ContestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

public class ContestInsertionHandler {

    public static final String INVITATION_MESSAGE = "You have been invited to participate in a contest";


    /**
     * Inserts a contest in the municipality corresponding to the given ID.
     *
     * @param municipalityID the ID of the municipality
     * @param contestIF the DTO of the contest to insert
     * @return the ID of the inserted contest
     * @throws IllegalArgumentException if the animator of the contest is not authorized to insert contests
     * in the municipality, or if the animator is null, or if the contest is null or if the contest is invalid
     */
    public static long insertContest(long userID, long municipalityID, ContestIF contestIF){
        ContestBuilder builder = new ContestBuilder(UserRepository.getInstance().getItemByID(userID),
                MunicipalityRepository.getInstance().getItemByID(municipalityID));

        builder.setName(contestIF.name())
                .setTopic(contestIF.topic())
                .setRules(contestIF.rules())
                .setStartDate(contestIF.startDate())
                .setVotingStartDate(contestIF.votingStartDate())
                .setEndDate(contestIF.endDate());

        if (contestIF.isPrivate()) {
            builder.setPrivate();
            contestIF.userIDs().stream()
                    .map(UserRepository.getInstance()::getItemByID)
                    .forEach(builder::addParticipant);
        }
        if (contestIF.geoLocatableID() != null)
            builder.setGeoLocation(MunicipalityRepository.getInstance().getGeoLocatableByID(contestIF.geoLocatableID()));

        builder.build();

        MunicipalityRepository.getInstance().getItemByID(municipalityID).addContest(builder.getResult());
        if (contestIF.isPrivate()) sendNotifications(builder.getResult(), INVITATION_MESSAGE);
        return builder.getResult().getID();
    }

    /**
     * Performs a search on the list of users in the system using the given filters.
     * The filters are applied in logical and.
     *
     * @param filters the filters to apply
     * @return the list of users corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<UserSOF> viewFilteredUsers(List<SearchFilter> filters){
        return (List<UserSOF>) SearchHandler.getFilteredItems(
                UserRepository.getInstance().getItemStream().toList(),
                filters);
    }

    /**
     * Performs a search on the list of geo-locatables in the system using the given filters.
     * The filters are applied in logical and.
     *
     * @param filters the filters to apply
     * @return the list of geo-locatable corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<GeoLocatableSOF> viewFilteredGeoLocatables(List<SearchFilter> filters){
        return (List<GeoLocatableSOF>) SearchHandler.getFilteredItems(
                MunicipalityRepository.getInstance().getAllGeoLocatables().toList(),
                filters);
    }



    private static void sendNotifications(Contest contest, String message) {
        Notification contestNotification = new Notification(contest, message);
        contest.getParticipants().forEach(user -> user.addNotification(contestNotification));
    }

}
