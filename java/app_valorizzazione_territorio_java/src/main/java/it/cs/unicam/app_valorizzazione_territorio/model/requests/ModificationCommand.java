package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Objects implementing this class encapsulate the actions to be performed on a modifiable item in order
 * to accept or reject a request.
 * @param <T> the type of the item affected by the request command.
 */
@Entity
@DiscriminatorValue("Modification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "modifiable_item", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(force = true)
public abstract class ModificationCommand<T extends Visualizable & Modifiable> extends RequestCommand<T>{

    @ElementCollection
    @CollectionTable(name = "modifications", joinColumns = @JoinColumn(name = "command_id"))
    private final List<Modification> modifications;

    public static<I extends Modifiable & Visualizable> ModificationCommand<I> createModificationCommand(
            I item, Parameter parameter, Object value) {
        return createModificationCommand(item, List.of(Pair.of(parameter, value)));
    }

    @SuppressWarnings("unchecked")
    public static<I extends Modifiable & Visualizable> ModificationCommand<I> createModificationCommand(
            I item, List<Pair<Parameter, Object>> modifications) {
        if (item instanceof Content<?> content)
            return (ModificationCommand<I>) new ContentModificationCommand(content, modifications);
        if (item instanceof GeoLocatable geoLocatable)
            return (ModificationCommand<I>) new GeoLocatableModificationCommand(geoLocatable, modifications);
        if (item instanceof User user)
            return (ModificationCommand<I>) new UserModificationCommand(user, modifications);
        return null;
    }

    /**
     * Constructor for a modification command.
     * @param modifications the modifications to be performed
     */
    public ModificationCommand(List<Pair<Parameter, Object>> modifications) {
        this.modifications = modifications.stream()
                .map(p -> new Modification(p.getKey(), p.getValue()))
                .toList();
    }

    /**
     * Constructor for a modification command.
     * @param parameter the parameter to be modified
     * @param value the new value of the parameter
     */
    public ModificationCommand(Parameter parameter, Object value) {
        this.modifications = new LinkedList<>();
        this.modifications.add(new Modification(parameter, value));
    }

    public ConfirmationType getConfirmationType() {
        if(modifications.stream().anyMatch(p -> p.getParameter() == Parameter.ADD_ROLE && p.getNewValue() instanceof Role r
                && r.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)) {
            return ConfirmationType.PROMOTION_TO_ADMIN;
        }
        else return ConfirmationType.NONE;
    }

    @Override
    public void accept() {
        modifications.forEach(p -> getItem().getSettersMapping().get(p.getParameter()).accept(p.getNewValue()));
    }

    @Override
    public void reject() {
        // Do nothing
    }

    @Embeddable
    @NoArgsConstructor(force = true)
    private static class Modification implements Serializable {
        @Enumerated(EnumType.STRING)
        private final Parameter parameter;
        @Lob
        @Column(columnDefinition="BLOB")
        private final Object newValue;
        public Modification(Parameter parameter, Object newValue) {
            this.parameter = parameter;
            this.newValue = newValue;
        }
        public Parameter getParameter() {
            return parameter;
        }
        public Object getNewValue() {
            return newValue;
        }
    }

    @Entity
    @DiscriminatorValue("ModifiableContent")
    @NoArgsConstructor(force = true)
    private static class ContentModificationCommand extends ModificationCommand<Content<?>> {
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "content_id")
        private final Content<?> content;
        public ContentModificationCommand(Content<?> content, List<Pair<Parameter, Object>> modifications) {
            super(modifications);
            this.content = content;
        }
        public ContentModificationCommand(Content<?> content, Parameter parameter, Object value) {
            super(parameter, value);
            this.content = content;
        }
        public Content<?> getItem() {
            return content;
        }
    }

    @Entity
    @DiscriminatorValue("ModifiableGeoLocatable")
    private static class GeoLocatableModificationCommand extends ModificationCommand<GeoLocatable> {
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "geo_locatable_id")
        private final GeoLocatable geoLocatable;
        public GeoLocatableModificationCommand(GeoLocatable geoLocatable, List<Pair<Parameter, Object>> modifications) {
            super(modifications);
            this.geoLocatable = geoLocatable;
        }
        public GeoLocatableModificationCommand(GeoLocatable geoLocatable, Parameter parameter, Object value) {
            super(parameter, value);
            this.geoLocatable = geoLocatable;
        }
        public GeoLocatable getItem() {
            return geoLocatable;
        }
    }

    @Entity
    @DiscriminatorValue("ModifiableUser")
    private static class UserModificationCommand extends ModificationCommand<User> {
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private final User user;
        public UserModificationCommand(User user, List<Pair<Parameter, Object>> modifications) {
            super(modifications);
            this.user = user;
        }
        public UserModificationCommand(User user, Parameter parameter, Object value) {
            super(parameter, value);
            this.user = user;
        }
        public User getItem() {
            return user;
        }
    }
}
