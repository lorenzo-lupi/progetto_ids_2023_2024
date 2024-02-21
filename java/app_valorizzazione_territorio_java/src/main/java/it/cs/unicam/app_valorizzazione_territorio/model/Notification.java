package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private final String message;

    /**
     * Factory method for creating a notification.
     *
     * @param visualizable visualizable object
     * @param message message of the notification
     * @return a notification
     */
    public static Notification createNotification(Visualizable visualizable, String message) {
        if (visualizable instanceof Contest contest) return new ContestNotification(contest, message);
        if (visualizable instanceof Content<?> content) return new ContentNotification(content, message);
        if (visualizable instanceof PointOfInterest poi) return new PointOfInterestNotification(poi, message);
        return null;
    }

    public Notification(String message) {
        this.message = message;
    }
    public String message() {
        return message;
    }
    public abstract Visualizable visualizable();

    @Entity
    @DiscriminatorValue("Content")
    private static class ContentNotification extends Notification {
        @Transient //TODO: @ManyToOne(fetch = FetchType.EAGER)
        private final Content<?> content;
        public ContentNotification(Content<?> content, String message) {
            super(message);
            this.content = content;
        }
        public Visualizable visualizable() {
            return content;
        }
    }

    @Entity
    @DiscriminatorValue("PointOfInterest")
    private static class PointOfInterestNotification extends Notification{
        @Transient //TODO : @ManyToOne(fetch = FetchType.EAGER)
        private final PointOfInterest pointOfInterest;
        public PointOfInterestNotification(PointOfInterest pointOfInterest, String message) {
            super(message);
            this.pointOfInterest = pointOfInterest;
        }
        public Visualizable visualizable() {
            return pointOfInterest;
        }
    }

    @Entity
    @DiscriminatorValue("Contest")
    private static class ContestNotification extends Notification {
        @ManyToOne(fetch = FetchType.EAGER)
        private final Contest contest;

        public ContestNotification(Contest contest, String message) {
            super(message);
            this.contest = contest;
        }
        public Visualizable visualizable() {
            return contest;
        }
    }

}
