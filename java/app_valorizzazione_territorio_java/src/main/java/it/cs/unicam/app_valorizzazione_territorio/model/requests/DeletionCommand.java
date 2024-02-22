package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Deletable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import jakarta.persistence.*;

/**
 * Objects implementing this class encapsulate the actions to be performed on a deletable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
@Entity
@DiscriminatorValue("Deletion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "deletable_item", discriminatorType = DiscriminatorType.STRING)
public abstract class DeletionCommand<T extends Visualizable & Deletable> extends RequestCommand<T> {

    @SuppressWarnings("unchecked")
    public static<I extends Deletable & Visualizable> DeletionCommand<I> createDeletionCommand(I item) {
        if (item instanceof Content<?> content)
            return (DeletionCommand<I>) new ContentDeletionCommand(content);
        if (item instanceof GeoLocatable geoLocatable)
            return (DeletionCommand<I>) new GeoLocatableDeletionCommand(geoLocatable);
        return null;
    }

    public ConfirmationType getConfirmationType() {
        if (this.getItem() instanceof PointOfInterest) {
            return ConfirmationType.CASCADE_DELETION;
        }
        else return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        getItem().getDeletionAction().run();
    }

    @Override
    public void reject() {
        // Do nothing
    }

    @Entity
    @DiscriminatorValue("DeletableContent")
    private static class ContentDeletionCommand extends DeletionCommand<Content<?>> {
        @Transient //TODO: @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "content_id")
        private final Content<?> content;
        public ContentDeletionCommand(Content<?> content) {
            this.content = content;
        }
        public Content<?> getItem() {
            return content;
        }
    }

    @Entity
    @DiscriminatorValue("DeletableGeoLocatable")
    private static class GeoLocatableDeletionCommand extends DeletionCommand<GeoLocatable> {
        @Transient //TODO: @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "geoLocatable_id")
        private final GeoLocatable geoLocatable;
        public GeoLocatableDeletionCommand(GeoLocatable geoLocatable) {
            this.geoLocatable = geoLocatable;
        }
        public GeoLocatable getItem() {
            return geoLocatable;
        }
    }


}