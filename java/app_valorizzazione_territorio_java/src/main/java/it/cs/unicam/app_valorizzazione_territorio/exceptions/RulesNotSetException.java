package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class RulesNotSetException extends IllegalStateException{
    public RulesNotSetException(String message) {
        super(message);
    }
}
