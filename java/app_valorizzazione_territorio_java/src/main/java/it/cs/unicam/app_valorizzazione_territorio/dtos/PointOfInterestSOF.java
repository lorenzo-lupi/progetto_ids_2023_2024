package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

public record PointOfInterestSOF(String title,
                                String position,
                                long id) implements Identifiable {
    @Override
    public long getID() {
        return this.id();
    }
}
