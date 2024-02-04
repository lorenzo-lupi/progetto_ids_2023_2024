package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class GeoLocatableNotFoundException extends IllegalArgumentException{
    public GeoLocatableNotFoundException(String message) {
        super(message);
    }
}
