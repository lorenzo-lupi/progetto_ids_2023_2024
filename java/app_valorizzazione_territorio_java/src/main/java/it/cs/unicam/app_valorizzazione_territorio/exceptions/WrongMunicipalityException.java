package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class WrongMunicipalityException extends IllegalArgumentException{
    public WrongMunicipalityException(String message) {
        super(message);
    }
}
