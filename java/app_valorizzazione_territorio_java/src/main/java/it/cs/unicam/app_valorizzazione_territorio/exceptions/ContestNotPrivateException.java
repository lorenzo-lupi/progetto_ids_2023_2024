package it.cs.unicam.app_valorizzazione_territorio.exceptions;

public class ContestNotPrivateException extends IllegalStateException{
    public ContestNotPrivateException(String message) {
        super(message);
    }
}
