package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import jakarta.persistence.*;

/**
 * Classes implementing this interface provide a set of actions to be performed on their objects
 * in order to accept or reject a request.
 *
 * @param <T> the type of the item affected by the request command.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "request_command_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RequestCommand<T extends Visualizable> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long ID;

        public abstract T getItem();

        /**
         * Returns the type of confirmation required to accept the request.
         *
         * @return the type of confirmation required to accept the request.
         */
        public abstract ConfirmationType getConfirmationType();

        /**
         * Accepts the request.
         */
        public abstract void accept();

        /**
         * Rejects the request.
         */
        public abstract void reject();
}
