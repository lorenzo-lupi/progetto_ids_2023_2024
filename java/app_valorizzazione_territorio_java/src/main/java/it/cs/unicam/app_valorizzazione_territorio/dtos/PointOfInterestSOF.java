package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.io.File;

public record PointOfInterestSOF(String name,
                                File representativeImage,
                                String classification,
                                long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
