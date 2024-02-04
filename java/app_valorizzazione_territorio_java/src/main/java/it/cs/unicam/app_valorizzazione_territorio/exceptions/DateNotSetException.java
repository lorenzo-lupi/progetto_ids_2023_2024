package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class DateNotSetException extends IllegalStateException{
    public DateNotSetException(String message) {
        super(message);
    }
}
