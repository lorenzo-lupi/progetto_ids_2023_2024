package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.PositionParser;

import java.io.File;
import java.util.List;

public record PointOfInterestDOF(String name,
                                 String description,
                                 String position,
                                 MunicipalitySOF municipalitySOF,
                                 List<File> images,
                                 List<ContentSOF> contents,
                                 long id) implements Identifiable, Positionable {
    @Override
    public long getID() {
        return this.id;
    }


    @Override
    public Position getPosition() {
        return PositionParser.parse(this.position());
    }
}
