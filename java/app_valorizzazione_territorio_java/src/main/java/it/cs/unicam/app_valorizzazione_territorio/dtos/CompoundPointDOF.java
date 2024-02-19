package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPointTypeEnum;

import java.util.List;

public record CompoundPointDOF (
        long ID,
        String title,
        String description,
        CompoundPointTypeEnum type,
        List<GeoLocatableSOF> pointsOfInterest
        )  implements Identifiable{

    @Override
    public long getID() {
        return this.ID;
    }
}
