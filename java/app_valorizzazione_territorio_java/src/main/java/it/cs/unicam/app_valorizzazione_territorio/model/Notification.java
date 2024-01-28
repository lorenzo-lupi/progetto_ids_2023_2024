package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;

public record Notification(Visualizable visualizable, String message) {
}
