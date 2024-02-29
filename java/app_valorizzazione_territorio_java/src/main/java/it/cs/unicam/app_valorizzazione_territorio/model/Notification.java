package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(force = true)
public abstract class Notification implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private final String message;

    /////// FOR DELETION PURPOSES /////////
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Municipality municipality;
    @Getter
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "notifications")
    private final List<User> users;
    //////////////////////////////////////

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
        this.users = new ArrayList<>();
    }
    public String message() {
        return message;
    }
    public abstract Visualizable visualizable();
    public abstract void setVisualizableNull(); //For deletion purposes
    @Override
    public long getID() {
        return ID;
    }
    @PreRemove
    public void preRemove() {
        if (this.municipality != null) this.municipality.getNotifications().remove(this);
        this.users.forEach(user -> user.getNotifications().remove(this));
    }

    @Entity
    @DiscriminatorValue("Content")
    @NoArgsConstructor(force = true)
    private static class ContentNotification extends Notification {
        @ManyToOne(fetch = FetchType.EAGER)
        private Content<?> content;
        public ContentNotification(Content<?> content, String message) {
            super(message);
            this.content = content;
            this.content.getNotifications().add(this);
        }
        public Visualizable visualizable() {
            return content;
        }
        public void setVisualizableNull() {
            this.content = null;
        }
        @PreRemove
        public void preRemove() {
            super.preRemove();
            if (this.content != null) this.content.getNotifications().remove(this);
        }
    }

    @Entity
    @DiscriminatorValue("PointOfInterest")
    @NoArgsConstructor(force = true)
    private static class PointOfInterestNotification extends Notification{
        @ManyToOne(fetch = FetchType.EAGER)
        private PointOfInterest pointOfInterest;
        public PointOfInterestNotification(PointOfInterest pointOfInterest, String message) {
            super(message);
            this.pointOfInterest = pointOfInterest;
            this.pointOfInterest.getNotifications().add(this);
        }
        public Visualizable visualizable() {
            return pointOfInterest;
        }
        public void setVisualizableNull() {
            this.pointOfInterest = null;
        }
        @PreRemove
        public void preRemove() {
            super.preRemove();
            if (this.pointOfInterest != null) this.pointOfInterest.getNotifications().remove(this);
        }
    }

    @Entity
    @DiscriminatorValue("Contest")
    @NoArgsConstructor(force = true)
    private static class ContestNotification extends Notification {
        @ManyToOne(fetch = FetchType.EAGER)
        private Contest contest;

        public ContestNotification(Contest contest, String message) {
            super(message);
            this.contest = contest;
            this.contest.getNotifications().add(this);
        }
        public Visualizable visualizable() {
            return contest;
        }
        public void setVisualizableNull() {
            this.contest = null;
        }
        @PreRemove
        public void preRemove() {
            super.preRemove();
            if (this.contest != null) this.contest.getNotifications().remove(this);
        }
    }

}
