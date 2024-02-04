package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class DescriptionNotSetException extends IllegalStateException{
    public DescriptionNotSetException(String message) {
        super(message);
    }
}
