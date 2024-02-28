package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.Notification;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.*;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Event;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.ContestJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.NotificationJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is responsible for handling time events, that is, events that should be triggered by due dates
 * and expiration of time periods.
 */
@Service
public class TimeEventsHandler {

    private final ContestJpaRepository contestJpaRepository;
    private final GeoLocatableJpaRepository geoLocatableJpaRepository;
    private final NotificationJpaRepository notificationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public TimeEventsHandler(ContestJpaRepository contestJpaRepository,
                             GeoLocatableJpaRepository geoLocatableJpaRepository,
                             NotificationJpaRepository notificationJpaRepository,
                             UserJpaRepository userJpaRepository) {
        this.contestJpaRepository = contestJpaRepository;
        this.geoLocatableJpaRepository = geoLocatableJpaRepository;
        this.notificationJpaRepository = notificationJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

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
    public void endContest(long contestId) {
        Optional<Contest> contestOptional = contestJpaRepository.findByBaseContestIdAndValidTrue(contestId);
        if (contestOptional.isEmpty())
            throw new IllegalArgumentException("Contest not found");

        Contest contest = contestOptional.get();
        ContestContent winner = contest.getProposalRegister().getWinner();
        if (winner != null) {
            Notification winnerNotification = notificationJpaRepository.save(Notification.createNotification(
                    winner, "Proclaimed the winner of the contest: " + contest.getName() + "!"));
            contest.getMunicipality().addNotification(winnerNotification);
            if (contest.isPrivate()) {
                contest.getParticipants().forEach(user -> {
                    user.addNotification(winnerNotification);
                    userJpaRepository.save(user);
                });
            }
            if (contest.hasGeoLocation() && contest.getGeoLocation() instanceof PointOfInterest pointOfInterest) {
                PointOfInterestContentBuilder contestContentBuilder = new PointOfInterestContentBuilder(pointOfInterest);
                ContentDirector director = new ContentDirector(contestContentBuilder);
                for (Content<Contest> content : contest.getApprovedContents()) {
                    director.makeFrom(content);
                    contestContentBuilder.getResult().approve();
                    pointOfInterest.addContent(contestContentBuilder.getResult());
                }
                geoLocatableJpaRepository.save(pointOfInterest);
            }
        }
    }

    /**
     * Starts the event with the given ID.
     * A notification is added to the list of notification of the municipality where the event resides.
     *
     * @param eventId the ID of the event to start
     * @throws IllegalArgumentException if the event is not found
     */
    public void startEvent(long eventId) {
        Optional<Event> optionalEvent = geoLocatableJpaRepository.findEventById(eventId);
        if (optionalEvent.isEmpty())
            throw new IllegalArgumentException("Event not found");

        Event e = optionalEvent.get();
        e.getMunicipality().addNotification(notificationJpaRepository.save(
                Notification.createNotification(e, "The event " + e.getName() + " has started!")));
    }
}
