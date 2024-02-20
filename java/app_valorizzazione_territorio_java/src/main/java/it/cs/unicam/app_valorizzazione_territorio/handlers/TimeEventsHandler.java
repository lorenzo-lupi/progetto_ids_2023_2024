package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.*;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Event;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;

/**
 * This class is responsible for handling time events, that is, events that should be triggered by due dates
 * and expiration of time periods.
 */
public class TimeEventsHandler {

    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    /**
     * Ends the contest with the given ID, proclaiming the winner if there is one.
     * If there is a winner, a notification is added to the list of notification of the municipality
     * where the contest resides.
     * Moreover, if the contest is private all the participants are notified about the winning proposal, while
     * if the contest is related to a point of interest, all the proposals of the contest are added to the list
     * of approved contents of the point of interest.
     *
     * @param contestId the ID of the contest to end
     */
    public static void endContest(long contestId) {
        Contest contest = municipalityRepository.getContestByID(contestId);
        ContestContent winner = contest.getProposalRequests().getWinner();
        if (winner != null) {
            Notification winnerNotification = new Notification(winner,
                    "Proclaimed the winner of the contest: " + contest.getName() + "!");
            contest.getMunicipality().addNotification(winnerNotification);
            if (contest.isPrivate()) {
                contest.getParticipants().forEach(user -> user.addNotification(winnerNotification));
            }
            if (contest.hasGeoLocation() && contest.getGeoLocation() instanceof PointOfInterest pointOfInterest) {
                PointOfInterestContentBuilder contestContentBuilder = new PointOfInterestContentBuilder(pointOfInterest);
                ContentDirector director = new ContentDirector(contestContentBuilder);
                for (Content<Contest> content : contest.getApprovedContents()) {
                    director.makeFrom(content);
                    contestContentBuilder.getResult().approve();
                    pointOfInterest.addContent(contestContentBuilder.getResult());
                }
            }
        }
    }

    /**
     * Starts the event with the given ID.
     * A notification is added to the list of notification of the municipality where the event resides.
     *
     * @param eventId the ID of the event to start
     */
    public static void startEvent(long eventId) {
        PointOfInterest event = municipalityRepository.getPointOfInterestByID(eventId);
        if (event instanceof Event e) {
            e.getMunicipality().addNotification(
                    new Notification(e, "The event " + e.getName() + " has started!"));
        }
    }
}
