package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.io.File;
import java.util.List;

public record PointOfInterestDOF(String name,
                                 String description,
                                 String position,
                                 MunicipalitySOF municipalitySOF,
                                 List<File> images,
                                 List<ContentSOF> contents,
                                 long id) implements Identifiable {
    @Override
    public long getID() {
        return this.id;
    }
}
