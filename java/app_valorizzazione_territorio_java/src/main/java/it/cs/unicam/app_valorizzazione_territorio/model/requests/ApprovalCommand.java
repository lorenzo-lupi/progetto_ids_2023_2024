package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import jakarta.persistence.*;

/**
 * Objects implementing this class encapsulate the actions to be performed on an approvable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
@Entity
@DiscriminatorValue("Approval")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "approvable_item", discriminatorType = DiscriminatorType.STRING)
public abstract class ApprovalCommand<T extends Approvable & Visualizable> extends RequestCommand<T> {

    @SuppressWarnings("unchecked")
    public static<I extends Approvable & Visualizable> ApprovalCommand<I> createApprovalCommand(I item) {
        if (item instanceof Content<?> content)
            return (ApprovalCommand<I>) new ContentApprovalCommand(content);
        if (item instanceof GeoLocatable geoLocatable)
            return (ApprovalCommand<I>) new GeoLocatableApprovalCommand(geoLocatable);
        return null;
    }

    public ConfirmationType getConfirmationType() {
        return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        getItem().approve();
    }

    @Override
    public void reject() {
        getItem().reject();
    }

    @Entity
    @DiscriminatorValue("ApprovableContent")
    private static class ContentApprovalCommand extends ApprovalCommand<Content<?>> {
        @Transient //TODO: @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "content_id")
        private final Content<?> content;
        public ContentApprovalCommand(Content<?> content) {
            this.content = content;
        }
        public Content<?> getItem() {
            return content;
        }
    }

    @Entity
    @DiscriminatorValue("ApprovableGeoLocatable")
    private static class GeoLocatableApprovalCommand extends ApprovalCommand<GeoLocatable> {
        @Transient //TODO: @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "geoLocatable_id")
        private final GeoLocatable geoLocatable;
        public GeoLocatableApprovalCommand(GeoLocatable geoLocatable) {
            this.geoLocatable = geoLocatable;
        }
        public GeoLocatable getItem() {
            return geoLocatable;
        }
    }
}
