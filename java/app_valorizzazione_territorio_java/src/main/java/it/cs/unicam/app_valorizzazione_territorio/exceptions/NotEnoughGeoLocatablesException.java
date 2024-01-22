package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class NotEnoughGeoLocatablesException extends IllegalStateException{
    public NotEnoughGeoLocatablesException(String message) {
        super(message);
    }
}
