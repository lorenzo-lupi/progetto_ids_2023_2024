package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class ContestHasNoGeoLocatableException extends IllegalStateException{
    public ContestHasNoGeoLocatableException(String message) {
        super(message);
    }
}
