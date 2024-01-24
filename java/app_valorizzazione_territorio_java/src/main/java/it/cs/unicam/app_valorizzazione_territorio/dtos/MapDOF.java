package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;

import java.util.List;

public record MapDOF(String osmData,
                     List<Identifiable> points,
                     long id) implements Identifiable {
    @Override
    public long getID() {
        return this.id;
    }
}
