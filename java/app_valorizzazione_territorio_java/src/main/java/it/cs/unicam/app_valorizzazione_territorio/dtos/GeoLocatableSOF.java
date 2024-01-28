package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.io.File;

public record GeoLocatableSOF(String name,
                                 File representativeImage,
                                 String geoLocatableType,
                                 long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
