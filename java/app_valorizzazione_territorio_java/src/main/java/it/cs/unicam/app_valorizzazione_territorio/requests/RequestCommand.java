package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;

/**
 * Classes implementing this interface provide a set of actions to be performed on their objects
 * in order to accept or reject a request.
 *
 * @param <T> the type of the item affected by the request command.
 */
public abstract class RequestCommand<T extends Visualizable> {

        private final T item;

        public RequestCommand(T item) {
            this.item = item;
        }

        public T getItem() {
            return item;
        }

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
